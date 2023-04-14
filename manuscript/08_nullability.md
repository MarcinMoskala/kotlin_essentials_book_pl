## Możliwość wystąpienia wartości null

Kotlinie powstał jako rozwiązanie problemów Javy, a największym problemem w Javie jest możliwość wystąpienia wartości null. W Javie, jak w wielu innych językach, każda zmienna może przyjmować wartość `null`. Każde wywołanie na wartości `null` prowadzi do słynnego `NullPointerException` (NPE). Jest to wyjątek nr 1 w większości projektów Javie[^08_0]. Jest tak powszechny, że często nazywany jest "błędem wartym miliard dolarów" po słynnym przemówieniu Sir Charlesa Antony'ego Richarda Hoare'a, który powiedział: "Nazywam to moim błędem wartym miliard dolarów. Był to wynalazek null reference w 1965 roku... Doprowadziło to do niezliczonych błędów, podatności i awarii systemów, które przez ostatnie czterdzieści lat prawdopodobnie spowodowały straty rzędu miliarda dolarów".

Jednym z priorytetów Kotlinie było ostateczne rozwiązanie tego problemu, co udało się doskonale. Wprowadzone w Kotlinie mechanizmy są tak skuteczne, że zobaczenie `NullPointerException` wyrzuconego z kodu Kotlinie jest niezwykle rzadkie. Wartość `null` przestała być problemem, a programiści Kotlinie nie obawiają się jej już. Stała się naszym przyjacielem.

Jak zatem działa możliwość wystąpienia wartości null w Kotlin? Wszystko opiera się na kilku zasadach:

1. Każda właściwość musi mieć jawną wartość. Nie ma czegoś takiego jak domyślna wartość `null`.

```kotlin
var person: Person // BŁĄD KOMPILACJI,
// właściwość musi być zainicjalizowana
```

2. Zwykły typ nie akceptuje wartości `null`.

```kotlin
var person: Person = null // BŁĄD KOMPILACJI,
// Person nie jest typem nullable i nie może być `null`
```

3. Aby określić typ nullable, musisz zakończyć zwykły typ znakiem zapytania (`?`).

```kotlin
var person: Person? = null // OK
```

4. Wartości nullable nie mogą być używane bezpośrednio. Muszą być używane bezpiecznie lub rzutowane najpierw (za pomocą jednego z narzędzi przedstawionych później w tym rozdziale).

```kotlin
person.name // BŁĄD KOMPILACJI,
// typ person jest nullable, więc nie możemy go używać bezpośrednio
```

Dzięki wszystkim tym mechanizmom zawsze wiemy, co może być `null`, a co nie. Używamy wartości null tylko wtedy, gdy tego potrzebujemy - gdy istnieje ku temu jakiś powód. W takich przypadkach użytkownicy są zmuszeni do jawnej obsługi wartości null. We wszystkich innych przypadkach nie ma takiej potrzeby. Jest to doskonałe rozwiązanie, ale potrzebne są dobre narzędzia, aby poradzić sobie z nullowaniem w sposób wygodny dla programistów.

Kotlinie obsługuje wiele sposobów użycia wartości nullable, w tym bezpieczne wywołania, asercje not-null, inteligentne rzutowanie czy operator Elvisa. Omówmy je po kolei.

### Bezpieczne wywołania

Najprostszym sposobem wywołania metody lub właściwości na wartości nullable jest bezpieczne wywołanie, które polega na użyciu znaku zapytania i kropki (`?.`) zamiast zwykłej kropki (`.`). Bezpieczne wywołania działają następująco:
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
    user = User("Ciasteczko")
    user?.cheer() // Cześć, mam na imię Ciasteczko
    println(user?.name) // Ciasteczko
}
```

Zauważ, że wynik bezpiecznego wywołania zawsze jest typem nullable, ponieważ bezpieczne wywołanie zwraca `null`, gdy jest wywołane na `null`. Oznacza to, że wartość null propaguje się. Jeśli chcesz dowiedzieć się, jaką długość ma imię użytkownika, wywołanie `user?.name.length` nie zostanie skompilowane. Mimo że `name` nie jest wartością null, wynik `user?.name` to `String?`, więc musimy użyć ponownie bezpiecznego wywołania: `user?.name?.length`.

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
    user = User("Ciasteczko")
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

var user: User? = User("Ciasteczko")

fun main() {
    println(user!!.name.length) // 10
    user = null
    println(user!!.name.length) // rzuca NullPointerException
}
```

To nie jest bardzo bezpieczna opcja, ponieważ jeśli się mylimy i wartość `null` znajduje się tam, gdzie jej nie oczekujemy, prowadzi to do wyjątku `NullPointerException`. Czasami chcemy zgłosić wyjątek, aby upewnić się, że nie ma sytuacji, w których używany jest `null`, ale zazwyczaj wolimy zgłosić bardziej znaczący wyjątek[^08_4]. W tym celu najpopularniejsze opcje to:
- `requireNotNull`, który przyjmuje wartość nullable jako argument i rzuca wyjątek `IllegalArgumentException`, jeśli ta wartość jest równa null. W przeciwnym razie zwraca tę wartość jako non-nullable.
- `checkNotNull`, który przyjmuje wartość nullable jako argument i rzuca wyjątek `IllegalStateException`, jeśli ta wartość jest równa null. W przeciwnym razie zwraca tę wartość jako non-nullable.

```kotlin
fun sendData(dataWrapped: Wrapper<Data>) {
    val data = requireNotNull(dataWrapped.data)
    val polaczenie = checkNotNull(polaczenia["db"])
    polaczenie.send(data)
}
```

### Smart-casting

Smart-casting działa również dla wartości null. W związku z tym, w zakresie sprawdzenia braku wartości null, typ nullable jest rzutowany na typ non-nullable.

```kotlin
fun printLengthIfNotNull(str: String?) {
    if (str != null) {
        println(str.length) // str smart-casted do String
    }
}
```

Smart-casting działa również, gdy używamy `return` lub `throw`, jeśli wartość nie jest `null`.

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

> Dzięki funkcji o nazwie **kontrakty**, która jest wyjaśniona w książce *Advanced Kotlin*, smart-casting działa w powyższym kodzie.

### Operator Elvisa

Ostatnią specjalną funkcją Kotlinie, która jest używana do obsługi wartości null, jest operator Elvisa `?:`. Tak, to pytajnik i dwukropek. Nazywa się go operatorem Elvisa, ponieważ przypomina Elvisa Presleya (z jego charakterystyczną fryzurą), który patrzy na nas zza muru, więc widzimy tylko jego włosy i oczy.

![](elvis.png)

Umieszcza się go pomiędzy dwiema wartościami. Jeśli wartość po lewej stronie operatora Elvisa nie jest `null`, używamy wartości nullable, która wynika z operatora Elvisa. Jeśli lewa strona jest `null`, zwracana jest prawa strona.

```kotlin
fun main() {
    println("A" ?: "B") // A
    println(null ?: "B") // B
    println("A" ?: null) // A
    println(null ?: null) // null
}
```

Możemy użyć operatora Elvisa, aby ustawić wartość domyślną dla wartości nullable.

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

### Rozszerzenia dla typów nullable

Zwykłe funkcje nie mogą być wywoływane na zmiennych nullable. Istnieje jednak specjalny rodzaj funkcji, który można zdefiniować tak, aby można go było wywoływać na zmiennych nullable[^08_3]. Dzięki temu, biblioteka standardowa Kotlinie definiuje następujące funkcje, które można wywoływać na `String?`:
* `orEmpty` zwraca wartość, jeśli nie jest `null`. W przeciwnym razie zwraca pusty ciąg.
* `isNullOrEmpty` zwraca `true`, jeśli wartość jest `null` lub pusta. W przeciwnym razie zwraca `false`.
* `isNullOrBlank` zwraca `true`, jeśli wartość jest `null` lub pusta. W przeciwnym razie zwraca `false`.

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

Biblioteka standardowa Kotlinie zawiera również podobne funkcje dla nullable list:
* `orEmpty` zwraca wartość, jeśli nie jest `null`. W przeciwnym razie zwraca pustą listę.
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

Te funkcje pomagają nam operować na wartościach nullable.

### `null` to nasz przyjaciel

Zmiennność była źródłem bólu w wielu językach, takich jak Javie, gdzie każdy obiekt może być nullable. W efekcie ludzie zaczęli unikać nullability. W rezultacie można znaleźć sugestie takie jak "Pozycja 43. Zwracaj puste tablice lub kolekcje, a nie wartości null" z *Effective Java* 2nd Edition autorstwa Joshua Bloch. Takie praktyki nie mają sensu w Kotlinie, gdzie mamy właściwy system nullability i nie powinniśmy obawiać się wartości `null`. W Kotlinie traktujemy `null` jako naszego przyjaciela, a nie jako błąd[^08_1]. Rozważ funkcję `getUsers`. Istnieje istotna różnica między zwracaniem pustej listy a `null`. Pusta lista powinna być interpretowana jako "wynik to pusta lista użytkowników, ponieważ żadni nie są dostępni". Wynik `null` powinien być interpretowany jako "nie można wyprodukować wyniku, a lista użytkowników pozostaje nieznana". Zapomnij o przestarzałych praktykach dotyczących nullability. Wartość `null` to nasz przyjaciel w Kotlin[^08_2].

### lateinit

Są sytuacje, gdy chcemy zachować typ właściwości jako nie-nullable, ale nie możemy określić jego wartości podczas tworzenia obiektu. Weźmy pod uwagę właściwości, których wartość jest wstrzykiwana przez ramkę wstrzykiwania zależności, albo weźmy pod uwagę właściwości, które są tworzone dla każdego testu w fazie konfiguracji. Uczynienie takich właściwości nullable prowadziłoby do niewygodnego użycia: używałbyś operacji not-null nawet wtedy, gdy wiesz, że wartość nie może być `null`, ponieważ na pewno zostanie zainicjowana przed użyciem. Dla takich sytuacji twórcy Kotlinie wprowadzili właściwość `lateinit`. Takie właściwości mają typy nie-nullable i nie można ich zainicjować podczas tworzenia.

```kotlin
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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

Zawsze możesz sprawdzić, czy właściwość została zainicjowana, używając właściwości `isInitialized` na jej odniesieniu. Aby użyć odniesienia do właściwości, użyj dwóch dwukropków i nazwy właściwości[^08_5].

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

Kotlinie oferuje potężne wsparcie dla nullability, które zamienia nullability z przerażającego i zawiłego na użyteczne i bezpieczne. Jest to obsługiwane przez system typów, który rozróżnia, co jest nullable lub not nullable. Zmienne, które są nullable, muszą być używane bezpiecznie; do tego możemy użyć bezpiecznych wywołań, asercji not-null, smart-casting czy operatora Elvisa. Teraz przejdźmy wreszcie do klas. Używaliśmy ich już wiele razy, ale wreszcie mamy wszystko, czego potrzebujemy, aby dobrze je opisać.

[^08_0]: Niektóre badania to potwierdzają: na przykład dane zebrane przez OverOps potwierdzają, że `NullPointerException` jest najczęstszym wyjątkiem w 70% środowisk produkcyjnych.
[^08_1]: Zobacz artykuł "Null is your friend, not a mistake" (link https://kt.academy/l/re-null) autorstwa Romana Elizarova, obecnego kierownika projektu języka programowania Kotlin.
[^08_2]: Więcej informacji na temat stosowania nullability znajdziesz w książce *Effective Kotlin*.
[^08_3]: Są to funkcje rozszerzeń, które omówimy w rozdziale *Extensions*.
[^08_4]: Więcej informacji na temat wyjątków, `IllegalArgumentException` i `IllegalStateException` znajdziesz w rozdziale *Exceptions*.
[^08_5]: Aby odnieść się do właściwości innego obiektu, musimy zacząć od obiektu, zanim użyjemy `::` i nazwy właściwości. Więcej informacji na temat odnoszenia się do właściwości znajdziesz w zaawansowanej książce o Kotlin.
