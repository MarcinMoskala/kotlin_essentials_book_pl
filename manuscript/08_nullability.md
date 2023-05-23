## Nullowalność

Kotlin pierwotnie powstawał jako rozwiązanie problemów Javy, a największą bolączką w Javie jest możliwość wystąpienia wartości `null` niemalże wszędzie. W Javie, jak w wielu innych językach, każda zmienna może przyjmować wartość `null`. Każde wywołanie na tej wartości prowadzi do słynnego `NullPointerException` (NPE). Jest to najpopularniejszy wyjątek w większości projektów napisanych w Javie[^08_0]. Jest tak powszechny, że po słynnym przemówieniu Sir Charlesa Antony'ego Richarda Hoare'a często nazywany jest "błędem za miliard dolarów". Powiedział on: "Nazywam to moim błędem za miliard dolarów. Był to wynalazek null reference w 1965 roku... Doprowadziło to do niezliczonych błędów, podatności i awarii systemów, które przez ostatnie czterdzieści lat prawdopodobnie spowodowały straty rzędu miliarda dolarów".

Jednym z priorytetów Kotlina było ostateczne rozwiązanie tego problemu, co udało się doskonale. Wprowadzone w nim mechanizmy są tak skuteczne, że zobaczenie `NullPointerException` rzuconego z kodu napisanego w Kotlinie jest niezwykle rzadkie. Wartość `null` przestała być problemem, a programiści Kotlina już się jej nie obawiają. Stała się naszym przyjacielem.

Jak to zatem działa? Opiera się to na następujących zasadach:

1. Każda zmienna musi mieć określoną wartość. Nie ma czegoś takiego jak domyślna wartość `null`.

```kotlin
var person: Person // BŁĄD KOMPILACJI,
// zmienna musi być zainicjalizowana
```

2. Zwykły typ nie akceptuje wartości `null`.

```kotlin
var person: Person = null // BŁĄD KOMPILACJI,
// Person nie jest typem nullowalnym i nie może być null
```

3. Aby określić typ nullowalny, musisz udekorować zwykły typ znakiem zapytania (`?`).

```kotlin
var person: Person? = null // OK
```

4. Zmienne nullowalne nie mogą być używane bezpośrednio. Muszą być wywyływane w bezpieczny sposób lub wcześniej rzutowane za pomocą jednego z narzędzi przedstawionych później w tym rozdziale.

```kotlin
person.name // BŁĄD KOMPILACJI,
// typ person jest nullowanych, więc nie możemy go używać bezpośrednio
```

Dzięki wszystkim tym mechanizmom zawsze wiemy, co może być `null`, a co nie. Używamy wartości `null` tylko wtedy, gdy istnieje ku temu konkretny powód. W takich przypadkach użytkownicy są zmuszeni do jawnej obsługi wartości `null`. We wszystkich innych przypadkach nie ma takiej potrzeby. Jest to doskonałe rozwiązanie, wymagające jednak dobrych narzędzi do obsługi nullowalności w wygodny dla programistów sposób.

Kotlin obsługuje wiele sposobów użycia wartości nullowalnych, w tym bezpieczne wywołania, asercje not-null, inteligentne rzutowanie czy operator Elvisa. Omówmy je po kolei.

### Bezpieczne wywołania

Najprostszym sposobem wywołania metody lub właściwości na wartości nullowalnej jest bezpieczne wywołanie, które polega na użyciu znaku zapytania i kropki (`?.`) zamiast zwykłej kropki (`.`). Bezpieczne wywołania działają następująco:
* jeśli wartość to `null`, nic nie robi i zwraca `null`,
* jeśli wartość nie jest `null`, działa jak zwykłe wywołanie.

```kotlin
class User(val name: String) {
    fun cheer() {
        println("Cześć, mam na imię $name")
    }
}

var user: User? = null

fun main() {
    user?.cheer() // (nic nie robi)
    println(user?.name) // null
    user = User("Cookie")
    user?.cheer() // Cześć, mam na imię Cookie
    println(user?.name) // Cookie
}
```

Zauważ, że wynik bezpiecznego wywołania zawsze ma typ nullowalny, ponieważ bezpieczne wywołanie zwraca `null`, gdy jest wywołane na obiekcie `null`. Oznacza to, że wartość `null` propaguje się. Jeśli chcesz dowiedzieć się, jaką długość ma imię użytkownika, wywołanie `user?.name.length` się nie skompiluje. Mimo, że `name` nie jest wartością nullowalną, wynik `user?.name` to `String?`, więc musimy użyć ponownie bezpiecznego wywołania: `user?.name?.length`.

```kotlin
class User(val name: String) {
    fun cheer() {
        println("Cześć, mam na imię $name")
    }
}

var user: User? = null

fun main() {
    // println(user?.name.length) // NIEDOZWOLONE
    println(user?.name?.length) // null
    user = User("Cookie")
    // println(user?.name.length) // NIEDOZWOLONE
    println(user?.name?.length) // 10
}
```

### Asercja not-null

Kiedy nie spodziewamy się wartości `null` i chcemy rzucić wyjątkiem, gdy wystąpi, możemy użyć asercji not-null `!!`.

```kotlin
class User(val name: String) {
    fun cheer() {
        println("Cześć, mam na imię $name")
    }
}

var user: User? = User("Cookie")

fun main() {
    println(user!!.name.length) // 10
    user = null
    println(user!!.name.length) // rzuca NullPointerException
}
```

Powyższe rozwiązanie nie jest bardzo bezpieczne, ponieważ jeśli się mylimy i wartość `null` znajduje się tam, gdzie jej nie oczekujemy, doprowadzi to do wyjątku `NullPointerException`. Dlatego stosujemy `!!` tylko wtedy, gdy rzeczywiście chcemy zobaczyć wyjątek, jeśli wartość jest `null`. Aczkolwiek w takich przypadkach preferowanym rozwiązaniem jest użycie jednej z funkcji, które rzucają bardziej znaczący wyjątek, jak `requireNotNull` lub `checkNotNull`[^08_4]:
- `requireNotNull`, który przyjmuje wartość nullowalną jako argument i rzuca wyjątek `IllegalArgumentException` jeśli ta wartość jest równa `null`. W przeciwnym razie zwraca tę wartość jako nienullowalną.
- `checkNotNull`, który przyjmuje wartość nullowalną jako argument i rzuca wyjątek `IllegalStateException` jeśli ta wartość jest równa `null`. W przeciwnym razie zwraca tę wartość jako nienullowalną.

```kotlin
fun sendData(dataWrapped: Wrapper<Data>) {
    val data = requireNotNull(dataWrapped.data)
    val connection = checkNotNull(connections["db"])
    connection.send(data)
}
```

### Smart-casting

Smart-casting działa również dla wartości `null`. W związku z tym, w zakresie sprawdzenia, że wartość nie jest `null`, typ nullowalny jest rzutowany na typ nienullowalny.

```kotlin
fun printLengthIfNotNull(str: String?) {
    if (str != null) {
        println(str.length) // str smart-casted do String
    }
}
```

Smart-casting działa również, gdy używamy `return` lub `throw`.

```kotlin
fun printLengthIfNotNull(str: String?) {
    if (str == null) return
    println(str.length) // str smart-casted do String
}
```

```kotlin
fun printLengthIfNotNullOrThrow(str: String?) {
    if (str == null) throw Error()
    println(str.length) // str smart-casted do String
}
```

Smart-casting jest dość inteligentny i działa w różnych przypadkach, takich jak po `&&` i `||` w wyrażeniach logicznych.

```kotlin
fun printLengthIfNotNull(str: String?) {
    if (str != null && str.length > 0) {
        // str w wyrażeniu powyżej smart-casted do String
        // ...
    }
}
```

```kotlin
fun printLengthIfNotNull(str: String?) {
    if (str == null || str.length == 0) {
        // str w wyrażeniu powyżej smart-casted do String
        // ...
    }
}
```

```kotlin
fun printLengthIfNotNullOrThrow(str: String?) {
    requireNotNull(str) // str smart-casted do String
    println(str.length)
}
```

> Smart-casting działa dla `requireNotNull` dzięki Kotlinowym kontraktom, które opisałem w książce *Zaaawansowany Kotlin*.

### Operator Elvisa

Ostatnią specjalną funkcją Kotlina, która jest używana do obsługi wartości `null`, jest operator Elvisa `?:`. Tak, to pytajnik i dwukropek. Nazywa się go operatorem Elvisa, ponieważ przypomina Elvisa Presleya (z jego charakterystyczną fryzurą). Możesz sobie wyobrazić, że patrzy na nas zza muru, więc widzimy tylko jego włosy i oczy.

![](elvis.png)

Operator Elvisa umieszcza się pomiędzy dwiema wartościami. Jeśli wartość po lewej stronie operatora Elvisa nie jest `null`, to jest ona wynikiem. Jeśli lewa strona jest `null`, zwracana jest prawa strona.

```kotlin
fun main() {
    println("A" ?: "B") // A
    println("A" ?: null) // A
    println(null ?: "B") // B
    println(null ?: null) // null
}
```

Celem użycia operatora Elvisa jest określenie wartości domyślnej dla wartości nullowalnej.

```kotlin
class User(val name: String)

fun printName(user: User?) {
    val name: String = user?.name ?: "default"
    println(name)
}

fun main() {
    printName(User("Cookie")) // Cookie
    printName(null) // default
}
```

### Rozszerzenia dla typów nullowanych

Zwykłe funkcje nie mogą być wywoływane na wartościach nullowalnych. Istnieje jednak specjalny rodzaj funkcji, tzw. funkcje rozszerzające, który można zdefiniować w sposób pozwalający na wywoływanie ich z argumentami nullowalnymi[^08_3]. Biblioteka standardowa Kotlina definiuje następujące funkcje, które można wywoływać na `String?`:
* `orEmpty` zwraca pusty string zamiast `null`.
* `isNullOrEmpty` zwraca `true`, jeśli wartość to `null` lub pusty string. W przeciwnym razie zwraca `false`.
* `isNullOrBlank` zwraca `true`, jeśli wartość to `null`, albo string pusty lub zawierający jedynie białe znaki. W przeciwnym razie zwraca `false`.

```kotlin
fun check(str: String?) {
    println("Wartość: \"$str\"")
    println("Wartość lub pusta: \"${str.orEmpty()}\"")
    println("Czy jest null lub pusta? " + str.isNullOrEmpty())
    println("Czy jest null lub pusta? " + str.isNullOrBlank())
}

fun main() {
    check("ABC")
    // Wartość: "ABC"
    // Wartość lub pusta: "ABC"
    // Czy jest null lub pusta? false
    // Czy jest null lub pusta? false
    check(null)
    // Wartość: "null"
    // Wartość lub pusta: ""
    // Czy jest null lub pusta? true
    // Czy jest null lub pusta? true
    check("")
    // Wartość: ""
    // Wartość lub pusta: ""
    // Czy jest null lub pusta? true
    // Czy jest null lub pusta? true
    check("       ")
    // Wartość: "       "
    // Wartość lub pusta: "       "
    // Czy jest null lub pusta? false
    // Czy jest null lub pusta? true
}
```

Biblioteka standardowa Kotlina zawiera również podobne funkcje dla nullowalnych list:
* `orEmpty` zwraca pustą listę zamiast `null`.
* `isNullOrEmpty` zwraca `true`, jeśli wartość jest `null` lub pusta. W przeciwnym razie zwraca `false`.

```kotlin
fun check(list: List<Int>?) {
    println("Lista: \"$list\"")
    println("Lista lub pusta: \"${list.orEmpty()}\"")
    println("Czy jest null lub pusta? " + list.isNullOrEmpty())
}

fun main() {
    check(listOf(1, 2, 3))
    // Lista: "[1, 2, 3]"
    // Lista lub pusta: "[1, 2, 3]"
    // Czy jest null lub pusta? false
    check(null)
    // Lista: "null"
    // Lista lub pusta: "[]"
    // Czy jest null lub pusta? true
    check(listOf())
    // Lista: "[]"
    // Lista lub pusta: "[]"
    // Czy jest null lub pusta? true
}
```

Te funkcje pomagają nam operować na wartościach nullowanych.

### `null` to nasz przyjaciel

To, że każda wartość mogła być nullem, jest ogromnym problemem w językach takich jak Java. W efekcie programiści zaczęli unikać unikać wartości `null` i traktować ją jak wroga. Wiele książek dotyczących dobrych praktyk programowania w Javie sugeruje unikanie wartości `null`, na przykład poprzez zastąpienie ich pustymi listami, stringami lub specjalną wartością enuma (patrz "Temat 43. Zwracanie pustych tablic lub kolekcji zamiast wartości null" z bardzo popularnej książki *Java. Efektywne programowanie.* 2nd Edition autorstwa Joshua Blocha). Takie praktyki nie mają sensu w Kotlinie, gdzie mamy dobre wsparcie nullowalności i nie powinniśmy obawiać się wartości `null`. W Kotlinie traktujemy `null` jako naszego przyjaciela, a nie jako błąd[^08_1]. Rozważ funkcję `getUsers`. Istnieje istotna różnica między zwracaniem pustej listy a `null`. Pusta lista powinna być interpretowana jako "wynik to pusta lista użytkowników, ponieważ żadni nie są dostępni". Wynik `null` powinien być interpretowany jako "nie można wyprodukować wyniku, a lista użytkowników pozostaje nieznana". Zapomnij o przestarzałych praktykach dotyczących nullowalności. W Kotlinie wartość `null` to nasz przyjaciel[^08_2].

### Właściwości lateinit

Są sytuacje, gdy chcemy, aby właściwość klasy miała typ nienullowalny, ale nie możemy określić jej wartości podczas tworzenia obiektu. Chodzi przede wszystkim o właściwości, których wartość jest wstrzykiwana lub są tworzone w jednej z pierwszych metod cyklu życia klasy. Uczynienie takich właściwości nullowalnymi wymagałoby ich odpakowania przy każdym użyciu, mimo iż wiedzielibyśmy, że nie mogą mieć one wartości `null`, ponieważ spodziewamy się że ich wartość zostanie wstrzyknięta lub ustawiona odpowiednio wcześniej. Dla takich sytuacji twórcy Kotlina wprowadzili modyfikator `lateinit`. Gdy go używamy, właściwość nie określa wartości pierwotnej, a przy tym musi mieć typ nienullowany. Kotlin spodziewa się, że ustawimy wartość tej właściwości przed jej pierwszym użyciem. Przykłady użycia `lateinit`:

```kotlin
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Injector.inject(this)
        presenter.onCreate()
    }
}

class UserServiceTest {
    lateinit var userRepository: InMemoryUserRepository
    lateinit var userService: UserService

    @Before
    fun setup() {
        userRepository = InMemoryUserRepository()
        userService = UserService(userRepository)
    }

    @Test
    fun `powinien zarejestrować nowego użytkownika`() {
        // kiedy
        userService.registerUser(aRegisterUserRequest)

        // wtedy
        userRepository.hasUserId(aRegisterUserRequest.id)
        // ...
    }
}
```

Gdy używamy właściwości lateinit, musimy ustawić jej wartość przed jej pierwszym użyciem. Jeśli tego nie zrobimy, program rzuci wyjątek `UninitializedPropertyAccessException` w czasie wykonywania.

```kotlin
lateinit var text: String

fun main() {
    println(text) // BŁĄD W CZASIE WYKONYWANIA!
    // właściwość lateinit text nie została zainicjowana
}
```

Zawsze możesz sprawdzić, czy właściwość została zainicjowana, używając właściwości `isInitialized` na jej referencji. Aby użyć referencji do właściwości, użyj dwóch dwukropków i nazwy właściwości[^08_5].

```kotlin
lateinit var text: String

private fun printIfInitialized() {
    if (::text.isInitialized) {
        println(text)
    } else {
        println("Nie zainicjowany")
    }
}

fun main() {
    printIfInitialized() // Nie zainicjowany
    text = "ABC"
    printIfInitialized() // ABC
}
```

### Podsumowanie

Kotlin oferuje potężne wsparcie dla nullowalności, które sprawia, że `null` przestaje być zagrożeniem, a staje się bezpieczny i prawdziwie użyteczny. Otrzymujemy system typów, który rozróżnia, co jest nullowalne a co nie. Zmienne nullowanye muszą być używane bezpiecznie; do tego możemy użyć bezpiecznych wywołań, asercji not-null, smart-castingu czy operatora Elvisa. Teraz przejdźmy wreszcie do klas. Używaliśmy ich już wiele razy, ale dopiero teraz mamy wszystko, czego potrzebujemy, aby dobrze je omówić.

[^08_0]: Niektóre badania to potwierdzają: na przykład dane zebrane przez OverOps potwierdzają, że `NullPointerException` jest najczęstszym wyjątkiem w 70% projektów.
[^08_1]: Zobacz artykuł "Null is your friend, not a mistake" (link https://kt.academy/l/re-null) autorstwa Romana Elizarova, obecnego kierownika zespołu tworzącego język Kotlin.
[^08_2]: Więcej informacji na temat stosowania nullowalności znajdziesz w książce *Efektywny Kotlin*.
[^08_3]: Są to funkcje rozszerzające, które omówimy w rozdziale *Rozszerzenia*.
[^08_4]: Więcej informacji na temat wyjątków `IllegalArgumentException` i `IllegalStateException` znajdziesz w rozdziale *Wyjątki*.
[^08_5]: Aby odnieść się do właściwości innego obiektu, musimy zacząć od obiektu, zanim użyjemy `::` i nazwy właściwości. Więcej informacji na temat referencji do właściwości znajdziesz w książce *Zaawansowany Kotlin*.
