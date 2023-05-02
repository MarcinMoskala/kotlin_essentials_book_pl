## Piękno systemu typów w Kotlinie

System typów Kotlin jest niesamowicie zaprojektowany. Wiele funkcji, które wyglądają jak przypadki specjalne, są po prostu naturalnym następstwem tego, jak zaprojektowany jest system typów. Na przykład, dzięki systemowi typów, w poniższym przykładzie typ `surname` to `String`, typ `age` to `Int`, a my możemy użyć `return` i `throw` po prawej stronie operatora Elvisa.

```kotlin
fun processPerson(person: Person?) {
    val name = person?.name ?: "unknown"

    val surname = person?.surname ?: return

    val age = person?.age
        ?: throw Error("Person must have age")

    // ...
}
```

System typów daje nam również bardzo wygodne wsparcie dla nullowalności, inteligentne wnioskowanie typów i wiele więcej. W tym rozdziale odkryjemy wiele magii Kotlin. Zawsze uwielbiam mówić o tym na moich warsztatach, ponieważ widzę oszałamiające piękno tego, jak system typów Kotlin jest tak dobrze zaprojektowany, że wszystkie te elementy doskonale się ze sobą łączą i dają nam wspaniałe doświadczenie z programowania. Uważam ten temat za fascynujący, ale postaram się również dodać kilka użytecznych wskazówek, które pokazują, gdzie ta wiedza może być przydatna w praktyce. Mam nadzieję, że odkrywanie tego sprawi ci tyle samo przyjemności, co mnie.

### Czym jest typ?

Zanim zaczniemy rozmawiać o systemie typów, powinniśmy najpierw wyjaśnić, czym jest typ. Czy znasz odpowiedź? Pomyśl o tym przez chwilę.

Typy są często mylone z klasami, ale te dwa terminy reprezentują zupełnie różne koncepcje. Spójrz na poniższy przykład. Możesz zobaczyć `User` użyte cztery razy. Czy możesz mi powiedzieć, które zastosowania to klasy, które to typy, a które coś innego?

![](typing_system_type_vs_class_question.png)

Po słowie kluczowym `class` definiujesz nazwę klasy. Klasa to szablon dla obiektów, który definiuje zestaw właściwości i metod. Gdy wywołujemy konstruktor, tworzymy obiekt. Typy są używane tutaj, aby określić, jakiego rodzaju obiektów oczekujemy w zmiennych[^20_1].

![](typing_system_type_vs_class.png)

### Dlaczego mamy typy?

Przeprowadźmy na chwilę eksperyment myślowy. Kotlin to język o statycznej typizacji, więc wszystkie zmienne i funkcje muszą być typowane. Jeśli nie określimy ich typów jawnie, zostaną one wywnioskowane. Ale cofnijmy się na chwilę i wyobraźmy sobie, że jesteś projektantem języka, który decyduje, jak powinien wyglądać Kotlin. Możemy porzucić wszystkie te wymagania i całkowicie wyeliminować typy. Kompilator tak naprawdę ich nie potrzebuje[^20_2]. Ma klasy, które definiują, jak powinny być tworzone obiekty, oraz obiekty, które są używane podczas wykonywania. Co tracimy, jeśli pozbędziemy się typów? Głównie bezpieczeństwo i wygodę dla programistów.

![](typing_system_type_vs_class_crossed.png)

Warto wspomnieć, że wiele języków obsługuje klasy i obiekty, ale nie typy. Wśród nich są JavaScript[^20_6] i (do niedawna) Python - dwie z najpopularniejszych na świecie języków[^20_3]. Jednak typy przynoszą nam wartość, dlatego w społeczności JavaScript coraz więcej osób używa TypeScript (który to jest w zasadzie JavaScript plus typy), a Python wprowadził wsparcie dla typów.

Więc dlaczego mamy typy? Są one głównie dla nas, programistów. Typ mówi nam, jakie metody lub właściwości możemy używać na obiekcie. Typ mówi nam, jaki rodzaj wartości może być użyty jako argument. Typy uniemożliwiają używanie nieprawidłowych obiektów, metod lub właściwości. Dają nam bezpieczeństwo, a sugestie są dostarczane przez środowisko IDE. Kompilator również korzysta z typów, gdyż są one wykorzystywane do lepszego optymalizowania naszego kodu lub do decydowania, która funkcja powinna zostać wybrana, gdy jej nazwa jest przeciążona. Mimo to, to właśnie programiści są najważniejszymi beneficjentami typów.

Więc czym jest typ? **Można go rozważyć jako zbiór rzeczy, które możemy zrobić z obiektem**. Zwykle jest to zbiór metod i właściwości.

### Relacja między klasami a typami

Mówimy, że klasy generują typy. Pomyśl o klasie `User`. Generuje ona dwa typy. Czy możesz je oba wymienić? Jeden to `User`, ale drugi to nie `Any` (`Any` jest już w hierarchii typów). Drugim nowym typem generowanym przez klasę `User` jest `User?`. Tak, nullowanych wariant to oddzielny typ.

Istnieją klasy, które generują znacznie więcej typów: klasy ogólne. Klasa `Box<T>` teoretycznie generuje nieskończoną liczbę typów.

![](typing_system_type_class_relation.png)

### Klasa kontra typ w praktyce

Dyskusja ta może brzmieć bardzo teoretycznie, ale ma już pewne praktyczne implikacje. Zauważ, że klasy nie mogą być nullowanych, ale typy już tak. Weź pod uwagę początkowy przykład, gdzie poprosiłem Cię, abyś wskazał, gdzie `User` jest typem. Tylko na pozycjach reprezentujących typy można użyć `User?` zamiast `User`.

![](typing_system_type_vs_class_nullowanych.png)

Funkcje składowe są zdefiniowane dla klas, więc ich odbiorca nie może być nullowanych ani mieć argumentów typu[^20_4]. Funkcje rozszerzeń są zdefiniowane dla typów, więc mogą być nullowanych lub zdefiniowane dla konkretnego typu ogólnego. Weź pod uwagę funkcję `sum`, która jest rozszerzeniem `Iterable<Int>`, lub funkcję `isNullOrBlank`, która jest rozszerzeniem `String?`.

```kotlin
fun Iterable<Int>.sum(): Int {
    var sum: Int = 0
    for (element in this) {
        sum += element
    }
    return sum
}

@OptIn(ExperimentalContracts::class)
inline fun CharSequence?.isNullOrBlank(): Boolean {
    contract {
        returns(false) implies (this@isNullOrBlank != null)
    }

    return this == null || this.isBlank()
}
```

### Relacja między typami

Przyjmijmy, że mamy klasę `Dog` i jej nadklasę `Animal`.

```kotlin
open class Animal
class Dog : Animal()
```

Gdziekolwiek oczekiwany jest typ `Animal`, możesz użyć `Dog`, ale nie na odwrót.

```kotlin
fun petAnimal(animal: Animal) {}
fun petDog(dog: Dog) {}

fun main() {
    val dog: Dog = Dog()
    val dogAnimal: Animal = dog // działa
    petAnimal(dog) // działa
    val animal: Animal = Animal()
    val animalDog: Dog = animal // błąd kompilacji
    petDog(animal) // błąd kompilacji
}
```

Dlaczego? Ponieważ istnieje konkretna relacja między tymi typami: `Dog` jest podtypem `Animal`. Zgodnie z regułą, gdy A jest podtypem B, możemy użyć A tam, gdzie oczekiwane jest B. Możemy również powiedzieć, że `Animal` jest nadtypem `Dog`, a podtyp może być użyty tam, gdzie oczekiwany jest nadtyp.

{width: 25%, align: middle}
![](typing_system_A_B.png)

Istnieje również relacja między typami nullowanych i non-nullowanych. Non-nullowanych może być używane wszędzie tam, gdzie oczekiwane jest nullowanych.

```kotlin
fun petDogIfPresent(dog: Dog?) {}
fun petDog(dog: Dog) {}

fun main() {
    val dog: Dog = Dog()
    val dogNullable: Dog? = dog
    petDogIfPresent(dog) // działa
    petDogIfPresent(dogNullable) // działa
    petDog(dog) // działa
    petDog(dogNullable) // błąd kompilacji
}
```

Dzieje się tak, ponieważ wariant non-nullowanych każdego typu jest podtypem wariantu nullowanych.

{width: 50%, align: middle}
![](typing_system_A_B_nullability.png)

Nadklasą wszystkich klas w Kotlinie jest `Any`, co jest podobne do `Object` w Javie. Nadtypem wszystkich typów nie jest `Any`, ale `Any?`. `Any` jest nadtypem wszystkich typów non-nullowanych. Mamy też coś, czego nie ma w Javie i większości innych głównych języków: podtyp wszystkich typów, który nazywa się `Nothing`. Wkrótce o nim porozmawiamy.

{width: 50%, align: middle}
![](typing_system_A_B_nullability_Any_Nothing.png)

`Any` jest tylko nadtypem typów non-nullowanych. Więc tam, gdzie oczekiwany jest `Any`, typy nullowanych nie będą akceptowane. Ten fakt jest również wykorzystywany do ustawienia górnego ograniczenia parametru typu, aby akceptować tylko typy non-nullowanych[^20_5].

```kotlin
fun <T : Any> String.parseJson(): T = ...
```

`Unit` nie ma żadnego specjalnego miejsca w hierarchii typów. To po prostu deklaracja obiektu, która jest używana, gdy funkcja nie określa typu wyniku.

```kotlin
object Unit {
    override fun toString() = "kotlin.Unit"
}
```

Porozmawiajmy o koncepcji, która ma bardzo specjalne miejsce w hierarchii typów: porozmawiajmy o `Nothing`.

### Podtyp wszystkich typów: Nothing

`Nothing` jest podtypem wszystkich typów w Kotlinie. Gdybyśmy mieli instancję tego typu, mogłaby być użyta zamiast wszystkiego innego (jak Joker w grze karcianej Rummy). Nic dziwnego, że taka instancja nie istnieje. `Nothing` jest pustym typem (znanym również jako dół typów, zerowy typ, niezamieszkały typ lub nigdy nie występujący typ), co oznacza, że nie ma żadnych wartości. Dosłownie niemożliwe jest stworzenie instancji typu `Nothing`, ale ten typ jest naprawdę bardzo użyteczny. Powiem więcej: niektóre funkcje deklarują `Nothing` jako swój typ wyniku. Prawdopodobnie używałeś takich funkcji już wiele razy. Jakie to są funkcje? Deklarują `Nothing` jako typ wyniku, ale nie mogą go zwrócić, ponieważ ten typ nie ma instancji. Ale co mogą zrobić te funkcje? Trzy rzeczy: muszą działać wiecznie, zakończyć program lub rzucić wyjątek. We wszystkich przypadkach nigdy nie zwracają, więc typ `Nothing` nie tylko jest prawidłowy, ale też naprawdę użyteczny.

```kotlin
fun runForever(): Nothing {
    while (true) {
        // no-op
    }
}

fun endProgram(): Nothing {
    exitProcess(0)
}

fun fail(): Nothing {
    throw Error("Some error")
}
```

Nigdy nie znalazłem dobrego przypadku użycia dla funkcji, która działa wiecznie, a zakończenie programu nie jest bardzo powszechne, ale często używamy funkcji, które rzucają wyjątki. Kto nie używał nigdy `TODO()`? Ta funkcja rzuca wyjątek `NotImplementedError`. Istnieje również funkcja `error` z biblioteki standardowej, która rzuca `IllegalStateException`.

```kotlin
inline fun TODO(): Nothing = throw NotImplementedError()

inline fun error(message: Any): Nothing =
    throw IllegalStateException(message.toString())
```

`TODO` jest używany jako symbol zastępczy w miejscu, gdzie planujemy zaimplementować jakiś kod.

```kotlin
fun fib(n: Int): Int = TODO()
```

`error` jest używany do sygnalizowania sytuacji niedozwolonej:

```kotlin
fun get(): T = when {
    left != null -> left
    right != null -> right
    else -> error("Muszą być albo left, albo right")
}
```

Ten typ wyniku jest istotny. Powiedzmy, że masz warunek if, który zwraca albo `Int`, albo `Nothing`. Jaki powinien być wnioskowany typ? Najbliższy nadtyp zarówno dla `Int`, jak i `Nothing` to `Int`. Dlatego wnioskowany typ będzie `Int`.

```kotlin
// wnioskowany typ odpowiedzi to Int
val answer = if (timeHasPassed) 42 else TODO()
```

Ta sama zasada obowiązuje, gdy używamy operatora Elvisa, wyrażenia when itp. W poniższym przykładzie typ zarówno `name`, jak i `fullName` to `String`, ponieważ zarówno `fail`, jak i `error` deklarują `Nothing` jako swój typ wyniku. To ogromne udogodnienie.

```kotlin
fun processPerson(person: Person?) {
    // wnioskowany typ name to String
    val name = person?.name ?: fail()
    // wnioskowany typ fullName to String
    val fullName = when {
        !person.middleName.isNullOrBlank() ->
            "$name ${person.middleName} ${person.surname}"
        !person.surname.isNullOrBlank() ->
            "$name ${person.surname}"
        else ->
            error("Osoba musi mieć nazwisko")
    }
    // ...
}
```

### Typ wyniku z return i throw

Zacznę ten podrozdział od czegoś dziwnego: czy wiedziałeś, że możesz umieścić `return` lub `throw` po prawej stronie przypisania zmiennej?

```kotlin
fun main() {
    val a = return
    val b = throw Error()
}
```

To nie ma sensu, ponieważ zarówno `return`, jak i `throw` kończą funkcję, więc nigdy nie przypiszemy niczego do takich zmiennych (jak `a` i `b` w powyższym przykładzie). To przypisanie to nieosiągalny fragment kodu. W Kotlinie powoduje to tylko ostrzeżenie.

![](typing_system_return_return_type.png)

Kod powyżej jest poprawny z punktu widzenia języka, ponieważ zarówno `return`, jak i `throw` są wyrażeniami, co oznacza, że deklarują one typ wyniku. Tym typem jest `Nothing`.

```kotlin
fun main() {
    val a: Nothing = return
    val b: Nothing = throw Error()
}
```

To wyjaśnia, dlaczego możemy umieścić `return` lub `throw` po prawej stronie operatora Elvisa lub w wyrażeniu when.

```kotlin
fun processPerson(person: Person?) {
    val name = person?.name ?: return
    val fullName = when {
        !person.middleName.isNullOrBlank() ->
            "$name ${person.middleName} ${person.surname}"
        !person.surname.isNullOrBlank() ->
            "$name ${person.surname}"
        else -> return
    }
    // ...
}
```

```kotlin
fun processPerson(person: Person?) {
    val name = person?.name ?: throw Error("Imię jest wymagane")
    val fullName = when {
        !person.middleName.isNullOrBlank() ->
            "$name ${person.middleName} ${person.surname}"
        !person.surname.isNullOrBlank() ->
            "$name ${person.surname}"
        else -> throw Error("Nazwisko jest wymagane")
    }
    // ...
}
```

Zarówno `return`, jak i `throw` deklarują `Nothing` jako swój typ wyniku. W konsekwencji, Kotlin wywnioskuje `String` jako typ zarówno `name`, jak i `fullName`, ponieważ `String` jest najbliższym supertypem zarówno `String`, jak i `Nothing`.

Więc teraz możesz powiedzieć, że znasz `Nothing`. Tak jak John Snow.

![](Nothing_John_Snow.png)

### Kiedy jakiś kod jest nieosiągalny?

Gdy element deklaruje `Nothing` jako typ zwracany, oznacza to, że wszystko po jego wywołaniu jest nieosiągalne. To jest uzasadnione: nie ma żadnych instancji `Nothing`, więc nie można ich zwrócić. Oznacza to, że instrukcja, która deklaruje `Nothing` jako swój typ wyniku, nigdy nie zakończy się w normalny sposób, więc kolejne instrukcje są nieosiągalne. Dlatego wszystko po `fail` lub `throw` będzie nieosiągalne.

{width: 60%}
![](typing_system_fail_throw.png)

Podobnie jest z `return`, `TODO`, `error`, itp. Jeśli nieopcjonalne wyrażenie deklaruje `Nothing` jako swój typ wyniku, wszystko po tym jest nieosiągalne. To prosta zasada, ale przydatna dla kompilatora. Jest również przydatna dla nas, ponieważ daje nam więcej możliwości. Dzięki tej zasadzie możemy użyć `TODO()` w funkcji zamiast zwracać wartość. Wszystko, co deklaruje `Nothing` jako typ wyniku, kończy funkcję (lub działa w nieskończoność), więc ta funkcja nie zakończy się bez wcześniejszego zwrócenia lub rzucenia wyjątku.

```kotlin
fun fizzBuzz(): String {
    TODO()
}
```

Chciałbym zakończyć ten temat bardziej zaawansowanym przykładem, który pochodzi z biblioteki Kotlin Coroutines. Istnieje klasa `MutableStateFlow`, która reprezentuje zmienną wartość, której zmiany stanu można obserwować za pomocą metody `collect`. Chodzi o to, że `collect` zawiesza bieżącą korutynę, dopóki to, co obserwuje, nie zostanie zamknięte, ale StateFlow nie może zostać zamknięty. Dlatego ta funkcja `collect` deklaruje `Nothing` jako swój typ wyniku.

```kotlin
public interface SharedFlow<out T> : Flow<T> {
    public val replayCache: List<T>
    override suspend fun collect(
        collector: FlowCollector<T>
    ): Nothing
}
```

Jest to bardzo przydatne dla programistów, którzy nie wiedzą, jak działa `collect`. Dzięki typowi wynikowemu IntelliJ informuje ich, że kod, który umieszczają po `collect`, jest nieosiągalny.

![SharedFlow nie może być zamknięty, więc jego funkcja `collect` nigdy nie zwróci wartości, dlatego deklaruje `Nothing` jako swój typ wyniku.](stateflow_unreachable.png)

### Typ null

Przyjrzyjmy się kolejnej osobliwości. Czy wiedziałeś, że możesz przypisać wartość `null` do zmiennej bez ustawiania jawnego typu? Co więcej, taką zmienną można użyć wszędzie tam, gdzie akceptowane jest `null`.

```kotlin
fun main() {
    val n = null
    val i: Int? = n
    val d: Double? = n
    val str: String? = n
}
```

Oznacza to, że `null` ma swój typ, który jest podtypem wszystkich typów nullowanych. Spójrz na hierarchię typów i zgadnij, jaki to typ.

{width: 50%, align: middle}
![](typing_system_A_B_nullability_Any_Nothing.png)

Mam nadzieję, że odgadłeś, że typem `null` jest `Nothing?`. Teraz zastanów się nad wywnioskowanym typem `a` i `b` w poniższym przykładzie.

```kotlin
val a = if (predicate) "A" else null

val b = when {
    predicate2 -> "B"
    predicate3 -> "C"
    else -> null
}
```

W wyrażeniu if szukamy najbliższego supertypu typów z obu gałęzi. Najbliższym supertypem `String` i `Nothing?` jest `String?`. To samo dotyczy wyrażenia when: najbliższym supertypem `String`, `String` i `Nothing?` jest `String?`. Wszystko ma sens.

Z tego samego powodu, kiedy wymagamy `String?`, możemy przekazać zarówno `String`, jak i `null`, którego typem jest `Nothing?`. Jest to jasne, gdy spojrzysz na hierarchię typów. `String` i `Nothing?` są jedynymi niepustymi podtypami `String?`.

![](Socrates.png)

{pagebreak}

### Podsumowanie

W tym rozdziale nauczyliśmy się:
* Klasa jest szablonem do tworzenia obiektów. Typ definiuje oczekiwania i funkcjonalności.
* Każda klasa generuje typ nullowanych i typ non-nullowanych.
* Typ nullowanych jest supertypem dla nieoznaczonej wersji tego typu.
* Supertypem wszystkich typów jest `Any?`.
* Supertypem typów non-nullowanych jest `Any`.
* Podtypem wszystkich typów jest `Nothing`.
* Gdy funkcja deklaruje `Nothing` jako typ zwracany, oznacza to, że zgłosi błąd lub będzie działać w nieskończoność.
* Zarówno `throw`, jak i `return` deklarują `Nothing` jako swój typ wyniku.
* Kompilator Kotlin rozumie, że gdy wyrażenie deklaruje `Nothing` jako typ wyniku, wszystko później jest nieosiągalne.
* Typem `null` jest `Nothing?`, który jest podtypem wszystkich typów nullowanych.

W następnym rozdziale omówimy generyki i zobaczymy, jak są ważne dla naszego systemu typów.

[^20_1]: Parametry są również zmiennymi.
[^20_2]: Z wyjątkiem ustalania, którą funkcję wybrać w przypadku przeciążenia.
[^20_3]: Wszystko zależy od tego, co mierzymy, ale Python, Java i JavaScript zajmują pierwsze trzy miejsca w większości rankingów. W niektórych są pokonane przez język C, który jest szeroko stosowany w bardzo niskopoziomowym rozwoju, jak rozwój procesorów dla samochodów czy lodówek.
[^20_4]: Argumenty typów i parametry typów będą lepiej wyjaśnione w rozdziale *Generyki*.
[^20_5]: Wyjaśnię górne granice parametrów typów w rozdziale *Generyki*.
[^20_6]: Formalnie JavaScript obsługuje słabe typowanie, ale w tym rozdziale omawiamy statyczne typowanie, którego JavaScript nie obsługuje. 
