## Funkcje

Kiedy Andrey Breslav, pierwotny twórca Kotlina, został zapytany o swoją ulubioną funkcjonalność tego języka, odpowiedział bez wahania: funkcje[^06_1]. W końcu to funkcje są najważniejszym elementem naszych programów. Spójrz na dowolną aplikację, a przekonasz się, że większość kodu albo definiuje, albo wywołuje funkcje.

![Przykładem jest losowa klasa z projektu APKUpdater. Zauważ, że niemal każdy wiersz albo definiuje, albo wywołuje jakąś funkcję.](06_example_app.png)

W Kotlinie definiujemy funkcje za pomocą słowa kluczowego `fun`. Dlatego mamy tak dużo zabawy w Kotlinie. Z odrobiną kreatywności funkcja może składać się tylko z `fun`:

```kotlin
fun <Fun> `fun`(`fun`: Fun): Fun = `fun`
```

> Jest to tzw. *funkcja tożsamościowa*, czyli funkcja zwracająca swój argument bez żadnych modyfikacji. Posiada ona generyczny parametr typu `Fun`. Parametry generyczne zostaną wyjaśnione w rozdziale *Typy generyczne*.

Zgodnie z konwencją funkcje nazywamy, używając notacji lowerCamelCase[^06_2]. Możemy używać znaków, podkreślnika `_` oraz cyfr (ale nie na pierwszej pozycji), jednak zazwyczaj używamy tylko znaków.

{width: 50%}
![W Kotlin funkcje nazywamy, używając notacji lowerCamelCase.](camelCase.png)

Tak wygląda przykładowa funkcja oraz jej wywołanie w Kotlinie:

```kotlin
fun square(x: Double): Double {
    return x * x
}

fun main() {
    println(square(10.0)) // 100.0
}
```

Zauważ, że typ parametru jest określony po nazwie zmiennej i dwukropku, a typ wyniku jest określony po dwukropku za nawiasem a parametrami. Taka notacja jest typowa dla języków z silnym wsparciem dla wnioskowania o typach, ponieważ gdy jest używana, to łatwiej jest dodawać lub usuwać definicje typów.

```kotlin
val a: Int = 123
// łatwo przekształcić z lub do
val a = 123

fun add(a: Int, b: Int): Int = a + b
// łatwo przekształcić z lub do
fun add(a: Int, b: Int) = a + b
```

Aby użyć zarezerwowanego słowa kluczowego jako nazwy funkcji (takiego jak `fun` czy `when`), użyjemy odwróconego apostrofu (znaków backtick), jak w przykładzie poniżej. Kiedy funkcja ma niedozwoloną nazwę, zarówno jej definicja, jak i wywołania wymagają odwróconego apostrofu.

Gdy piszemy testy jednostkowe, często nazywamy ich funkcje przy użyciu pełnych zdań i spacji, co jest możliwe właśnie dzięki użyciu odwróconego apostrofu.

```kotlin
class CartViewModelTests {
    @Test
    fun `should show error dialog when no items loaded`() {
        ...
    }
}
```

### Funkcje z pojedynczym wyrażeniem

Wiele funkcji w rzeczywistych projektach ma tylko jedno wyrażenie[^06_3], więc używają `return` już w pierwszej linii. Funkcja `square` zdefiniowana powyżej to świetny przykład. W przypadku takich funkcji, zamiast definiować ciało za pomocą nawiasów klamrowych, możemy użyć znaku równości (`=`) i wyrażenia obliczającego wynik (bez używania `return`). Takie funkcje będziemy nazywali *funkcjami z pojedynczym wyrażeniem*.

```kotlin
fun square(x: Double): Double = x * x

fun main() {
    println(square(10.0)) // 100.0
}
```

Wyrażenie może być bardziej skomplikowane i zajmować wiele linii. Jest to dopuszczalne, o ile pozostają pojedynczym wyrażeniem.

```kotlin
fun findUsers(userFilter: UserFilter): List<User> =
    userRepository
        .getUsers()
        .map { it.toDomain() }
        .filter { userFilter.accepts(it) }
```

Kiedy używamy składni funkcji z pojedynczym wyrażeniem, nie musimy określać typu zwracanego, gdyż może zostać on automatycznie wywnioskowany na podstawie typu zwracanego przez wyrażenie. Wciąż jednak możemy określić ten typ, ponieważ często jest on przydatny dla bezpieczeństwa i czytelności kodu[^06_4].

```kotlin
fun square(x: Double) = x * x

fun main() {
    println(square(10.0)) // 100.0
}
```

### Funkcje na wszystkich poziomach

Kotlin pozwala nam definiować funkcje na różnych poziomach, co nie jest oczywiste; przykładowo Java pozwala wyłącznie na definiowanie funkcji w klasach. W Kotlinie możemy zdefiniować:

* funkcje w plikach, poza jakąkolwiek klasą, nazywane **funkcjami pliku**[^06_5],
* funkcje wewnątrz klas lub obiektów, które nazywać będziemy **funkcjami klasy**[^06_6] (wszystkie funkcje klasy są również **metodami**),
* funkcje wewnątrz funkcji, nazywane **funkcjami lokalnymi** lub **funkcjami zagnieżdżonymi**.

```kotlin
// Funkcja pliku
fun double(i: Int) = i * 2

class A {
    // Funkcja klasy (metoda)
    private fun triple(i: Int) = i * 3

    // Funkcja klasy (metoda)
    fun twelveTimes(i: Int): Int {
        // Funkcja lokalna
        fun fourTimes() = double(double(i))
        return triple(fourTimes())
    }
}

// Funkcja pliku
fun main(args: Array<String>) {
    double(1) // 2
    A().twelveTimes(2) // 24
}
```

Funkcje plików często są używane do definiowania funkcji pomocniczych, czyli uniwersalnych funkcji pomagających przy programowaniu. Funkcje plików można łatwo przenosić i rozdzielać między plikami. Pod wieloma względami funkcje plików są lepsze niż statyczne funkcje w Javie. Korzystanie z nich wydaje się intuicyjne i wygodne dla programistów.

W przypadku funkcji lokalnych (zdefiniowanych wewnątrz funkcji) historia wygląda nieco inaczej. Często widzę, że programistom brakuje wyobraźni, jak z nich korzystać. Funkcje lokalne są popularne w JavaScript i Pythonie, ale nie są wspierane w Javie. Cechą funkcji lokalnych jest to, że mają one dostęp do zmiennych lokalnych (zdefiniowanych powyżej nich). Dlatego funkcje lokalne są używane do wyodrębniania powtarzającego się kodu wewnątrz funkcji, który operuje na zmiennych lokalnych. Kiedy piszemy dłuższe funkcje (zwłaszcza testy), powinny one opowiadać pewną "historię". Funkcje lokalne bardzo potrafią w tym pomóc, nie tylko poprzez wyodrębnianie powtarzającego się kodu, ale także poprzez wyrażanie intencji w kodzie.

Spójrz na poniższy przykład, który przedstawia funkcję walidującą formularz. Sprawdza ona warunki dla pól formularza. Jeśli jakiś warunek nie jest spełniony, powinniśmy wyświetlić błąd i zmienić lokalną zmienną `isValid` na `false`, w takim przypadku nie powinniśmy jednak kończyć funkcji, ponieważ chcemy sprawdzić wszystkie pola (nie powinniśmy zatrzymywać się na pierwszym, które nie jest poprawne). To jest przykład, gdzie funkcja lokalna może nam pomóc wyodrębnić powtarzające się zachowanie.

```kotlin
fun validateForm() {
    var isValid = true
    val errors = mutableListOf<String>()
    fun addError(view: FormView, error: String) {
        view.error = error
        errors += error
        isValid = false
    }

    val email = emailView.text
    if (email.isBlank()) {
        addError(emailView, "Email cannot be empty or blank")
    }

    val pass = passView.text.trim()
    if (pass.length < 3) {
        addError(passView, "Password too short")
    }

    if (isValid) {
        tryLogin(email, pass)
    } else {
        showErrors(errors)
    }
}
```

### Parametry i argumenty

Zmienna zdefiniowana jako część definicji funkcji jest jej **parametrem**. Wartość przekazywana podczas wywoływania funkcji nazywana jest **argumentem**. Wartość (argument) w czasie wywołania funkcji jest przypisywana do zmiennej (parametru), dzięki czemu mamy do niej dostęp w ciele funkcji.

```kotlin
fun square(x: Double) = x * x // x to parametr

fun main() {
    println(square(10.0)) // 10.0 to argument
    println(square(0.0)) // 0.0 to argument
}
```

W Kotlinie parametry są tylko do odczytu, więc nie można im przypisać nowej wartości.

```kotlin
fun a(i: Int) {
    i = i + 10 // BŁĄD KOMPILACJI
    // ...
}
```

Jeśli koniecznie chcesz zmodyfikować zmienną parametru, jedynym sposobem jest zastąpienie jej lokalną zmienną, która będzie również do zapisu.

```kotlin
fun a(i: Int) {
    var i = i + 10
    // ...
}
```

To możliwe, ale niezalecane. Parametr przechowuje wartość używaną jako argument, a ta wartość nie powinna ulegać zmianie. Lokalna zmienna do odczytu i zapisu reprezentuje inny koncept i powinna mieć inną nazwę.

### Typ zwracany `Unit`

W Kotlinie wszystkie funkcje mają jakiś typ zwracany, więc każde wywołanie funkcji jest wyrażeniem. Gdy typ nie jest określony, domyślnym typem wyniku jest `Unit`, a domyślną wartością wyniku jest obiekt `Unit`.

```kotlin
fun someFunction() {}

fun main() {
    val res: Unit = someFunction()
    println(res) // kotlin.Unit
}
```

`Unit` to po prostu bardzo prosty obiekt, który jest używany jako zastępcza wartość, gdy nic innego nie jest zwracane. Gdy określasz funkcję bez określonego typu zwracanego, ten typ będzie niejawnie określony jako `Unit`. Gdy definiujesz funkcję bez `return` w ostatniej linii, jest to równoznaczne z użyciem `return` bez wartości. Użycie `return` bez wartości to to samo, co zwrócenie wartości `Unit`.

```kotlin
fun a() {}

// to samo co
fun a(): Unit {}

// to samo co
fun a(): Unit {
    return
}

// to samo co
fun a(): Unit {
    return Unit
}
```

Oczywiście, `Unit` nie jest tak naprawdę zwracany, gdy nie jest potrzebny, więc wydajność kodu nie jest zagrożona.

### Parametry vararg

Każdy parametr oczekuje jednego argumentu, z wyjątkiem parametrów oznaczonych modyfikatorem `vararg`. Takie parametry akceptują dowolną liczbę argumentów.

```kotlin
fun a(vararg params: Int) {}

fun main() {
    a()
    a(1)
    a(1, 2)
    a(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
}
```

Dobrym przykładem takiej funkcji jest `listOf`, która tworzy listę z wartości użytych jako argumenty.

```kotlin
fun main() {
    println(listOf(1, 3, 5, 6)) // [1, 3, 5, 6]
    println(listOf("A", "B", "C")) // [A, B, C]
}
```

Oznacza to, że parametr `vararg` przechowuje kolekcję wartości, dlatego nie może mieć typu pojedynczego obiektu. Parametr `vararg` reprezentuje tablicę zadeklarowanego typu, a my możemy iterować po tej tablicy za pomocą pętli `for` (co zostanie wyjaśnione bardziej szczegółowo w następnym rozdziale).

```kotlin
fun concatenate(vararg strings: String): String {
    // Typem `strings` jest Array<String>
    var accumulator = ""
    for (s in strings) accumulator += s
    return accumulator
}

fun sum(vararg ints: Int): Int {
    // Typem `ints` jest IntArray
    var accumulator = 0
    for (i in ints) accumulator += i
    return accumulator
}

fun main() {
    println(concatenate()) //
    println(concatenate("A", "B")) // AB
    println(sum()) // 0
    println(sum(1, 2, 3)) // 6
}
```

Wrócimy do parametrów `vararg` w rozdziale *Kolekcje*, w sekcji poświęconej tablicom.

### Nazwane argumenty i domyślne wartości

Deklarując funkcje, często określamy opcjonalne parametry. Dobrym przykładem jest `joinToString`, która przekształca obiekt iterowalny w `String`. Można go używać bez żadnych argumentów lub zmieniać jego zachowanie za pomocą argumentów.

```kotlin
fun main() {
    val list = listOf(1, 2, 3, 4)
    println(list.joinToString()) // 1, 2, 3, 4
    println(list.joinToString(separator = "-")) //  1-2-3-4
    println(list.joinToString(limit = 2)) //  1, 2, ...
}
```

W Kotlinie wiele funkcji używa opcjonalnej parametryzacji, ale jak to zrobić? Wystarczy umieścić znak równości po parametrze, a następnie określić wartość domyślną.

```kotlin
fun cheer(how: String = "Hello,", who: String = "World") {
    println("$how $who")
}

fun main() {
    cheer() // Hello, World
    cheer("Hi") // Hi World
}
```

Wartości określone w ten sposób są tworzone na żądanie, gdy nie ma parametru dla ich pozycji. To nie Python, więc nie są przechowywane statycznie, dlatego bezpiecznie można używać zmiennych wartości jako argumentów domyślnych.

```kotlin
fun addOneAndPrint(list: MutableList<Int> = mutableListOf()){
    list.add(1)
    println(list)
}

fun main() {
    addOneAndPrint() // [1]
    addOneAndPrint() // [1]
    addOneAndPrint() // [1]
}
```

> W Python analogiczny kod wygenerowałby `[1]`, `[1, 1]` oraz `[1, 1, 1]`.

Wywołując funkcję, możemy określić pozycję argumentu za pomocą nazwy parametru, jak w poniższym przykładzie. W ten sposób określamy wybrane pozycje bez konieczności określania pozostałych. To są tzw. argumenty nazwane.

```kotlin
fun cheer(how: String = "Hello,", who: String = "World") {
    print("$how $who")
}

fun main() {
    cheer(who = "Group") // Hello, Group
}
```

Składnia nazwanych parametrów może nam pomóc zwiększyć czytelność naszego kodu. Gdy znaczenie argumentu nie jest jasne, lepiej określić nazwę jego parametru.

```kotlin
fun main() {
    val list = listOf(1, 2, 3, 4)
    println(list.joinToString("-")) // 1-2-3-4
    // lepiej
    println(list.joinToString(separator = "-")) //  1-2-3-4
}
```

Nazywanie argumentów zapobiega także błędom wynikającym ze zmiany pozycji parametrów. W poniższym przykładzie, gdyby programista zamienił miejscami `name` i `surname`, kod wciąż działałby tak samo dzięki użyciu nazw parametrów.

```kotlin
class User(
    val name: String,
    val surname: String,
)

val user = User(
    name = "Norbert",
    surname = "Moskała",
)
```

Uważa się, że stosowanie nazwanych argumentów jest dobrą praktyką, gdy wywołujemy funkcje z wieloma argumentami, których znaczenie może nie być oczywiste dla przyszłych programistów czytających nasz kod.

### Przeciążanie funkcji

W Kotlinie możemy zdefiniować funkcje o tej samej nazwie w tym samym zakresie (pliku lub klasie), o ile mają różne typy parametrów lub różną liczbę parametrów. Nazywa się to przeciążaniem funkcji (overloading). Kotlin decyduje, którą funkcję wykonać na podstawie typów argumentów.

```kotlin
fun a(a: Any) = "Any"
fun a(i: Int) = "Int"
fun a(l: Long) = "Long"

fun main() {
    println(a(1)) // Int
    println(a(18L)) // Long
    println(a("ABC")) // Any
}
```

Praktycznym przykładem przeciążania funkcji jest dostarczenie wielu wariantów funkcji dla wygody użytkownika.

```kotlin
import java.math.BigDecimal

class Money(val amount: BigDecimal, val currency: String)

fun pln(amount: BigDecimal) = Money(amount, "PLN")
fun pln(amount: Int) = pln(amount.toBigDecimal())
fun pln(amount: Double) = pln(amount.toBigDecimal())
```

### Składnia infiksowa

Metody z pojedynczym parametrem mogą używać modyfikatora `infix`, który pozwala na specjalny rodzaj wywołania funkcji: bez kropki i nawiasów argumentu.

```kotlin
class View
class ViewInteractor {
    infix fun clicks(view: View) { 
        // ...
    }
}

fun main() {
    val aView = View()
    val interactor = ViewInteractor()

    // normalna notacja
    interactor.clicks(aView)
    // notacja infiksowa
    interactor clicks aView
}
```

Tę notację stosuje się w niektórych funkcjach z biblioteki stdlib Kotlin (Standard Library), takich jak operacje bitowe `and`, `or` i `xor` na liczbach (przedstawione w rozdziale *Podstawowe typy, ich literały i operacje*).

```kotlin
fun main() {
    // notacja infiksowa
    println(0b011 and 0b001) // 1, czyli 0b001
    println(0b011 or 0b001) // 3, czyli 0b011
    println(0b011 xor 0b001) // 2, czyli 0b010

    // normalna notacja
    println(0b011.and(0b001)) // 1, czyli 0b001
    println(0b011.or(0b001)) // 3, czyli 0b011
    println(0b011.xor(0b001)) // 2, czyli 0b010
}
```

Notacja infiksowa została wprowadzona wyłącznie dla naszej wygody. Jest to Kotlinowy "syntactic sugar", czyli funkcjonalność zaprojektowana tylko po to, aby ułatwić czytanie lub pisanie kodu.

> W odniesieniu do pozycji operatorów lub funkcji w stosunku do ich operandów oraz argumentów używamy trzech rodzajów pozycji: prefiksowa, infiksowa i postfiksowa. Notacja prefiksowa polega na umieszczeniu operatora lub funkcji **przed** operandami lub argumentami[^06_7]. Dobrym przykładem jest plus lub minus umieszczony przed pojedynczą liczbą (jak w `+12` lub `-3,14`). Można by też argumentować, że wywołanie funkcji pliku również używa notacji prefiksowej, ponieważ nazwa funkcji pojawia się przed argumentami (jak w `maxOf(10, 20)`). Notacja infiksowa polega na umieszczeniu operatora lub funkcji **pomiędzy** operandami lub argumentami[^06_8]. Dobrym przykładem jest plus lub minus pomiędzy dwiema liczbami (jak `1 + 2` lub `10 - 7`). Można by też argumentować, że wywołanie metody z argumentami również używa notacji infiksowej, ponieważ nazwa funkcji znajduje się między odbiorcą (obiektem, na którym wywołujemy tę metodę) a argumentami (jak `account.add(money)`). W Kotlin używamy terminu "notacja infiksowa" w bardziej restrykcyjny sposób, odnosząc się do specjalnej notacji używanej dla metod z modyfikatorem `infix`. Notacja postfiksowa polega na umieszczeniu operatora lub funkcji **za** operandami lub argumentami[^06_9]. W nowoczesnym programowaniu notacja postfiksowa praktycznie nie jest już używana. Można by argumentować, że wywołanie metody bez argumentów to notacja przyrostkowa, jak w `str.uppercase()`.

### Formatowanie funkcji

Gdy deklaracja funkcji (nazwa, parametry i typ wyniku) jest zbyt długa, aby zmieścić się w jednej linii, dzielimy ją tak, aby każda definicja parametru znajdowała się w innej linii, a początek i koniec deklaracji funkcji, wraz z typem zwracanym przez nią, również były na oddzielnych liniach.

```kotlin
fun veryLongFunction(
    param1: Param1Type,
    param2: Param2Type,
    param3: Param3Type,
): ResultType {
    // ciało
}
```

Klasy są formatowane w ten sam sposób[^06_10]:

```kotlin
class VeryLongClass(
    val property1: Type1,
    val property2: Type2,
    val property3: Type3,
) : ParentClass(), Interface1, Interface2 {
    // ciało
}
```

Gdy wywołanie funkcji[^06_11] jest zbyt długie, formatujemy je w podobny sposób: każdy argument znajduje się w innym wierszu. Istnieją jednak wyjątki od tej zasady, takie jak utrzymanie wielu parametrów vararg w tej samej linii.

```kotlin
fun makeUser(
    name: String,
    surname: String,
): User = User(
    name = name,
    surname = surname,
)

class User(
    val name: String,
    val surname: String,
)

fun main() {
    val user = makeUser(
        name = "Norbert",
        surname = "Moskała",
    )

    val characters = listOf(
        "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
        "K", "L", "M", "N", "O", "P", "R", "S", "T", "U",
        "W", "X", "Y", "Z",
    )
}
```

W tej książce szerokość moich linii jest znacznie mniejsza niż w normalnych projektach, dlatego jestem zmuszony częściej łamać linie, niż bym chciał (mam do dyspozycji tylko 62 znaki w linii).

Zauważ, że gdy podaję argumenty lub parametry, czasami dodaję przecinek na końcu. Nazywa się to **trailing comma**. Taka notacja jest opcjonalna.

```kotlin
fun printName(
    name: String,
    surname: String, // <- trailing comma
) {
    println("$name $surname")
}

fun main() {
    printName(
        name = "Norbert",
        surname = "Moskała", // <- trailing comma
    )
}
```

Lubię używać trailing comma, ponieważ ułatwia dodawanie kolejnego elementu w przyszłości. Bez niej dodanie lub usunięcie elementu wymaga nie tylko nowej linii, ale także dodatkowego przecinka po ostatnim elemencie. Prowadzi to do bezsensownych modyfikacji linii w Gicie, co utrudnia odczytanie tego, co tak naprawdę się zmieniło w naszym projekcie. Niektórzy programiści nie lubią trailing comma, przez co w społeczności toczy się święta wojna. Zdecydujcie w swoim zespole, czy Wam się ona podoba, czy nie, i bądźcie konsekwentni w swoich projektach.

![Dodawanie parametru i argumentu w Git, gdy używana jest trailing comma.](trailing_comma_used.png)

![Dodawanie parametru i argumentu w Git, gdy trailing comma nie jest używana.](trailing_comma_not_used.png)

### Podsumowanie

Jak widać, funkcje w Kotlinie oferują wiele potężnych możliwości. Składnia z pojedynczym wyrażeniem sprawia, że proste funkcje są krótsze. Nazwane argumenty i domyślne wartości pomagają nam poprawić czytelność kodu i zmniejszyć liczbę funkcji. Typ wyniku `Unit` sprawia, że każde wywołanie funkcji jest wyrażeniem. Parametry `vararg` pozwalają na używanie dowolnej liczby argumentów dla jednej pozycji parametru. Notacja infiksowa wprowadza wygodniejszy sposób wywoływania pewnego rodzaju funkcji. Trailing comma pomaga zmniejszyć liczbę zmian w Gicie. Wszystko to jest dla naszej wygody. Czas przejść do kolejnej przydatnej funkcjonalności języka Kotlin, czyli do używania pętli `for`.

[^06_1]: To było podczas panelu dyskusyjnego na konferencji KotlinConf w Amsterdamie. Źródło: youtu.be/heqjfkS4z2I?t=660
[^06_2]: Ta zasada ma pewne wyjątki. Na przykład w przypadku Androida, funkcje Jetpack Compose powinny być nazwane według konwencji UpperCamelCase. Ponadto, testy jednostkowe często są nazwane pełnymi zdaniami otoczonymi odwrotnym apostrofem.
[^06_3]: Przypominam, że wyrażenie to część kodu, która zwraca wartość.
[^06_4]: Zobacz *Efektywny Kotlin* *Temat 4: Nie udostępniaj wywnioskowanych typów*.
[^06_5]: Po angielsku używane jest pojęcie **top-level functions**, ale **funkcje na najwyższym poziomie** brzmi mało intuicyjnie, więc zdecydowałem się używać pojęcia **funkcje pliku", które lepiej oddaje sens tego pojęcia.
[^06_6]: Po angielsku **member functions**, co często tłumaczy się na **funkcje członkowskie**, które to tłumaczenie uważam za wyjątkowo brzydkie i nie będę go stosował. To, co definiuje elementy określane jako "member", to ich przypisanie do klasy, a więc widzę zasadnym używanie pojęcia "funkcje klasy" jako tłumaczenie "member function" oraz "właściwość klasy" jako tłumaczenie "member property".
[^06_7]: Od łacińskiego słowa *praefixus*, które oznacza "umocowany z przodu".
[^06_8]: Od łacińskiego słowa *infixus*, czas przeszły słowa *infigere*, które można przetłumaczyć jako "umocowany pomiędzy".
[^06_9]: Stworzone z przedrostka "post-", który oznacza "po, za", oraz słowa "fix", oznaczające "umocowany w miejscu".
[^06_10]: Klasy omówimy w rozdziale *Klasy i interfejsy*.
[^06_11]: Wywołanie konstruktora jest również uważane za wywołanie funkcji w Kotlinie.
