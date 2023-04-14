## Obiekty

Czym jest obiekt? To pytanie, którym często zaczynam tę sekcję na moich warsztatach, i zazwyczaj dostaję natychmiastową odpowiedź: "Instancją klasy". To prawda, ale jak tworzymy obiekty? Jednym ze sposobów jest proste użycie konstruktorów.

```kotlin
class A

// Użycie konstruktora do stworzenia obiektu
val a = A()
```

Jednak to nie jedyny sposób. W Kotlinie możemy również tworzyć obiekty za pomocą **wyrażeń obiektów** oraz **deklaracji obiektów**. Omówmy te dwie opcje.

### Wyrażenia obiektów

Aby utworzyć pusty obiekt za pomocą wyrażenia, używamy słowa kluczowego `object` i nawiasów klamrowych. Ta składnia tworzenia obiektów nazywana jest *wyrażeniem obiektu*.

```kotlin
val instance = object {}
```

Pusty obiekt nie rozszerza żadnych klas (oprócz `Any`, które jest rozszerzane przez wszystkie obiekty w Kotlinie), nie implementuje żadnych interfejsów i nie ma niczego w swoim ciele. Mimo to jest przydatny. Jego moc tkwi w unikalności: taki obiekt równa się tylko sobie. Dlatego doskonale nadaje się do użycia jako rodzaj tokena lub blokady synchronizacji.

```kotlin
class Box {
    var value: Any? = NOT_SET

    fun initialized() = value != NOT_SET

    companion object {
        private val NOT_SET = object {}
    }
}

private val LOCK = object {}
fun synchronizedOperation() = synchronized(LOCK) {
    // ...
}
```

Pusty obiekt można również utworzyć za pomocą konstruktora `Any`, więc `Any()` jest alternatywą dla `object {}`.

```kotlin
private val NOT_SET = Any()
```

Jednak obiekty utworzone za pomocą wyrażeń obiektów nie muszą być puste. Mogą mieć ciała, rozszerzać klasy, implementować interfejsy itp. Składnia jest taka sama jak dla klas, ale deklaracje obiektów używają słowa kluczowego `object` zamiast `class` i nie powinny definiować nazwy ani konstruktora.

```kotlin
data class User(val name: String)

interface UserProducer {
    fun produce(): User
}

fun printUser(producer: UserProducer) {
    println(producer.produce())
}

fun main() {
    val user = User("Jake")
    val producer = object : UserProducer {
        override fun produce(): User = user
    }
    printUser(producer) // User(name=Jake)
}
```

W lokalnym zakresie wyrażenia obiektów definiują anonimowy typ, który nie będzie działać poza klasą, w której został zdefiniowany. Oznacza to, że nieodziedziczone składniki wyrażeń obiektów są dostępne tylko wtedy, gdy anonimowy obiekt jest deklarowany w lokalnym zakresie lub w zakresie prywatnym klasy; w przeciwnym razie obiekt jest jedynie nieprzezroczystym typem `Any` lub typem klasy lub interfejsu, od którego dziedziczy. Sprawia to, że nieodziedziczone składniki wyrażeń obiektów są trudne do wykorzystania w rzeczywistych projektach.

```kotlin
class Robot {
    // Możliwe, ale rzadko przydatne
    // zamiast tego preferuj regularne właściwości składowe
    private val point = object {
        var x = 0
        var y = 0
    }

    fun moveUp() {
        point.y += 10
    }

    fun show() {
        println("(${point.x}, ${point.y})")
    }
}

fun main() {
    val robot = Robot()
    robot.show() // (0, 0)
    robot.moveUp()
    robot.show() // (0, 10)

    val point = object {
        var x = 0
        var y = 0
    }
    println(point.x) // 0
    point.y = 10
    println(point.y) // 10
}
```

W praktyce wyrażenia obiektów są używane jako alternatywa dla anonimowych klas Java, tj. gdy musimy utworzyć obserwatora lub słuchacza z wieloma metodami obsługi.

```kotlin
taskNameView.addTextChangedListener(object : TextWatcher {
    override fun afterTextChanged(
        editable: Editable?
    ) {
        //...
    }

    override fun beforeTextChanged(
        text: CharSequence?,
        start: Int,
        count: Int,
        after: Int
    ) {
        //...
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        before: Int,
        count: Int
    ) {
        //...
    }
})
```

Zauważ, że "wyrażenie obiektu" to lepsza nazwa niż "anonimowa klasa", ponieważ jest to wyrażenie, które generuje obiekt.

### Deklaracja obiektu

Jeśli weźmiemy wyrażenie obiektu i nadamy mu nazwę, otrzymamy **deklarację obiektu**. Ta struktura również tworzy pojedynczy obiekt, ale obiekt ten nie jest anonimowy: ma nazwę, która może być użyta do odwołania się do niego.

```kotlin
object Point {
    var x = 0
    var y = 0
}

fun main() {
    println(Point.x) // 0
    Point.y = 10
    println(Point.y) // 10

    val p = Point
    p.x = 20
    println(Point.x) // 20
    println(Point.y) // 10
}
```

Deklaracja obiektu to implementacja wzorca singleton[^12_4], więc ta deklaracja tworzy klasę z pojedynczą instancją. Kiedykolwiek chcemy użyć tej klasy, musimy działać na tej pojedynczej instancji. Deklaracje obiektów obsługują wszystkie funkcje, które obsługują klasy; na przykład mogą rozszerzać klasy lub implementować interfejsy.

```kotlin
data class User(val name: String)

interface UserProducer {
    fun produce(): User
}

object FakeUserProducer : UserProducer {
    override fun produce(): User = User("fake")
}

fun setUserProducer(producer: UserProducer) {
    println(producer.produce())
}

fun main() {
    setUserProducer(FakeUserProducer) // User(name=fake)
}
```

### Obiekty towarzyszące

Kiedy wspominam czasy, gdy pracowałem jako programista Java, pamiętam dyskusje na temat tego, jakie funkcje powinny być wprowadzone do tego języka. Często słyszałem pomysł wprowadzenia dziedziczenia dla elementów statycznych. W końcu dziedziczenie jest bardzo ważne w Javie, więc dlaczego nie można go użyć dla elementów statycznych? Kotlin rozwiązał ten problem za pomocą obiektów towarzyszących; jednak, aby to było możliwe, musiał najpierw zlikwidować rzeczywiste elementy statyczne, tj. elementy, które są wywoływane na klasach, a nie na obiektach.

```
// Java
class User {
   // Definicja elementu statycznego
   public static User empty() {
       return new User();
   }
}

// Użycie elementu statycznego
User user = User.empty()
```

Tak, w Kotlinie nie mamy elementów statycznych, ale nie potrzebujemy ich, ponieważ używamy zamiast tego deklaracji obiektów. Jeśli zdefiniujemy deklarację obiektu w klasie, jest ona domyślnie statyczna (podobnie jak klasy zdefiniowane wewnątrz klas), więc możemy bezpośrednio wywołać jej elementy.

```kotlin
// Kotlin
class User {
    object Producer {
        fun empty() = User()
    }
}

// Użycie
val user: User = User.Producer.empty()
```

To nie jest tak wygodne jak elementy statyczne, ale możemy to poprawić. Jeśli użyjemy słowa kluczowego `companion` przed deklaracją obiektu zdefiniowaną w klasie, wówczas możemy wywoływać te metody obiektu niejawnie "w klasie".

```kotlin
class User {
    companion object Producer {
        fun empty() = User()
    }
}

// Użycie
val user: User = User.empty()
// lub
val user: User = User.Producer.empty()
```

Obiekty z modyfikatorem `companion`, znane również jako obiekty towarzyszące, nie muszą mieć wyraźnej nazwy. Ich domyślna nazwa to `Companion`.

```kotlin
class User {
    companion object {
        fun empty() = User()
    }
}

// Użycie
val user: User = User.empty()
// lub
val user: User = User.Companion.empty()
```

W ten sposób osiągnęliśmy składnię, która jest prawie tak wygodna jak elementy statyczne. Jedynym niedogodnością jest to, że musimy umieścić wszystkie "statyczne" elementy wewnątrz pojedynczego obiektu (w klasie może być tylko jeden obiekt towarzyszący). Jest to ograniczenie, ale mamy coś w zamian: obiekty towarzyszące to obiekty, więc mogą rozszerzać klasy lub implementować interfejsy.

Pozwól, że pokażę Ci przykład. Powiedzmy, że reprezentujesz pieniądze w różnych walutach za pomocą różnych klas, takich jak `USD`, `EUR` czy `PLN`. Dla wygody każda z nich definiuje funkcje konstruujące `from`, które upraszczają tworzenie obiektów.

```kotlin
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode.HALF_EVEN

abstract class Money(
    val amount: BigDecimal,
    val currency: String
)

class USD(amount: BigDecimal) : Money(amount, "USD") {
    companion object {
        private val MATH = MathContext(2, HALF_EVEN)
        fun from(amount: Int): USD =
            USD(amount.toBigDecimal(MATH))
        fun from(amount: Double): USD =
            USD(amount.toBigDecimal(MATH))

        @Throws(NumberFormatException::class)
        fun from(amount: String): USD =
            USD(amount.toBigDecimal(MATH))
    }
}

class EUR(amount: BigDecimal) : Money(amount, "EUR") {
    companion object {
        private val MATH = MathContext(2, HALF_EVEN)
        fun from(amount: Int): EUR =
            EUR(amount.toBigDecimal(MATH))
        fun from(amount: Double): EUR =
            EUR(amount.toBigDecimal(MATH))

        @Throws(NumberFormatException::class)
        fun from(amount: String): EUR =
            EUR(amount.toBigDecimal(MATH))
    }
}

class PLN(amount: BigDecimal) : Money(amount, "PLN") {
    companion object {
        private val MATH = MathContext(2, HALF_EVEN)
        fun from(amount: Int): PLN =
            PLN(amount.toBigDecimal(MATH))
        fun from(amount: Double): PLN =
            PLN(amount.toBigDecimal(MATH))

        @Throws(NumberFormatException::class)
        fun from(amount: String): PLN =
            PLN(amount.toBigDecimal(MATH))
    }
}

fun main() {
    val eur: EUR = EUR.from("12.00")
    val pln: PLN = PLN.from(20)
    val usd: USD = USD.from(32.5)
}
```

Powtarzające się funkcje tworzenia obiektów z różnych typów można wyodrębnić do abstrakcyjnej klasy `MoneyMaker`, którą można rozszerzyć o obiekty towarzyszące różnych walut. Ta klasa może oferować szereg metod do tworzenia waluty. W ten sposób wykorzystujemy dziedziczenie obiektów towarzyszących do wyodrębnienia wzorca, który jest wspólny dla wszystkich obiektów towarzyszących klas reprezentujących pieniądze.

```kotlin
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode.HALF_EVEN

abstract class Money(
    val amount: BigDecimal,
    val currency: String
)

abstract class MoneyMaker<Currency : Money> {
    private val MATH = MathContext(2, HALF_EVEN)
    abstract fun from(amount: BigDecimal): Currency
    fun from(amount: Int): Currency =
        from(amount.toBigDecimal(MATH))
    fun from(amount: Double): Currency =
        from(amount.toBigDecimal(MATH))

    @Throws(NumberFormatException::class)
    fun from(amount: String): Currency =
        from(amount.toBigDecimal(MATH))
}

class USD(amount: BigDecimal) : Money(amount, "USD") {
    companion object : MoneyMaker<USD>() {
        override fun from(amount: BigDecimal): USD =
            USD(amount)
    }
}

class EUR(amount: BigDecimal) : Money(amount, "EUR") {
    companion object : MoneyMaker<EUR>() {
        override fun from(amount: BigDecimal): EUR =
            EUR(amount)
    }
}

class PLN(amount: BigDecimal) : Money(amount, "PLN") {
    companion object : MoneyMaker<PLN>() {
        override fun from(amount: BigDecimal): PLN =
            PLN(amount)
    }
}

fun main() {
    val eur: EUR = EUR.from("12.00")
    val pln: PLN = PLN.from(20)
    val usd: USD = USD.from(32.5)
}
```

Nasza społeczność nadal uczy się korzystać z tych możliwości, ale już teraz można znaleźć wiele przykładów w projektach i bibliotekach. Oto kilka interesujących przykładów[^12_6]:

```kotlin
// Korzystanie z dziedziczenia obiektów towarzyszących do rejestrowania
// z ramki Kotlin Logging
class FooWithLogging {
    fun bar(item: Item) {
        logger.info { "Przedmiot $item" }
        // Logger pochodzi z obiektu towarzyszącego
    }

    companion object : KLogging()
    // towarzyszący dziedziczy właściwość logger
}
```

```kotlin
// Przykład specyficzny dla Androida dotyczący użycia abstrakcyjnej fabryki
// dla obiektu towarzyszącego
class MainActivity : Activity() {
    //...

    // Używanie obiektu towarzyszącego jako fabryki
    companion object : ActivityFactory() {
        override fun getIntent(context: Context): Intent =
            Intent(context, MainActivity::class.java)
    }
}

abstract class ActivityFactory {
    abstract fun getIntent(context: Context): Intent

    fun start(context: Context) {
        val intent = getIntent(context)
        context.startActivity(intent)
    }

    fun startForResult(activity: Activity, requestCode: Int) {
        val intent = getIntent(activity)
        activity.startActivityForResult(intent, requestCode)
    }
}

// Użycie wszystkich elementów towarzyszących fabryki Activity
val intent = MainActivity.getIntent(context)
MainActivity.start(context)
MainActivity.startForResult(activity, requestCode)

// W kontekstach na Kotlin Coroutines obiekty towarzyszące są
// używane jako klucze do identyfikowania kontekstów
data class CoroutineName(
    val name: String
) : AbstractCoroutineContextElement(CoroutineName) {

    // Obiekt towarzyszący to klucz
    companion object Key : CoroutineContext.Key<CoroutineName>

    override fun toString(): String = "CoroutineName($name)"
}

// Wyszukiwanie kontekstu według klucza
val name1 = context[CoroutineName] // Tak, to jest towarzyszący

// Można również odwoływać się do obiektów towarzyszących przez jego nazwę
val name2 = context[CoroutineName.Key]
```

### Deklaracje obiektów danych

Od Kotlin 1.8 można używać modyfikatora `data` dla deklaracji obiektów. Generuje on metodę `toString` dla obiektu; ta metoda obejmuje nazwę obiektu jako łańcuch znaków.

```kotlin
data object ABC

fun main() {
    println(ABC) // ABC
}
```

### Stałe wartości

Powszechną praktyką jest generalne wyodrębnianie stałych wartości jako właściwości obiektów towarzyszących i nazywanie ich używając UPPER_SNAKE_CASE[^12_5]. W ten sposób nazywamy te wartości i upraszczamy ich zmiany w przyszłości. Nadajemy stałym wartościom charakterystyczne nazwy, aby było jasne, że reprezentują stałą[^12_2].

```kotlin
class Product(
    val code: String,
    val price: Double,
) {
    init {
        require(price > MIN_AMOUNT)
    }

    companion object {
        val MIN_AMOUNT = 5.00
    }
}
```

Gdy właściwości obiektów towarzyszących lub właściwości na najwyższym poziomie reprezentują stałą wartość (znaną w czasie kompilacji) będącą albo wartością pierwotną, albo `String`[^12_3], możemy dodać modyfikator `const`. Jest to optymalizacja. Wszystkie użycia takich zmiennych zostaną zastąpione ich wartościami w czasie kompilacji.

```kotlin
class Product(
    val code: String,
    val price: Double,
) {
    init {
        require(price > MIN_AMOUNT)
    }

    companion object {
        const val MIN_AMOUNT = 5.00
    }
}
```

Takie właściwości można również używać w adnotacjach:

```kotlin
private const val OUTDATED_API: String =
    "To jest część przestarzałego API."

@Deprecated(OUTDATED_API)
fun foo() {
    ...
}

@Deprecated(OUTDATED_API)
fun boo() {
    ...
}
```

### Podsumowanie

W tym rozdziale dowiedzieliśmy się, że obiekty można tworzyć nie tylko z klas, ale także za pomocą wyrażeń obiektów i deklaracji obiektów. Obie te formy obiektów mają praktyczne zastosowania. Wyrażenie obiektu jest używane jako alternatywa dla anonimowych obiektów Java, ale oferuje więcej. Deklaracja obiektu to implementacja wzorca singleton w Kotlinie. Specjalna forma deklaracji obiektu, znana jako obiekt towarzyszący, jest używana jako alternatywa dla elementów statycznych, ale z dodatkowym wsparciem dla dziedziczenia. Mamy również modyfikator `const`, który oferuje lepsze wsparcie dla stałych elementów zdefiniowanych na najwyższym poziomie lub w deklaracjach obiektów.

W poprzednim rozdziale omówiliśmy klasy danych, ale w Kotlinie używamy innych modyfikatorów dla klas. W następnym rozdziale poznamy kolejny ważny rodzaj klas: wyjątki.

[^12_2]: Ta praktyka jest lepiej opisana w *Effective Kotlin*, *Pozycja 27: Użyj abstrakcji, aby chronić kod przed zmianami*.
[^12_3]: Akceptowane typy to `Int`, `Long`, `Double`, `Float`, `Short`, `Byte`, `Boolean`, `Char` i `String`.
[^12_4]: Wzorzec programowania, w którym klasa jest implementowana tak, aby mogła mieć tylko jedną instancję.
[^12_5]: UPPER_SNAKE_CASE to konwencja nazewnictwa, w której każdy znak jest pisany wielką literą, a słowa oddzielamy podkreśleniem, jak w nazwie UPPER_SNAKE_CASE. Użycie go dla stałych jest sugerowane w dokumentacji Kotlin w sekcji *Kotlin Coding Convention*.
[^12_6]: Nie traktuj ich jako najlepszych praktyk, ale raczej jako przykłady tego, co można zrobić z faktem, że obiekty towarzyszące mogą dziedziczyć po klasach i implementować interfejsy.
