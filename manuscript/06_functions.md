## Funkcje

Kiedy Andrey Breslav, twórca Kotlin, został zapytany o swoją ulubioną funkcję podczas panelu dyskusyjnego na KotlinConf w Amsterdamie, powiedział, że to właśnie funkcje[^06_1]. W końcu funkcje są najważniejszymi elementami naszych programów. Spójrz na aplikacje w rzeczywistym świecie, większość kodu albo definiuje, albo wywołuje funkcje.

![Przykładem jest losowa klasa z otwartoźródłowego projektu APKUpdater. Zauważ, że niemal każdy wiersz albo definiuje, albo wywołuje funkcję.](06_example_app.png)

W Kotlin definiujemy funkcje za pomocą słowa kluczowego `fun`. Dlatego mamy tak dużo "fun" w Kotlin. Z odrobiną kreatywności funkcja może składać się tylko z `fun`:

```kotlin
fun <Fun> `fun`(`fun`: Fun): Fun = `fun`
```

> Jest to tzw. *funkcja tożsamościowa*, funkcja zwracająca swój argument bez żadnych modyfikacji. Posiada ona generyczny parametr typu `Fun`, ale zostanie to wyjaśnione w rozdziale *Generics*.

Zgodnie z konwencją nazywamy funkcje używając składni lower camelCase[^06_0]. Formalnie możemy używać znaków, podkreślnika `_` oraz cyfr (ale nie na pierwszej pozycji), jednak ogólnie powinniśmy używać tylko znaków.

{width: 50%}
![W Kotlin nazywamy funkcje zgodnie z lowerCamelCase.](camelCase.png)

To wygląda jak typowa funkcja:

```kotlin
fun square(x: Double): Double {
    return x * x
}

fun main() {
    println(square(10.0)) // 100.0
}
```

Zauważ, że typ parametru jest określony po nazwie zmiennej i dwukropku, a typ wyniku jest określony po dwukropku w nawiasach parametrów. Taka notacja jest typowa dla języków z silnym wsparciem dla wnioskowania o typach, ponieważ łatwiej jest dodawać lub usuwać wyraźne definicje typów.

```kotlin
val a: Int = 123
// łatwo przekształcić z lub do
val a = 123

fun add(a: Int, b: Int): Int = a + b

// łatwo przekształcić z lub do
fun add(a: Int, b: Int) = a + b
```

Aby użyć zarezerwowanego słowa kluczowego jako nazwy funkcji (takiego jak `fun` czy `when`), użyjemy znaków backtick, jak w przykładzie powyżej. Kiedy funkcja ma niedozwoloną nazwę, zarówno jej definicja, jak i wywołania wymagają backticków.

Innym zastosowaniem backticków jest nazywanie funkcji testów jednostkowych, aby można było je opisać w zwykłym języku angielskim, jak w poniższym przykładzie. Nie jest to standardowa praktyka, ale jest to dość popularna praktyka, którą wiele zespołów decyduje się przyjąć.

```kotlin
class CartViewModelTests {
    @Test
    fun `should show error dialog when no items loaded`() {
        ...
    }
}
```

### Funkcje z pojedynczym wyrażeniem

Wiele funkcji w rzeczywistych projektach ma tylko jedno wyrażenie[^06_2], więc zaczynają się od użycia słowa kluczowego `return`. Funkcja `square` zdefiniowana powyżej to świetny przykład. W przypadku takich funkcji zamiast definiować ciało za pomocą nawiasów klamrowych, możemy użyć znaku równości (`=`) i tylko podać wyrażenie obliczające wynik bez podawania `return`. Jest to *składnia pojedynczego wyrażenia*, a funkcje, które jej używają, nazywane są *funkcjami z pojedynczym wyrażeniem*.

```kotlin
fun square(x: Double): Double = x * x

fun main() {
    println(square(10.0)) // 100.0
}
```

Wyrażenie może być bardziej skomplikowane i zajmować wiele linii. Jest to dopuszczalne, o ile jego ciało stanowi pojedyncze polecenie.

```kotlin
fun findUsers(userFilter: UserFilter): List<User> =
    userRepository
        .getUsers()
        .map { it.toDomain() }
        .filter { userFilter.accepts(it) }
```

Kiedy używamy składni funkcji z pojedynczym wyrażeniem, możemy wnioskować typ wyniku. Nie musimy tego robić, ponieważ jawnie określony typ wyniku może być wciąż przydatny dla bezpieczeństwa i czytelności[^06_3], ale możemy.

```kotlin
fun square(x: Double) = x * x

fun main() {
    println(square(10.0)) // 100.0
}
```

### Functions on all levels

Kotlin allows us to define functions on many levels, but this isn’t very obvious as Java only allows functions inside classes. In Kotlin, we can define:
* functions in files outside any classes, called **top-level functions**,
* functions inside classes or objects, called **member functions** (they are also called **methods**),
* functions inside functions, called **local functions** or **nested functions**.

```kotlin
// Top-level function
fun double(i: Int) = i * 2

class A {
    // Member function (method)
    private fun triple(i: Int) = i * 3

    // Member function (method)
    fun twelveTimes(i: Int): Int {
        // Local function
        fun fourTimes() = double(double(i))
        return triple(fourTimes())
    }
}

// Top-level function
fun main(args: Array<String>) {
    double(1) // 2
    A().twelveTimes(2) // 24
}
```

Funkcje na poziomie najwyższym (zdefiniowane poza klasami) często są używane do definiowania narzędzi, małych, ale użytecznych funkcji, które pomagają nam w rozwoju. Funkcje na poziomie najwyższym można przenosić i dzielić między plikami. W wielu przypadkach funkcje na poziomie najwyższym w Kotlin są lepsze niż statyczne funkcje w Java. Korzystanie z nich wydaje się intuicyjne i wygodne dla programistów.

Jednak w przypadku funkcji lokalnych (zdefiniowanych wewnątrz funkcji) historia wygląda inaczej. Często widzę, że programistom brakuje wyobraźni, aby z nich korzystać (ze względu na brak wystawienia na nie). Funkcje lokalne są popularne w JavaScript i Python, ale w Java nie ma czegoś takiego. Własnością funkcji lokalnych jest to, że mogą one bezpośrednio uzyskiwać dostęp do zmiennych lokalnych lub je modyfikować. Są one używane do wyodrębniania powtarzającego się kodu wewnątrz funkcji, który operuje na zmiennych lokalnych. Dłuższe funkcje powinny opowiadać "historię", a lokalne podprogramy mogą owijać wyrażenie blokowe w opisowe nazwy.

Spójrz na poniższy przykład, który przedstawia funkcję walidującą formularz. Sprawdza ona warunki dla pól formularza. Jeśli jakiś warunek nie jest spełniony, powinniśmy pokazać błąd i zmienić lokalną zmienną isValid na false, w takim przypadku nie powinniśmy jednak zwracać się z funkcji, ponieważ chcemy sprawdzić wszystkie pola (nie powinniśmy zatrzymywać się na pierwszym, które zawodzi). To jest przykład, gdzie funkcja lokalna może nam pomóc wyodrębnić powtarzające się zachowanie.

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

Zmienna zdefiniowana jako część definicji funkcji nazywa się **parametrem**. Wartość przekazywana podczas wywoływania funkcji nazywa się **argumentem**.

```kotlin
fun square(x: Double) = x * x // x to parametr

fun main() {
    println(square(10.0)) // 10.0 to argument
    println(square(0.0)) // 0.0 to argument
}
```

W Kotlin parametry są tylko do odczytu, więc nie można im przypisać nowej wartości.

```kotlin
fun a(i: Int) {
    i = i + 10 // BŁĄD
    // ...
}
```

Jeśli musisz zmodyfikować zmienną parametru, jedynym sposobem jest zastąpienie jej lokalną zmienną, która jest zmienna (mutable).

```kotlin
fun a(i: Int) {
    var i = i + 10
    // ...
}
```

To możliwe, ale niezalecane. Parametr przechowuje wartość używaną jako argument, a ta wartość nie powinna ulegać zmianie. Lokalna zmienna do odczytu i zapisu reprezentuje inny koncept i dlatego powinna mieć inną nazwę.

### Typ zwracany `Unit`

W Kotlin wszystkie funkcje mają typ wyniku, więc każde wywołanie funkcji jest wyrażeniem. Gdy typ nie jest określony, domyślnym typem wyniku jest `Unit`, a domyślną wartością wyniku jest obiekt `Unit`.

```kotlin
fun someFunction() {}

fun main() {
    val res: Unit = someFunction()
    println(res) // kotlin.Unit
}
```

`Unit` to po prostu bardzo prosty obiekt, który jest używany jako zastępcza wartość, gdy nic innego nie jest zwracane. Gdy określasz funkcję bez wyraźnego typu wyniku, jej typ wyniku będzie niejawnie równać się `Unit`. Gdy definiujesz funkcję bez `return` w ostatniej linii, jest to równoznaczne z użyciem `return` bez wartości. Użycie `return` bez wartości to to samo, co zwrócenie `Unit`.

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

### Parametry Vararg

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

Oznacza to, że parametr vararg przechowuje kolekcję wartości, dlatego nie może mieć typu pojedynczego obiektu. Parametr vararg reprezentuje więc tablicę zadeklarowanego typu, a my możemy iterować przez tablice za pomocą pętli for (co zostanie wyjaśnione bardziej szczegółowo w następnym rozdziale).

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

Wrócimy do parametrów vararg w rozdziale *Kolekcje*, w sekcji poświęconej tablicom.

### Składnia nazwanych parametrów i argumenty domyślne

Deklarując funkcje, często określamy opcjonalne parametry. Dobrym przykładem jest `joinToString`, które przekształca obiekt iterable w `String`. Można go używać bez żadnych argumentów lub zmieniać jego zachowanie za pomocą konkretnych argumentów.

```kotlin
fun main() {
    val list = listOf(1, 2, 3, 4)
    println(list.joinToString()) // 1, 2, 3, 4
    println(list.joinToString(separator = "-")) //  1-2-3-4
    println(list.joinToString(limit = 2)) //  1, 2, ...
}
```

W Kotlin wiele innych funkcji używa opcjonalnej parametryzacji, ale jak to zrobić? Wystarczy umieścić znak równości po parametrze, a następnie określić wartość domyślną.

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
fun addOneAndPrint(list: MutableList<Int> = mutableListOf()) {
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

Wywołując funkcję, możemy określić pozycję argumentu za pomocą nazwy parametru, jak w poniższym przykładzie. W ten sposób możemy określić późniejsze opcjonalne pozycje bez określania wcześniejszych. Nazywa się to *składnią nazwanych parametrów*.

```kotlin
fun cheer(how: String = "Hello,", who: String = "World") {
    print("$how $who")
}

fun main() {
    cheer(who = "Group") // Hello, Group
}
```

Składnia nazwanych parametrów jest bardzo przydatna do poprawy czytelności naszego kodu. Gdy znaczenie argumentu nie jest jasne, lepiej określić dla niego nazwę parametru.

```kotlin
fun main() {
    val list = listOf(1, 2, 3, 4)
    println(list.joinToString("-")) // 1-2-3-4
    // lepiej
    println(list.joinToString(separator = "-")) //  1-2-3-4
}
```

Nazywanie argumentów zapobiega także błędom wynikającym ze zmiany pozycji parametrów.

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

W powyższym przykładzie, bez nazwanych argumentów, programista mógłby zamienić pozycje `name` i `surname`. Gdyby tutaj nie użyto nazwanych argumentów, doprowadziłoby to do nieprawidłowego imienia i nazwiska w obiekcie. Nazwane argumenty chronią nas przed takimi sytuacjami.

Uważa się, że stosowanie konwencji nazwanych argumentów jest dobrą praktyką, gdy wywołujemy funkcje z wieloma argumentami, których znaczenie może nie być oczywiste dla programistów czytających nasz kod w przyszłości.

### Przeciążanie funkcji

W Kotlin możemy zdefiniować funkcje o tej samej nazwie w tym samym zakresie (plik lub klasa), o ile mają różne typy parametrów lub różną liczbę parametrów. Nazywa się to przeciążaniem funkcji (**overloading**). Kotlin decyduje, którą funkcję wykonać, na podstawie typów określonych argumentów.

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

Notacja infiksowa służy tylko dla naszej wygody. Jest to przykład składni cukru syntaktycznego Kotlin - składni zaprojektowanej tylko po to, aby ułatwić czytanie lub wyrażanie.

> W odniesieniu do pozycji operatorów lub funkcji w stosunku do ich operandów lub argumentów używamy trzech rodzajów pozycji: przedrostka, infiksu i przyrostka. Notacja przedrostkowa polega na umieszczeniu operatora lub funkcji **przed** operandami lub argumentami[^06_8]. Dobrym przykładem jest plus lub minus umieszczony przed pojedynczą liczbą (jak `+12` lub `-3,14`). Można by też argumentować, że wywołanie funkcji na najwyższym poziomie również używa notacji przedrostkowej, ponieważ nazwa funkcji pojawia się przed argumentami (jak `maxOf(10, 20)`). Notacja infiksowa polega na umieszczeniu operatora lub funkcji **pomiędzy** operandami lub argumentami[^06_6]. Dobrym przykładem jest plus lub minus pomiędzy dwiema liczbami (jak `1 + 2` lub `10 - 7`). Można by też argumentować, że wywołanie metody z argumentami również używa notacji infiksowej, ponieważ nazwa funkcji znajduje się między odbiorcą (obiektem, na którym wywołujemy tę metodę) a argumentami (jak `account.add(money)`). W Kotlin używamy terminu "notacja infiksowa" w bardziej restrykcyjny sposób, odnosząc się do specjalnej notacji używanej dla metod z modyfikatorem `infix`. Notacja przyrostkowa polega na umieszczeniu operatora lub funkcji **za** operandami lub argumentami[^06_7]. W nowoczesnym programowaniu notacja przyrostkowa praktycznie nie jest już używana. Można by argumentować, że wywołanie metody bez argumentów to notacja przyrostkowa, jak w `str.uppercase()`.

### Formatowanie funkcji

Gdy deklaracja funkcji (nazwa, parametry i typ wyniku) jest zbyt długa, aby zmieścić się w jednym wierszu, dzielimy ją tak, aby każda definicja parametru znajdowała się w innym wierszu, a początek i koniec deklaracji funkcji również były na oddzielnych wierszach.

```kotlin
fun veryLongFunction(
    param1: Param1Type,
    param2: Param2Type,
    param3: Param3Type,
): ResultType {
    // ciało
}
```

Klasy są formatowane w ten sam sposób[^06_5]:

```kotlin
class VeryLongClass(
    val property1: Type1,
    val property2: Type2,
    val property3: Type3,
) : ParentClass(), Interface1, Interface2 {
    // ciało
}
```

Gdy wywołanie funkcji[^06_4] jest zbyt długie, formatujemy je w podobny sposób: każdy argument znajduje się w innym wierszu. Istnieją jednak wyjątki od tej zasady, takie jak utrzymanie wielu parametrów vararg w tej samej linii.

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

W tej książce szerokość moich linii jest znacznie mniejsza niż w normalnych projektach, dlatego jestem zmuszony częściej łamać linie, niż bym chciał.

Zauważ, że gdy podaję argumenty lub parametry, czasami dodaję przecinek na końcu. Nazywa się to **końcowym przecinkiem** (trailing comma). Taka notacja jest opcjonalna.

```kotlin
fun printName(
    name: String,
    surname: String, // <- końcowy przecinek
) {
    println("$name $surname")
}

fun main() {
    printName(
        name = "Norbert",
        surname = "Moskała", // <- końcowy przecinek
    )
}
```

Lubię używać notacji z końcowym przecinkiem, ponieważ ułatwia dodawanie kolejnego elementu w przyszłości. Bez niej dodanie lub usunięcie elementu wymaga nie tylko nowej linii, ale także dodatkowego przecinka po ostatnim elemencie. Prowadzi to do bezsensownych modyfikacji linii w Git, co utrudnia odczytanie tego, co tak naprawdę się zmieniło w naszym projekcie. Niektórzy programiści nie lubią notacji z końcowym przecinkiem, co czasami prowadzi do świętej wojny. Zdecydujcie się w swoim zespole, czy Wam się to podoba, czy nie, i bądźcie konsekwentni w swoich projektach.

![Dodawanie parametru i argumentu w Git, gdy używany jest końcowy przecinek.](trailing_comma_used.png)

![Dodawanie parametru i argumentu w Git, gdy końcowy przecinek nie jest używany.](trailing_comma_not_used.png)

{pagebreak}

### Podsumowanie

Jak widać, funkcje w Kotlin mają wiele potężnych funkcji. Składnia jednowyrażeniowa sprawia, że proste funkcje są krótsze. Nazwane i domyślne argumenty pomagają nam poprawić bezpieczeństwo i czytelność. Typ wyniku `Unit` sprawia, że każde wywołanie funkcji jest wyrażeniem. Parametry vararg pozwalają na używanie dowolnej liczby argumentów dla jednej pozycji parametru. Notacja infiksowa wprowadza wygodniejszy sposób wywoływania pewnego rodzaju funkcji. Końcowe przecinki minimalizują liczbę zmian w git. Wszystko to jest dla naszej wygody. Na razie jednak przejdźmy do innego tematu: używania pętli for.

[^06_0]: Ta zasada ma pewne wyjątki. Na przykład w przypadku Androida, funkcje Jetpack Compose powinny być nazwane według konwencji UpperCamelCase. Ponadto, testy jednostkowe często są nazwane pełnymi zdaniem w nawiasach.
[^06_1]: Źródło: https://youtu.be/heqjfkS4z2I?t=660
[^06_2]: Przypominam, że wyrażenie to część naszego kodu, która zwraca wartość.
[^06_3]: Zobacz *Effective Kotlin* *Pozycja 4: Nie eksponuj wnioskowanych typów*
[^06_4]: Wywołanie konstruktora jest również uważane za wywołanie funkcji w Kotlin.
[^06_5]: Omówimy klasy później w tej książce, w rozdziale *Klasy i interfejsy*.
[^06_6]: Od łacińskiego słowa infixus, czas przeszły od infigere, które można przetłumaczyć jako "umocowany pomiędzy".
[^06_7]: Stworzone z przedrostka "post-", który oznacza "po, za", oraz słowa "fix", oznaczające "umocowany w miejscu".
[^06_8]: Od łacińskiego słowa praefixus, które oznacza "umocowany z przodu".
