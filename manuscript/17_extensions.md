## Rozszerzenia

Najbardziej intuicyjnym sposobem definiowania metod i właściwości jest umieszczanie ich wewnątrz klas. Takie elementy nazywane są **elementami klasy**.

```kotlin
class Telephone(
    // właściwość klasy
    val number: String
) {
    // funkcja klasy
    fun call() {
        print("Dzwonię pod $number")
    }
}

fun main() {
    // Użycie
    val telephone = Telephone("123456789")
    println(telephone.number) // 123456789
    telephone.call() // Dzwonię pod 123456789
}
```

Z drugiej strony, Kotlin daje jeszcze jeden sposób definiowania funkcji i właściwości, które są wywoływane na instancji: rozszerzenia. **Funkcje rozszerzające** są definiowane jak zwykłe funkcje, ale dodatkowo określają typ rozszerzany (poprzez nazwę typu i kropkę przed nazwą funkcji). W poniższym przykładzie funkcja `dzwon` jest zdefiniowana jako funkcja rozszerzenia dla `Telefon`, więc musi być wywoływana na instancji tego typu.

```kotlin
class Telephone(
    val numer: String
)

fun Telephone.call() {
    print("Dzwonię pod $numer")
}

fun main() {
    // Użycie
    val telephone = Telephone("123456789")
    telephone.call() // Dzwonię pod 123456789
}
```

> Zarówno funkcje elementów, jak i funkcje rozszerzające nazywane są metodami.

Funkcje rozszerzające można definiować na typach, których nie definiujemy sami, na przykład `String`. Daje nam to możliwość rozszerzania zewnętrznych interfejsów API o nasze własne funkcje.

```kotlin
fun String.remove(value: String) = this.replace(value, "")

fun main() {
    println("Who Framed Roger Rabbit?".remove(" "))
    // WhoFramedRogerRabbit?
}
```

Spójrz na powyższy przykład. Zdefiniowaliśmy funkcję rozszerzającą `remove` na `String`, więc musimy wywołać tę funkcję na obiekcie typu `String`. Wewnątrz funkcji odwołujemy się do tego obiektu za pomocą słowa kluczowego `this`, tak jak wewnątrz funkcji klasy. Słowo kluczowe `this` można również używać niejawnie.

```kotlin
// jawne this
fun String.remove(value: String) = this.replace(value, "")

// niejawne this
fun String.remove(value: String) = replace(value, "")
```

Słowo kluczowe `this` jest znane jako **receiver**. Typ, który rozszerzamy za pomocą funkcji rozszerzenia, nazywany jest **typem receivera** (**receiver type**).

![](207_receiver_type.png)

Funkcje rozszerzające zachowują się bardzo podobnie do funkcji z klas. Kiedy deweloperzy się o nich uczą, często martwią się o bezpieczeństwo obiektów, ale to nie jest problem, ponieważ rozszerzenia nie mają żadnego specjalnego dostępu do elementów klasy. Jedyna różnica między funkcjami rozszerzającymi a zwykłymi funkcjami z dodatkowym parametrem zamiast receivera polega na tym, że są one wywoływane "na" instancji, zamiast z instancją jako standardowym argumentem. Aby zrozumieć to lepiej, przyjrzyjmy się bliżej funkcjom rozszerzającym.

### Funkcje rozszerzające pod maską

Aby zrozumieć funkcje rozszerzające, użyjmy ponownie narzędzia "Tools > Kotlin > Show Kotlin Bytecode" i przycisku "Decompile" (jak wyjaśniłem w rozdziale *Twój pierwszy program w Kotlinie* w sekcji *Co kryje się pod maską na JVM?*). Skompilujemy i zdekompilujemy do Java naszą definicję funkcji `remove` oraz jej wywołanie:

```kotlin
fun String.remove(value: String) = this.replace(value, "")

fun main() {
    println("A B C".remove(" ")) // ABC
}
```

W rezultacie powinieneś zobaczyć następujący kod:

```java
public final class PlaygroundKt {
    @NotNull
    public static final String remove(
            @NotNull String $this$remove,
            @NotNull String value
    ) {
        // sprawdzenie, czy parametry nie są nullami
        return StringsKt.replace$default(
                $this$remove,
                value,
                ""
                // oraz wartości domyślne
        );
    }

    public static final void main(@NotNull String[] args) {
        // sprawdzenie, czy parametr nie jest nullem
        String var1 = remove("A B C", " ");
        System.out.println(var1);
    }
}
```

Zauważ, co stało się z typem odbiorcy: stał się parametrem. Możesz także zobaczyć, że pod maską `remove` nie jest wywoływane na obiekcie. To po prostu zwykła statyczna funkcja.

Definiując funkcję rozszerzenia, naprawdę nie dodajesz nic do klasy. To tylko cukier składniowy. Porównajmy dwie następujące implementacje funkcji `remove`.

```kotlin
fun remove(text: String, value: String) =
    text.replace(value, "")

fun String.remove(value: String) =
    this.replace(value, "")
```

Pod maską są niemal identyczne. Różnica polega na tym, jak się je wywołuje w Kotlinie. Zwykłe funkcje otrzymują wszystkie swoje argumenty na zwykłych pozycjach argumentów. Funkcje rozszerzające są wywoływane "na" wartości.

### Właściwości rozszerzające

Rozszerzenie nie może przechowywać stanu, więc nie może mieć pól. Ale przecież właściwości nie potrzebują pól, mogą być definiowane przez gettery i settery, czyli akcesory. Dlatego możemy definiować właściwości rozszerzające, jeśli nie potrzebują one pola i są w zupełności definiowane przez akcesory.

```kotlin
val <T> List<T>.lastIndex: Int
    get() = size - 1
```

Właściwości rozszerzające są bardzo popularne w Androidzie, gdzie dostęp do różnych usług jest jednocześnie powtarzalny i skomplikowany. Definiowanie właściwości rozszerzających pozwala nam zaoszczędzić sporo pracy.

```kotlin
val Context.inflater: LayoutInflater
    get() = getSystemService(Context.LAYOUT_INFLATER_SERVICE)
        as LayoutInflater

val Context.notificationManager: NotificationManager
    get() = getSystemService(Context.NOTIFICATION_SERVICE)
        as NotificationManager

val Context.alarmManager: AlarmManager
    get() = getSystemService(Context.ALARM_SERVICE)
        as AlarmManager
```

Właściwości rozszerzające mogą definiować zarówno gettera, jak i settera. Oto właściwość rozszerzenia, która dostarcza inną reprezentację daty urodzenia użytkownika:

```kotlin
class User {
    // ...
    var birthdateMillis: Long? = null
}

var User.birthdate: Date?
    get() = birthdateMillis?.let(::Date)
    set(value) {
        birthdateMillis = value?.time
    }
```

### Rozszerzenia kontra elementy klasy

Największa różnica między elementami klasy a rozszerzeniami z punktu widzenia użytkownika polega na tym, że **rozszerzenia muszą być importowane**. Z tego powodu mogą być umieszczone w dowolnym pakiecie czy nawet w innym module niż ten, w którym zdefiniowany jest rozszerzany typ. Ten fakt jest wykorzystywany, gdy nie mamy kontroli nad typem, do którego chcemy dodać funkcję lub właściwość. Jest również wykorzystywany w projektach mających na celu oddzielenie danych i zachowań. Właściwości z polami muszą być umieszczone w klasie, ale metody można umieścić oddzielnie, o ile mają dostęp do publicznego API klasy.

Dzięki temu, że rozszerzenia muszą być importowane, możemy mieć wiele rozszerzeń o tej samej nazwie dla tego samego typu. To dobrze, ponieważ różne biblioteki mogą dostarczać dodatkowe metody bez powodowania konfliktów. Z drugiej strony, niebezpieczne byłoby posiadanie dwóch rozszerzeń o tej samej nazwie, ale o różnych zachowaniach. Jeśli widzisz taką sytuację, jest to *code smell* i wskazówka, że ktoś nadużył możliwości funkcji rozszerzających.

Inną istotną różnicą jest to, że **rozszerzenia nie są wirtualne**, co oznacza, że nie mogą być zredefiniowane w klasach pochodnych. Co za tym idzie, jeśli masz rozszerzenie zdefiniowane zarówno dla nadtypu, jak i podtypu, kompilator decyduje, która funkcja jest wybierana na podstawie tego, jak zmienna jest typowana, a nie jaka jest jej rzeczywista klasa.

```kotlin
open class View
class Button : View()

fun View.printMe() {
    println("Jestem widokiem")
}

fun Button.printMe() {
    println("Jestem przyciskiem")
}

fun main() {
    val button: Button = Button()
    button.printMe() // Jestem przyciskiem
    val view: View = button
    view.printMe() // Jestem widokiem
}
```

Zachowanie funkcji rozszerzenia różni się od funkcji elementów. Funkcje elementów są *wirtualne*, więc rzutowanie w górę typu obiektu nie wpływa na wybór funkcji klasy.

```kotlin
open class View {
   open fun printMe() {
       println("Jestem widokiem")
   }
}
class Button: View() {
   override fun printMe() {
       println("Jestem przyciskiem")
   }
}

fun main() {
   val button: Button = Button()
   button.printMe() // Jestem przyciskiem
   val view: View = button
   view.printMe() // Jestem przyciskiem
}
```

To zachowanie jest wynikiem tego, że funkcje rozszerzające są kompilowane pod maską do normalnych funkcji, w których odbiorca funkcji rozszerzającej jest umieszczony jako pierwszy argument:

```kotlin
open class View
class Button : View()

fun printMe(view: View) {
    println("Jestem widokiem")
}

fun printMe(button: Button) {
    println("Jestem przyciskiem")
}

fun main() {
    val button: Button = Button()
    printMe(button) // Jestem przyciskiem
    val view: View = button
    printMe(view) // Jestem widokiem
}
```

Kolejną konsekwencją tego, czym są rozszerzenia, jest to, że **definiujemy rozszerzenia dla typów, a nie dla klas**. Daje nam to większą swobodę. Na przykład możemy zdefiniować rozszerzenie dla typów nullowalnych lub generycznych:

```kotlin
inline fun CharSequence?.isNullOrBlank(): Boolean {
    contract {
        returns(false) implies (this@isNullOrBlank != null)
    }

    return this == null || this.isBlank()
}

fun Iterable<Int>.sum(): Int {
    var sum: Int = 0
    for (element in this) {
        sum += element
    }
    return sum
}
```

Ostatnia ważna różnica polega na tym, że **rozszerzenia nie są wymieniane jako elementy w referencji do klasy**. To powoduje, że nie są uwzględniane przez procesory adnotacji; dlatego też, gdy przetwarzamy klasę za pomocą przetwarzania adnotacji, nie możemy wyodrębnić elementów, które powinny być przetworzone w rozszerzeniach. Z drugiej strony, jeśli wyodrębnimy elementy nieistotne jako rozszerzenia, nie musimy się martwić, że zostaną one zauważone przez te procesory. Nie musimy ich ukrywać, ponieważ i tak nie są w klasie, którą rozszerzają.

### Funkcje rozszerzeń dla deklaracji obiektów

Możemy zdefiniować rozszerzenia dla deklaracji obiektów.

```kotlin
object A

fun A.foo() {}

fun main() {
    A.foo()

    val a: A = A
    a.foo()
}
```

Aby zdefiniować funkcję rozszerzenia dla companion obiektu, musimy użyć rzeczywistej nazwy tego obiektu. Jeśli ta nazwa nie jest ustawiona jawnie, domyślną jest "Companion". Aby zdefiniować funkcję rozszerzającą companion obiekt, taki obiekt musi istnieć. Dlatego niektóre klasy definiują companion obiekty bez ciał.

```kotlin
class A {
    companion object
}

fun A.Companion.foo() {}

fun main() {
    A.foo()

    val a: A.Companion = A
    a.foo()
}
```

### Funkcje rozszerzające zdefiniowane wewnątrz klas

Możliwe jest definiowanie funkcji rozszerzających wewnątrz klas.

```kotlin
class Telephone {
    fun String.call() { 
        // ...
    }
}
```

Tego typu funkcje rozszerzające są uważane za złą praktykę i powinniśmy unikać ich stosowania, jeśli nie mamy ku temu dobrego powodu. Aby uzyskać bardziej szczegółowe wyjaśnienie, zobacz [*Effective Kotlin*, *Temat 46: Unikaj definiowania funkcji rozszerzających wewnątrz klas*](kt.academy/article/ek-member-extensions).

### Przypadki użycia

Najważniejszym zastosowaniem rozszerzeń jest dodawanie metod i właściwości do API, nad którymi nie mamy kontroli. Dobrym przykładem jest wyświetlanie toasta lub ukrywanie widoku w systemie Android. Obydwie te operacje są niepotrzebnie skomplikowane, więc lubimy definiować rozszerzenia, aby je uprościć.

```kotlin
fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun View.hide() {
    this.visibility = View.GONE
}
```

Jednak istnieją także przypadki, gdy wolimy używać rozszerzeń zamiast definiowania elementów w klasie. Weźmy pod uwagę interfejs `Iterable`, który zawiera tylko jedną funkcję, `iterator`.  Ma za to wiele metod, które są zdefiniowane w bibliotece standardowej jako rozszerzenia[^16_1], takie jak `onEach` czy `joinToString`. Fakt, że są one tak zdefiniowane, pozwala zachować minimalistyczny, zwięzły interfejs. Ma to sens, ponieważ `onEach` czy `joinToString` nie są esencjonalną częścią interfejsu `Iterable`, ale są raczej pewnymi narzędziami, które mogą być używane z każdym iterowalnym typem.

```kotlin
interface Iterable<out T> {
    operator fun iterator(): Iterator<T>
}
```

![](iterable_methods.png)

Funkcje rozszerzeń są również bardziej elastyczne niż zwykłe funkcje. Wynika to głównie z faktu, że są one definiowane dla typów, więc możemy definiować rozszerzenia dla typów takich jak `Iterable<Int>` czy `Iterable<T>`.

```kotlin
fun <T : Comparable<T>> Iterable<T>.sorted(): List<T> {
    if (this is Collection) {
        if (size <= 1) return this.toList()
        @Suppress("UNCHECKED_CAST")
        return (toTypedArray<Comparable<T>>() as Array<T>)
            .apply { sort() }
            .asList()
    }
    return toMutableList().apply { sort() }
}

fun Iterable<Int>.sum(): Int {
    var sum: Int = 0
    for (element in this) {
        sum += element
    }
    return sum
}
```

W większych projektach często mamy podobne klasy dla różnych części naszej aplikacji. Załóżmy, że implementujesz backend dla sklepu internetowego i masz klasę `Product`, która reprezentuje wszystkie produkty.

```kotlin
import java.math.BigDecimal

class Product(
    val id: String,
    val title: String,
    val imgSrc: String,
    val description: String,
    val price: BigDecimal,
    val type: ProductType,
    // ...
)
```

Masz również podobną (ale nie identyczną) klasę o nazwie `ProductJson`, która jest używana do reprezentowania obiektów, które używasz w odpowiedziach API swojej aplikacji lub które odczytujesz z żądań API.

```kotlin
class ProductJson(
    val id: String,
    val title: String,
    val img: String,
    val desc: String,
    val price: String,
    val type: String,
    // ...
)
```

Instancje `Product` są używane w twojej aplikacji, a instancje `ProductJson` są używane w API. Te obiekty muszą być oddzielone, ponieważ zdecydowałeś wcześniej, że nie chcesz zmieniać odpowiedzi API, gdy zmieniasz nazwę właściwości w klasie wewnętrznej. Często jednak musimy przekształcać pomiędzy `Product` a `ProductJson`. W tym celu możemy zdefiniować funkcję klasy `toProduct`.

```kotlin
class ProductJson(
    val id: String,
    val title: String,
    val img: String,
    val desc: String,
    val price: String,
    val type: String,
    // ...
) {
    fun toProduct() = Product(
        id = this.id,
        title = this.title,
        imgSrc = this.img,
        description = this.desc,
        price = BigDecimal(price),
        type = enumValueOf<ProductType>(this.type)
    )
}
```

Nie każdemu spodoba się to rozwiązanie, ponieważ sprawia, że `ProductJson` jest większy i bardziej skomplikowany. Nie jest to również przydatne przy przekształcaniu z `Product` na `ProductJson`, ponieważ w większości nowoczesnych architektur nie chcemy, aby klasy domenowe (takie jak `Product`) były świadome takich szczegółów jak ich reprezentacja API. Lepszym rozwiązaniem jest zdefiniowanie zarówno `toProduct`, jak i `toProductJson` jako funkcji rozszerzających, a następnie umieszczenie ich obok klasy `ProductJson`. Dobrze jest umieścić te funkcje przekształcające obok siebie, ponieważ mają wiele wspólnego.

```kotlin
class ProductJson(
    val id: String,
    val title: String,
    val img: String,
    val desc: String,
    val price: String,
    val type: String,
    // ...
)

fun ProductJson.toProduct() = Product(
    id = this.id,
    title = this.title,
    imgSrc = this.img,
    description = this.desc,
    price = BigDecimal(this.price),
    type = enumValueOf<ProductType>(this.type)
)

fun Product.toProductJson() = ProductJson(
    id = this.id,
    title = this.title,
    img = this.imgSrc,
    desc = this.description,
    price = this.price.toString(),
    type = this.type.name
)
```

Wygląda na to, że jest to popularny wzorzec zarówno na backendzie, jak i w Androidzie.

### Podsumowanie

W tym rozdziale poznaliśmy rozszerzenia - potężną funkcjonalność Kotlina, która często jest używana do tworzenia wygodnych i przydatnych narzędzi oraz do lepszej kontroli naszego kodu. Jednak wraz z wielką mocą przychodzi wielka odpowiedzialność. Nie powinniśmy się obawiać używania rozszerzeń, ale powinniśmy używać ich świadomie i tylko tam, gdzie mają sens.

W następnym rozdziale w końcu przedstawimy kolekcje, a więc omówimy listy, sety, mapy i tablice.

[^16_1]: Roman Elizarov (obecny leader zespołu rozwijającego język Kotlin) określa to pojęciem extension-oriented design. Źródło: elizarov.medium.com/extension-oriented-design-13f4f27deaee
