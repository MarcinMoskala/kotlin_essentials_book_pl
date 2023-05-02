## Rozszerzenia

Najbardziej intuicyjnym sposobem definiowania metod i właściwości jest umieszczanie ich wewnątrz klas. Takie elementy nazywane są **elementami klasy** lub, bardziej konkretnie, **funkcjami elementów** i **właściwościami elementów**.

```kotlin
class Telephone(
    // właściwość elementu
    val number: String
) {
    // funkcja elementu
    fun call() {
        print("Dzwonię do $numer")
    }
}

fun main() {
    // Użycie
    val telephone = Telephone("123456789")
    println(telephone.number) // 123456789
    telephone.call() // Dzwonię do 123456789
}
```

Z drugiej strony, Kotlin pozwala na inną metodę definiowania funkcji i właściwości, które są wywoływane na instancji: rozszerzenia. **Funkcje rozszerzenia** są definiowane jak zwykłe funkcje, ale mają dodatkowy typ (i kropkę) przed nazwą funkcji. W poniższym przykładzie funkcja `dzwon` jest zdefiniowana jako funkcja rozszerzenia dla `Telefon`, więc musi być wywoływana na instancji tego typu.

```kotlin
class Telephone(
    val number: String
)

fun Telephone.call() {
    print("Dzwonię do $numer")
}

fun main() {
    // Użycie
    val Telephone = telephone("123456789")
    telephone.dzwon() // Dzwonię do 123456789
}
```

> Zarówno funkcje elementów, jak i funkcje rozszerzenia nazywane są metodami.

Funkcje rozszerzenia można definiować na typach, którymi nie zarządzamy, na przykład `String`. Daje nam to możliwość rozszerzania zewnętrznych interfejsów API o nasze własne funkcje.

```kotlin
fun String.remove(value: String) = this.replace(value, "")

fun main() {
    println("Who Framed Roger Rabbit?".remove(" "))
    // WhoFramedRogerRabbit?
}
```

Spójrz na powyższy przykład. Zdefiniowaliśmy funkcję rozszerzenia `usun` na `String`, więc musimy wywołać tę funkcję na obiekcie typu `String`. Wewnątrz funkcji odwołujemy się do tego obiektu za pomocą słowa kluczowego `this`, tak jak wewnątrz funkcji elementów. Słowo kluczowe `this` można również używać niejawnie.

```kotlin
// jawne this
fun String.remove(value: String) = this.replace(value, "")

// niejawne this
fun String.remove(value: String) = replace(value, "")
```

Słowo kluczowe `this` jest znane jako **odbiorca**. Wewnątrz funkcji rozszerzenia nazywamy go **odbiorcą rozszerzenia**. Wewnątrz funkcji elementów nazywamy go **odbiorcą wysyłki**. Typ, który rozszerzamy za pomocą funkcji rozszerzenia, nazywany jest **typem odbiorcy**.

![](207_receiver_type.png)

Funkcje rozszerzenia zachowują się bardzo podobnie do funkcji elementów. Kiedy deweloperzy się o tym uczą, często martwią się o bezpieczeństwo obiektów, ale to nie jest problem, ponieważ rozszerzenia nie mają żadnego specjalnego dostępu do elementów klasy. Jedyna różnica między funkcjami rozszerzenia na najwyższym poziomie a innymi funkcjami na najwyższym poziomie polega na tym, że są one wywoływane na instancji zamiast otrzymywać tę instancję jako zwykły argument. Aby zobaczyć to wyraźniej, przyjrzyjmy się bliżej funkcjom rozszerzenia.

### Funkcje rozszerzenia pod maską

Aby zrozumieć funkcje rozszerzenia, użyjmy ponownie opcji "Narzędzia > Kotlin > Pokaż bajtkod Kotlin" i "Dekompiluj" (jak wyjaśniono w rozdziale *Twój pierwszy program w Kotlin* w sekcji *Co kryje się pod maską na JVM?*). Skompilujemy i zdekompilujemy na Java naszą definicję funkcji `usun` oraz jej wywołanie:

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

Zauważ, co stało się z typem odbiorcy: stał się parametrem. Możesz także zobaczyć, że pod maską `usun` nie jest wywoływane na obiekcie. To po prostu zwykła statyczna funkcja.

Definiując funkcję rozszerzenia, naprawdę nie dodajesz nic do klasy. To tylko cukier syntaktyczny. Porównajmy dwie następujące implementacje funkcji `usun`.

```kotlin
fun remove(text: String, value: String) =
    tekst.replace(value, "")

fun String.remove(value: String) =
    this.replace(value, "")
```

Pod maską są niemal identyczne. Różnica polega na tym, jak Kotlin oczekuje, że będą wywoływane. Zwykłe funkcje otrzymują wszystkie swoje argumenty w zwykłych pozycjach argumentów. Funkcje rozszerzenia są wywoływane "na" wartości.

### Właściwości rozszerzenia

Rozszerzenie nie może przechowywać stanu, więc nie może mieć pól. Chociaż właściwości nie potrzebują pól, mogą być definiowane przez swoje metody pobierające i ustawiające. Dlatego możemy definiować właściwości rozszerzenia, jeśli nie potrzebują one pola wspierającego i są definiowane przez akcesory.

```kotlin
val <T> List<T>.lastIndex: Int
    get() = size - 1
```

Właściwości rozszerzenia są bardzo popularne na Androidzie, gdzie dostęp do różnych usług jest skomplikowany i powtarzalny. Definiowanie właściwości rozszerzenia pozwala nam robić to znacznie łatwiej.

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

Właściwości rozszerzenia mogą definiować zarówno getter, jak i setter. Oto właściwość rozszerzenia, która dostarcza inną reprezentację daty urodzenia użytkownika:

```kotlin
class User {
    // ...
    var dateBirthMillis: Long? = null
}

var User.dateBirth: Date?
    get() = dateBirthMillis?.let(::Date)
    set(value) {
        dateBirthMillis = value?.time
    }
```

### Rozszerzenia vs elementy

Największa różnica między elementami a rozszerzeniami pod względem użytkowania polega na tym, że **rozszerzenia muszą być importowane oddzielnie**. Z tego powodu mogą być umieszczone w innym pakiecie. Ten fakt jest wykorzystywany, gdy nie możemy sami dodać elementu. Jest również wykorzystywany w projektach mających na celu oddzielenie danych i zachowań. Właściwości z polami muszą być umieszczone w klasie, ale metody można umieścić oddzielnie, o ile mają dostęp tylko do publicznego API klasy.

Dzięki temu, że rozszerzenia muszą być importowane, możemy mieć wiele rozszerzeń o tej samej nazwie dla tego samego typu. To dobrze, ponieważ różne biblioteki mogą dostarczać dodatkowych metod bez powodowania konfliktów. Z drugiej strony, niebezpieczne byłoby posiadanie dwóch rozszerzeń o tej samej nazwie, ale o różnych zachowaniach. Jeśli masz taką sytuację, jest to zapach kodu i wskazówka, że ktoś nadużył możliwości funkcji rozszerzenia.

Inną istotną różnicą jest to, że **rozszerzenia nie są wirtualne**, co oznacza, że nie mogą być zredefiniowane w klasach pochodnych. Dlatego jeśli masz rozszerzenie zdefiniowane zarówno dla supertypu, jak i subtypu, kompilator decyduje, która funkcja jest wybierana na podstawie tego, jak zmienna jest typowana, a nie jaka jest jej rzeczywista klasa.

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

Zachowanie funkcji rozszerzenia różni się od funkcji elementów. Funkcje elementów są *wirtualne*, więc rzutowanie w górę typu obiektu nie wpływa na wybór funkcji elementu.

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

To zachowanie jest wynikiem tego, że funkcje rozszerzenia są kompilowane wewnętrznie do normalnych funkcji, w których odbiorca rozszerzenia jest umieszczony jako pierwszy argument:

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

Kolejną konsekwencją tego, czym są rozszerzenia, jest to, że **definiujemy rozszerzenia dla typów, a nie dla klas**. Daje nam to większą swobodę. Na przykład możemy zdefiniować rozszerzenie dla typu nullable lub generycznego:

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

Ostatnia ważna różnica polega na tym, że **rozszerzenia nie są wymieniane jako elementy w odniesieniu do klasy**. Dlatego nie są uwzględniane przez procesory adnotacji; dlatego też, gdy przetwarzamy klasę za pomocą przetwarzania adnotacji, nie możemy wyodrębnić elementów, które powinny być przetworzone w rozszerzeniach. Z drugiej strony, jeśli wyodrębnimy elementy nieistotne jako rozszerzenia, nie musimy się martwić, że zostaną one zauważone przez te procesory. Nie musimy ich ukrywać, ponieważ i tak nie są w klasie, którą rozszerzają.

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

Aby zdefiniować funkcję rozszerzenia na obiekcie towarzyszącym (companion object), musimy użyć rzeczywistej nazwy tego obiektu. Jeśli ta nazwa nie jest ustawiona jawnie, domyślną jest "Companion". Aby zdefiniować funkcję rozszerzenia na obiekcie towarzyszącym, taki obiekt musi istnieć. Dlatego niektóre klasy definiują obiekty towarzyszące bez ciał.

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

### Członkowskie funkcje rozszerzeń

Możliwe jest definiowanie funkcji rozszerzeń wewnątrz klas. Takie funkcje nazywane są **członkowskimi funkcjami rozszerzeń**.

```kotlin
class Telephone {
    fun String.call() { 
        // ...
    }
}
```

Członkowskie funkcje rozszerzeń są uważane za nieoptymalne i powinniśmy unikać ich stosowania, jeśli nie mamy ku temu dobrego powodu. Aby uzyskać bardziej szczegółowe wyjaśnienie, zobacz [*Effective Kotlin*, *Pozycja 46: Unikaj członkowskich rozszerzeń*](https://kt.academy/article/ek-member-extensions).

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

Jednak istnieją także przypadki, gdy wolimy używać rozszerzeń zamiast elementów składowych. Weźmy pod uwagę interfejs `Iterable`, który ma tylko jedną funkcję członkowską, `iterator`; jednak ma wiele metod, które są zdefiniowane w bibliotece standardowej jako rozszerzenia[^16_1], takie jak `onEach` czy `joinToString`. Fakt, że są one zdefiniowane jako rozszerzenia, pozwala na mniejsze i bardziej zwięzłe interfejsy.

```kotlin
interface Iterable<out T> {
    operator fun iterator(): Iterator<T>
}
```

![](iterable_methods.png)

Funkcje rozszerzeń są również bardziej elastyczne niż zwykłe funkcje. Wynika to głównie z faktu, że są one definiowane dla typów, więc możemy definiować rozszerzenia dla typów, takich jak `Iterable<Int>` czy `Iterable<T>`.

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

Instancje `Product` są używane w twojej aplikacji, a instancje `ProductJson` są używane w API. Te obiekty muszą być oddzielone, ponieważ na przykład nie chcesz zmieniać odpowiedzi API, gdy zmieniasz nazwę właściwości w klasie wewnętrznej. Jednak często musimy przekształcać pomiędzy `Product` a `ProductJson`. W tym celu możemy zdefiniować funkcję członkowską `toProduct`.

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

Nie każdemu podoba się to rozwiązanie, ponieważ sprawia, że `ProductJson` jest większy i bardziej skomplikowany. Nie jest to również przydatne przy przekształcaniu z `Product` na `ProductJson`, ponieważ w większości nowoczesnych architektur nie chcemy, aby klasy domenowe (takie jak `Product`) były świadome takich szczegółów jak ich reprezentacja API. Lepszym rozwiązaniem jest zdefiniowanie zarówno `toProduct`, jak i `toProductJson` jako funkcji rozszerzeń, a następnie umieszczenie ich obok klasy `ProductJson`. Dobrze jest umieścić te funkcje przekształcające obok siebie, ponieważ mają wiele wspólnego.

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

Wygląda na to, że jest to popularny wzorzec zarówno w backendzie, jak i w aplikacjach Android.

### Podsumowanie

W tym rozdziale poznaliśmy rozszerzenia - potężną funkcję Kotlina, która często jest używana do tworzenia wygodnych i znaczących narzędzi oraz do lepszej kontroli naszego kodu. Jednak wraz z dużą mocą przychodzi wielka odpowiedzialność. Nie powinniśmy się obawiać używania rozszerzeń, ale powinniśmy używać ich świadomie i tylko tam, gdzie mają sens.

W następnym rozdziale w końcu przedstawimy kolekcje, abyśmy mogli omówić listy, zbiory, mapy i tablice. Przed nami wiele, więc przygotuj się.

[^16_1]: Roman Elizarov (kierownik projektu języka programowania Kotlin) odnosi się do tego jako projektu opartego na rozszerzeniach w bibliotece standardowej. Źródło: https://elizarov.medium.com/extension-oriented-design-13f4f27deaee
