## Generyki

Java została pierwotnie zaprojektowana tak, że wszystkie listy miały ten sam typ `List`, zamiast konkretnych list z określonymi parametrami typów, takimi jak `List<String>` czy `List<Int>`. Typ `List` w Javie akceptuje wszystkie rodzaje wartości; gdy prosisz o wartość na określonej pozycji, typem wyniku jest `Object` (który w Javie jest nadtypem wszystkich typów).

```
// Java
List names = new ArrayList();
names.add("Alex");
names.add("Ben");
names.add(123); // to jest niepoprawne, ale się kompiluje
for(int i = 0; i < names.size(); i++){
   String name= (String) names.get(i); // wyjątek przy i==2
   System.out.println(name.toUpperCase());
}
```

Trudno się używa takich list. Stanowczo preferujemy listy z określonymi typami elementów. Dopiero wtedy możemy być pewni, że nasza lista zawiera elementy właściwego typu i dopiero wtedy nie musimy jawnie rzutować tych elementów, gdy pobieramy je z listy. To był jeden z głównych powodów wprowadzenia typów generycznych w wersji 5 Javy. W Kotlinie nie mamy tego problemu, ponieważ został on zaprojektowany z obsługą typów generycznych od samego początku, a wszystkie listy są generyczne, więc muszą określić, jakie rodzaje elementów akceptują. Generyki są ważną cechą większości nowoczesnych języków programowania, dlatego w tym rozdziale omówimy, czym są i jak używamy ich w Kotlinie.

W Kotlinie mamy trzy rodzaje elementów generycznych:
* funkcje generyczne,
* klasy generyczne,
* interfejsy generyczne.

Omówmy je kolejno.

### Funkcje generyczne

Tak jak możemy przekazać wartość jako argument do parametru, możemy przekazać typ jako **argument typu** do **parametru typu**. Aby to zrobić, funkcja musi zdefiniować jeden lub więcej parametr typu w nawiasach ostrokątnych zaraz po słowie kluczowym `fun`. Zgodnie z tą konwencją, nazwy parametrów typu pisane są wielkimi literami. Gdy funkcja definiuje parametr typu, jej wywołanie musi określić argumenty typu. Parametr typu jest symbolem zastępczym dla konkretnego typu; argument typu to rzeczywisty typ używany podczas wywoływania funkcji. Aby jawnie określić argumenty typu, używamy również nawiasów ostrokątnych.

```kotlin
fun <T> a() {} // T to parametr typu
a<Int>() // Int jest tutaj używane jako argument typu
a<String>() // String jest tutaj używane jako argument typu
```

Istnieje popularna praktyka, że pojedynczy argument typu nazywa się `T` (od "type"); jeśli jest kilka argumentów typu, nazywają się `T` z kolejnymi liczbami. Ta praktyka jednak nie jest sztywną regułą i istnieje wiele innych konwencji nazewnictwa parametrów typu.

```kotlin
fun <T> a() {}
fun <T1, T2> b() {}
```

Gdy wywołujemy funkcję generyczną, wszystkie jej argumenty typu muszą być jasne dla kompilatora Kotlin. Możemy je albo jawnie określić, albo ich wartości mogą być wnioskowane przez kompilator.

```kotlin
fun <T> a() {}
fun <T1, T2> b() {}
fun <T> c(t: T) {}
fun <T1, T2> d(a: T1, b: T2) {}
fun <T> e(): T = TODO()

fun main() {
    // Jawnie określone argumenty typu
    a<Int>()
    a<String>()
    b<Double, Char>()
    b<Float, Long>()

    // Wnioskowane argumenty typu
    c(10) // Wnioskowany typ T to Int
    d("AAA", 10.0)
    // Wnioskowany typ T1 to String, a T2 to Double
    val e: Boolean = e() // Wnioskowany typ T to Boolean
}
```

W jaki sposób parametry typu mogą być przydatne? Używamy ich głównie do określenia związku między typami argumentów oraz typem wyniku. Możemy, na przykład, możemy wyrazić, że typ wyniku jest taki sam jak typ argumentu lub że oczekujemy dwóch argumentów tego samego typu.

```kotlin
import kotlin.random.Random

// Typ wyniku jest taki sam jak typ argumentu
fun <T> id(value: T): T = value

// Typ wyniku to najbliższy nadrzędny typ argumentów
fun <T> randomOf(a: T, b: T): T =
    if (Random.nextBoolean()) a else b

fun main() {
    val a = id(10) // Wnioskowany typ a to Int
    val b = id("AAA") // Wnioskowany typ b to String
    val c = randomOf("A", "B") // Wnioskowany typ c to String
    val d = randomOf(1, 1.5) // Wnioskowany typ d to Number
}
```

Parametry typu dla funkcji są przydatne dla kompilatora, ponieważ pozwalają mu sprawdzać i poprawnie wnioskować typy; sprawia to, że nasze programy są bezpieczniejsze, a programowanie staje się przyjemniejsze dla programistów. Chronią nas one przed używaniem niedozwolonych operacji i pozwalają naszemu IDE dawać lepsze sugestie.

W książce *Funkcjonalny Kotlin* zobaczysz wiele przykładów funkcji generycznych, zwłaszcza do przetwarzania kolekcji. Takie funkcje są naprawdę ważne i przydatne. Na razie wróćmy do początkowej motywacji wprowadzenia generyków: porozmawiajmy o klasach generycznych.

### Klasy generyczne

Klasy możemy uczynić generycznymi, określając parametr typu po nazwie klasy. Taki parametr typu można używać na całym obszarze klasy, zwłaszcza do określania właściwości, parametrów i typów wyników. Parametr typu jest określany, gdy definiujemy instancję, po czym pozostaje niezmieniony. Dzięki temu, gdy zadeklarujesz `ValueWithHistory<String>` i następnie wywołasz `setValue` w poniższym przykładzie, musisz użyć obiektu typu `String`; gdy wywołasz `currentValue`, obiekt wynikowy będzie miał typ `String`; a gdy wywołasz `history`, jego wynik będzie typu `List<String>`. To samo dotyczy wszystkich innych możliwych argumentów typu.

```kotlin
class ValueWithHistory<T>(
    private var value: T
) {
    private var history: List<T> = listOf(value)

    fun setValue(value: T) {
        this.value = value
        this.history += value
    }

    fun currentValue(): T = value

    fun history(): List<T> = history
}

fun main() {
    val letter = ValueWithHistory<String>("A")
    // Typem letter jest ValueWithHistory<String>
    letter.setValue("B")
    // letter.setValue(123) <- to nie zostanie skompilowane
    val l = letter.currentValue() // typem l jest String
    println(l) // B
    val h = letter.history() // typem h jest List<String>
    println(h) // [A, B]
}
```

Argument typu konstruktora może być wnioskowany. W powyższym przykładzie określiliśmy go jawnie, ale nie musieliśmy tego robić. Ten typ może być wnioskowany na podstawie typu argumentu.

```kotlin
val letter = ValueWithHistory("A")
// Typem letter jest ValueWithHistory<String>
```

Argumenty typu mogą być również wnioskowane z typów zmiennych. Powiedzmy, że chcemy użyć `Any` jako argumentu typu. Możemy to określić, określając typ zmiennej `letter` jako `ValueWithHistory<Any>`.

```kotlin
val letter: ValueWithHistory<Any> = ValueWithHistory("A")
// Typem letter jest ValueWithHistory<Any>
```

Jak wspomniałem we wstępie do tego rozdziału, najważniejszym powodem wprowadzenia generyków było stworzenie kolekcji z określonymi typami elementów. Weźmy pod uwagę klasę `ArrayList` z biblioteki standardowej (stdlib). Jest ona generyczna, więc gdy tworzymy instancję z tej klasy, musimy określić typy elementów. Dzięki temu Kotlin chroni nas, oczekując tylko wartości z akceptowanymi typami, które mają być dodane do listy, jak również typ ten jest określony, gdy zwracamy lub operujemy na elementach listy.

```kotlin
fun main() {
    val letters = ArrayList<String>()
    letters.add("A") // argument musi być typu String
    letters.add("B") // argument musi być typu String
    // Typem letters jest List<String>
    val a = letters[0] // typem a jest String
    println(a) // A
    for (l in letters) { // typem l jest String
        println(l) // najpierw A, potem B
    }
}
```

### Klasy generyczne a nullowalność

Zauważ, że argumenty typu mogą być nullowalne, więc możemy stworzyć `ValueWithHistory<String?>`. W takim przypadku wartość `null` jest zupełnie prawidłową opcją.

```kotlin
fun main() {
    val letter = ValueWithHistory<String?>(null)
    letter.setValue("A")
    letter.setValue(null)
    val l = letter.currentValue() // typem l jest String?
    println(l) // null
    val h = letter.history() // typem h jest List<String?>
    println(h) // [null, A, null]

    val letters = ArrayList<String?>()
    letters.add("A")
    letters.add(null)
    println(letters) // [A, null]
    val l2 = letters[1] // typem l2 jest String?
    println(l2) // null
}
```

Gdy używasz parametrów typu w klasach lub funkcjach, możesz uczynić je nullowalnymi, dodając znak zapytania. Zobacz poniższy przykład. Typ `T` może być, ale nie musi być nullowany, w zależności od argumentu typu, ale typ `T?` zawsze jest nullowany. Możemy przypisać wartość `null` do zmiennych typu `T?`. Nullowanych parametr typu `T?` musi zostać rozpakowany przed użyciem go jako `T`.

```kotlin
class Box<T> {
    var value: T? = null

    fun getOrThrow(): T = value!!
}
```

Można wyrazić również przeciwność. Ponieważ parametr typu może reprezentować typ nullowany, możemy określić zdecydowanie nienullowalną wersję tego typu, dodając `& Any` po parametrze typu. W poniższym przykładzie metoda `orThrow` może być wywoływana dla dowolnej wartości, ale nawet jeśli jej argument typu jest nullowany, zawsze typ zwracany będzie nienullowany.

```kotlin
fun <T> T.orThrow(): T & Any = this ?: throw Error()

fun main() {
    val a: Int? = if (Random.nextBoolean()) 42 else null
    val b = a.orThrow() // Typ b to Int
    val c = b.orThrow() // Typ c to Int
    println(b)
}
```

### Generyczne interfejsy

Interfejsy również mogą być generyczne, co ma podobne konsekwencje jak dla klas: określone parametry typu można użyć wewnątrz ciała interfejsu jako typy właściwości, parametrów i wyników. Dobrym przykładem jest interfejs `List`.

```kotlin
interface List<out E> : Collection<E> {
    override val size: Int
    override fun isEmpty(): Boolean
    override fun contains(element: @UnsafeVariance E): Boolean
    override fun iterator(): Iterator<E>
    override fun containsAll(
        elements: Collection<@UnsafeVariance E>
    ): Boolean
    operator fun get(index: Int): E
    fun indexOf(element: @UnsafeVariance E): Int
    fun lastIndexOf(element: @UnsafeVariance E): Int
    fun listIterator(): ListIterator<E>
    fun listIterator(index: Int): ListIterator<E>
    fun subList(fromIndex: Int, toIndex: Int): List<E>
}
```

> Modyfikator `out` oraz adnotacja `UnsafeVariance` wyjaśniam w książce *Zaawansowany Kotlin*.

![Dla typu `List<String>` metody takie jak `contains` oczekują argumentu typu `String`, a metody takie jak `get` deklarują `String` jako typ wyniku.](list_str_suggestions.png)

![Dla `List<String>`, metody takie jak `filter` mogą wywnioskować `String` jako parametr wyrażenia lambda.](list_suggestions.png)

Generyczne interfejsy mogą być implementowane przez zarówno zwykłe jak i generyczne klasy. Powiedzmy, że mamy klasę `Dog`, która implementuje `Consumer<DogFood>`, jak pokazano w poniższym fragmencie. Interfejs `Consumer` oczekuje metody `consume` z parametrem typu `T`. Oznacza to, że nasz `Dog` musi nadpisać metodę `consume` z argumentem typu `DogFood`. Musi to być `DogFood`, ponieważ implementujemy `Consumer<DogFood>` i typ parametru metody `consume` musi pasować do użytego argumentu typu `DogFood`. Ponieważ `Dog` implementuje `Consumer<DogFood>`, instancja `Dog` może być rzutowana do `Consumer<DogFood>`.

```kotlin
interface Consumer<T> {
    fun consume(value: T)
}
class DogFood

class Dog : Consumer<DogFood> {
    override fun consume(value: DogFood) {
        println("Mlask mlask")
    }
}

fun main() {
    val dog: Dog = Dog()
    val consumer: Consumer<DogFood> = dog
}
```

### Parametry typu i dziedziczenie

Klasy mogą dziedziczyć z otwartych klas generycznych lub implementować generyczne interfejsy; w obu przypadkach muszą wyraźnie określić argument typu. Rozważ poniższy fragment. Klasa `A` dziedziczy z `C<Int>` i implementuje `I<String>`.

```kotlin
open class C<T>
interface I<T>
class A : C<Int>(), I<String>

fun main() {
    val a = A()
    val c: C<Int> = a
    val i: I<String> = a
}
```

Spójrz jeszcze na bardziej praktyczny przykład klasy niegenerycznej, dziedziczącej po klasie generycznej: `MessageListAdapter`, który dziedziczy po `ArrayAdapter<String>`.

```kotlin
class MessageListAdapter(
    context: Context,
    val values: List<ClaimMessage>
) : ArrayAdapter<String>(
    context,
    R.layout.row_messages,
    values.map { it.title }.toTypedArray()
) {

    fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        // ...
    }
}
```

Jeszcze bardziej powszechnym przypadkiem jest, gdy jedna klasa/interfejs generyczny dziedziczy po innej klasie/interfejsie generycznym i używa swojego parametru typu jako argumentu typu klasy, po której dziedziczy. W poniższym fragmencie klasa `A` jest generyczna i używa swojego parametru typu `T` jako argumentu dla zarówno `C`, jak i `I`. Oznacza to, że jeśli utworzysz `A<Int>`, będziesz mógł rzutować ją do `C<Int>` lub `I<Int>`. Jeśli zaś utworzysz `A<String>`, będziesz mógł rzutować ją do `C<String>` lub do `I<String>`.

```kotlin
open class C<T>
interface I<T>
class A<T> : C<T>(), I<T>

fun main() {
    val a: A<Int> = A<Int>()
    val c1: C<Int> = a
    val i1: I<Int> = a

    val a1: A<String> = A<String>()
    val c2: C<String> = a1
    val i2: I<String> = a1
}
```

Dobrym przykładem jest hierarchia kolekcji. Obiekt typu `MutableList<Int>` implementuje `List<Int>`, który implementuje `Collection<Int>`, który implementuje `Iterable<Int>`.

```kotlin
interface Iterable<out T> {
    // ...
}
interface MutableIterable<out T> : Iterable<T> {
    // ...
}
interface Collection<out E> : Iterable<E> {
    /// ...
}
interface MutableCollection<E> : Collection<E>,
    MutableIterable<E> {
    // ...
}
interface List<out E> : Collection<E> {
    // ...
}
interface MutableList<E> : List<E>, MutableCollection<E> {
    // ...
}
```

Jednak klasa nie musi używać swojego parametru typu podczas dziedziczenia z klasy ogólnej lub implementacji ogólnego interfejsu. Parametry typu klas nadrzędnych i podrzędnych są niezależne od siebie i nie należy ich mylić, nawet jeśli mają taką samą nazwę.

```kotlin
open class C<T>
interface I<T>
class A<T> : C<Int>(), I<String>

fun main() {
    val a1: A<Double> = A<Double>()
    val c1: C<Int> = a1
    val i1: I<String> = a1
}
```

### Type erasure

Typy generyczne zostały dodane do Javy dla wygody programistów, ale nigdy nie zostały wbudowane w platformę JVM. Wszystkie argumenty typu są tracone, gdy kompilujemy Kotlin do bajtkodu JVM[^21_1]. W praktyce oznacza to, że `List<String>` staje się `List`, a `emptyList<Double>` staje się `emptyList`. Proces utraty argumentów typu nazywany jest **type erasure**. Z powodu tego procesu parametry typu mają pewne ograniczenia w porównaniu z normalnymi typami. Nie można ich używać dla sprawdzeń `is`; nie można używać ich do określania referencji do klasy[^21_2]; ani nie można używać ich jako argument typu z modyfikatorem `reified`[^21_3].

```kotlin
import kotlin.reflect.typeOf

fun <T> example(a: Any) {
    val check = a is T // BŁĄD
    val ref = T::class // BŁĄD
    val type = typeOf<T>() // BŁĄD
}
```

Jednak Kotlin może pokonać te ograniczenia dzięki użyciu funkcji inline z argumentami typu oznaczonymi modyfikatorem `reified`. Ten temat jest omówiony szczegółowo w rozdziale *Funkcje inline* w książce *Funkcyjny Kotlin*.

```kotlin
import kotlin.reflect.typeOf

inline fun <reified T> example(a: Any) {
    val check = a is T
    val ref = T::class
    val type = typeOf<T>()
}
```

### Ograniczenia generyczne

Ważną funkcjonalnością parametrów typu jest to, że można je ograniczyć, aby były podtypem konkretnego typu. Ustalamy ograniczenie, umieszczając oczekiwany nadtyp po dwukropku. Na przykład, powiedzmy, że implementujesz funkcję `maxOf`, która zwraca największy z jej argumentów. Aby znaleźć największą wartość, argumenty muszą być porównywalne. Więc obok parametru typu `T` możemy określić, że akceptujemy tylko typ będący podtypem `Comparable<T>`.

```kotlin
import java.math.BigDecimal

fun <T : Comparable<T>> maxOf(a: T, b: T): T {
    return if (a >= b) a else b
}

fun main() {
    val m = maxOf(BigDecimal("10.00"), BigDecimal("11.00"))
    println(m) // 11.00

    class A
    maxOf(A(), A()) // Błąd kompilacji,
    // A nie jest Comparable<A>
}
```

Ograniczenia parametrów typu są również używane dla klas generycznych. Rozważ poniższą klasę `ListAdapter`, która oczekuje argumentu typu będącego podtypem `ItemAdaper`.

```kotlin
class ListAdapter<T : ItemAdaper>(/*...*/) { /*...*/ }
```

Ważnym wynikiem posiadania ograniczenia jest to, że instancje tego typu mogą korzystać ze wszystkich metod oferowanych przez określony nadtyp. W ten sposób, gdy `T` jest ograniczone jako podtyp `Iterable<Int>`, wiemy, że możemy iterować po instancjach typu `T`, a elementy zwracane przez iterator będą typu `Int`. Instancje mają typ generyczny `T` ograniczony jako podtyp `Comparable<T>`, wiemy, że te instance możemy ze sobą porównywać. Innym popularnym wyborem dla ograniczenia jest `Any`, co oznacza, że typ może być dowolnym typem nienullowalnym.

```kotlin
fun <T : Iterable<Int>> T.count(): Int {
    var i = 0
    for (elem in this) {
        i++
    }
    return i
}

fun <T : Comparable<T>> maxOf(a: T, b: T): T {
    return if (a >= b) a else b
}

class Box<T: Any>(val value: T)
```

W rzadkich przypadkach możemy potrzebować więcej niż jednego ograniczenia górnego dla typu generycznego. Wtedy możemy użyć `where` po nazwie klasy lub funkcji, aby ustawić dowolną liczbą ograniczeń (oddzielamy je przecinkiem).

```kotlin
interface Animal {
    fun feed()
}
interface GoodTempered {
    fun pet()
}

fun <T> pet(animal: T) where T : Animal, T : GoodTempered {
    animal.pet()
    animal.feed()
}

class Cookie : Animal, GoodTempered {
    override fun pet() {
        // ...
    }
    override fun feed() {
        // ...
    }
}
class Cujo : Animal {
    override fun feed() {
        // ...
    }
}

fun main() {
    pet(Cookie()) // OK
    pet(Cujo()) //BŁĄD KOMPILACJI, Cujo nie jest GoodTempered
}
```

### Star projection

W niektórych przypadkach nie chcemy podać konkretnego argumentu typu. W takich sytuacjach możemy użyć star projection, czyli znaku `*`, który zastępuje argument typu i akceptuje dowolny typ. Istnieją dwie typowe sytuacje, w których jest to przydatne. Pierwsza to sprawdzenie, czy zmienna jest listą. W tym przypadku należy użyć sprawdzenia `is List<*>`. Użycie konkretnego argumentu typu, jak `List<Int>`, byłoby to i tak kompilowane do `List`, ze względu na type erasure. Oznacza to, że lista stringów przeszłaby sprawdzenie `is List<Int>`. Takie sprawdzenie byłoby mylące i jest niedozwolone w Kotlinie. Zamiast tego musisz użyć `is List<*>`. Podobnie z innymi typami generycznymi. 

```kotlin
fun main() {
    val list = listOf("A", "B")
    println(list is List<*>) // true
    println(list is List<Int>) // Błąd kompilacji
}
```

Star projection można również stosować, określając typy zmiennych. Możesz użyć `List<*>`, gdy chcesz wyrazić, że chcesz listy, bez względu na typ jej elementów. Gdy pobierasz elementy z takiej listy, mają one typ `Any?`, który jest nadrzędnym typem wszystkich typów.

```kotlin
fun printSize(list: List<*>) {
    println(list.size)
}

fun printList(list: List<*>) {
    for (e in list) { // typ e to Any?
        println(e)
    }
}
```

Star projection nie należy mylić z argumentem typu `Any?`. Aby zrozumieć różnicę, porównajmy `MutableList<Any?>` i `MutableList<*>`. Oba te typy deklarują `Any?` jako generyczny typ wyników z metod. Jednak, gdy dodawane są elementy, `MutableList<Any?>` akceptuje wszystko (`Any?`), ale `MutableList<*>` akceptuje `Nothing`, więc nie akceptuje żadnych wartości.

```kotlin
fun main() {
    val l1: MutableList<Any?> = mutableListOf("A")
    val r1 = l1.first() // typ r1 to Any?
    l1.add("B") // oczekiwany typ argumentu to Any?

    val l2: MutableList<*> = mutableListOf("A")
    val r2 = l2.first() // typ r2 to Any?
    l2.add("B") // BŁĄD,
    // oczekiwany typ argumentu to Nothing,
    // więc nie ma wartości, która mogłaby być użyta jako argument
}
```

Typ określony przez star projection jest interpretowany jako `Any?` we wszystkich pozycjach wyjściowych (typy wyników) oraz jako `Nothing` we wszystkich pozycjach wejściowych (typy parametrów).

### Podsumowanie

Dla wielu programistów generyki wydają się tak trudne i przerażające, ale tak naprawdę są dość proste i intuicyjne. Możemy uczynić element generycznym, poprzez dodanie parametrów typu. Takie parametry typu mogą być używane wewnątrz tego elementu. Ten mechanizm pozwala na uogólnienie algorytmów i klas, tak aby mogły być używane z różnymi typami. Dobrze jest rozumieć, jak działają generyki, dlatego ten rozdział przedstawił prawie wszystkie ich kluczowe aspekty. Jednakże są jeszcze inne, takie jak modyfikatory wariancji (`out` i `in`), ale do nich wrócimy w książce *Zaawansowany Kotlin*.

[^21_1]: Używam JVM jako odniesienia, ponieważ jest to najbardziej popularny target dla Kotlina, ale także dlatego, że był pierwszym targetem, więc wiele mechanizmów Kotlina zostało zaprojektowanych pod niego. Jednakże, jeśli chodzi o brak wsparcia dla argumentów typu na niskim poziomie, inne platformy nie są lepsze. Na przykład JavaScript nie obsługuje w ogóle typów generycznych.
[^21_2]: Odwołania do klasy i typu są wyjaśnione w książce *Zaawansowany Kotlin*.
[^21_3]: Modyfikator `reified` jest wyjaśniony w książce *Funkcyjny Kotlin*.
