## Klasy

Spojrzyj na świat wokół siebie, a prawdopodobnie zauważysz mnóstwo obiektów. Może to być książka, czytnik Ebooków, monitor lub kubek kawy. Jesteśmy otoczeni obiektami. Ta idea prowadzi do wniosku, że żyjemy w świecie obiektów, a zatem nasze programy powinny być zbudowane w ten sam sposób. To jest koncepcyjna podstawa podejścia do programowania obiektowego. Nie wszyscy podzielają ten światopogląd - niektórzy wolą widzieć świat jako miejsce możliwych działań[^09_0], co jest koncepcyjną podstawą podejścia do programowania funkcjonalnego - ale niezależnie od tego, które podejście preferujemy, klasy i obiekty są ważnymi strukturami w programowaniu Kotlin.

Klasa to szablon, który służy do tworzenia obiektu o konkretnych cechach. Aby utworzyć klasę w Kotlinie, używamy słowa kluczowego `class`, a następnie podajemy nazwę. To dosłownie wszystko, czego potrzebujemy, aby utworzyć najprostszą klasę, ponieważ ciało klasy jest opcjonalne. Aby utworzyć obiekt, który jest instancją klasy, używamy domyślnej funkcji konstruktora, która jest nazwą klasy i nawiasami okrągłymi. W przeciwieństwie do innych języków, takich jak C++ czy Javie, w Kotlinie nie używamy słowa kluczowego `new`.

```kotlin
// Najprostsza definicja klasy
class A

fun main() {
    // Tworzenie obiektu z klasy
    val a: A = A()
}
```

### Metody członkowskie

Wewnątrz klas możemy definiować funkcje. Aby to zrobić, najpierw musimy otworzyć nawiasy klamrowe w definicji klasy, aby określić ciało klasy.

```kotlin
class A {
    // ciało klasy
}
```

Tam możemy określić funkcje. Funkcje zdefiniowane w ten sposób mają dwie ważne cechy:
* Funkcje muszą być wywołane na instancji tej klasy. Oznacza to, że aby wywołać metodę, najpierw musi zostać utworzony obiekt.
* Wewnątrz metod możemy użyć `this`, które jest odniesieniem do instancji klasy, na której wywołaliśmy tę funkcję.

```kotlin
class A {
    fun printMe() {
        println(this)
    }
}

fun main() {
    val a = A()
    println(a) // A@ADDRESS
    a.printMe() // A@ADDRESS (ten sam adres)
}
```

Wszystkie elementy zdefiniowane w ciele klasy nazywane są **członkami**, więc funkcja zdefiniowana w ciele klasy nazywana jest **funkcją członkowską**. Funkcje związane z klasami nazywane są **metodami**, więc wszystkie funkcje członkowskie są metodami, ale funkcje rozszerzeń (które zostaną omówione w późniejszym rozdziale) są również metodami.

Koncepcyjnie rzecz biorąc, metody reprezentują to, co obiekt może robić. Na przykład, ekspres do kawy powinien być w stanie produkować kawę, co możemy reprezentować za pomocą metody `makeCoffee` w klasie `CoffeeMachine`. W ten sposób klasy z metodami pomagają nam modelować świat.

### Właściwości

Wewnątrz ciał klas możemy również definiować zmienne. Zmienne zdefiniowane wewnątrz klas nazywane są **polami**. Istnieje ważna idea związana z "enkapsulacją", która oznacza, że pola nie powinny być używane bezpośrednio spoza klasy, ponieważ w takim przypadku tracimy kontrolę nad ich stanem. Zamiast tego, pola powinny być używane przez akcesory:
* getter - funkcja, która służy do pobierania bieżącej wartości tego pola,
* setter - funkcja, która służy do ustawiania nowych wartości dla tego pola.

Ten wzorzec jest bardzo wpływowy; w projektach Javy można zobaczyć mnóstwo funkcji getterów i setterów, które są głównie używane w klasach przechowujących dane. Są one potrzebne do osiągnięcia enkapsulacji, ale są również zakłócającym boilerplate code. Dlatego twórcy języków wymyślili potężniejsze pojęcie zwane "właściwościami". **Właściwość** to zmienna w klasie, która jest automatycznie enkapsulowana, tak że używa gettera i settera "pod maską". We Kotlinie wszystkie zmienne zdefiniowane wewnątrz klas są właściwościami, a nie polami.

Niektóre języki, takie jak JavieScriptie, mają wbudowane wsparcie dla właściwości, ale Javie nie. Dlatego w Kotlin/JVM dla każdej właściwości generowane są funkcje akcesorów: getter dla `val` oraz getter i setter dla `var`.

```kotlin
// Kod w Kotlin
class User {
    var name: String = ""
}

fun main(args: Array<String>) {
    val user = User()
    user.name = "Alex" // wywołanie settera
    println(user.name) // wywołanie gettera
}
```

```javascript
// równoważny kod JavaScript
function User() {
    this.name = '';
}

function main(args) {
    var user = new User();
    user.name = 'Alex';
    println(user.name);
}
```

```java
// równoważny kod Javie
public final class User {
    @NotNull
    private String name = "";

    // getter
    @NotNull
    public final String getName() {
        return this.name;
    }

    // setter
    public final void setName(@NotNull String name) {
        this.name = name;
    }
}

public final class PlaygroundKt {
    public static void main(String[] var0) {
        User user = new User();
        user.setName("Alex"); // wywołanie settera
        System.out.println(user.getName()); // wywołanie gettera
    }
}
```
Każda właściwość w Kotlinie ma akcesory, dlatego nie powinniśmy definiować getterów ani setterów za pomocą jawnie określonych funkcji. Jeśli chcesz zmienić domyślny akcesor, istnieje specjalna składnia do tego.

```kotlin
class User {
    private var name: String = ""

    // NIE RÓB TEGO! ZDEFINIUJ GETTER WŁAŚCIWOŚCI ZAMIAST TEGO
    fun getName() = name

    // NIE RÓB TEGO! ZDEFINIUJ SETTER WŁAŚCIWOŚCI ZAMIAST TEGO
    fun setName(name: String) {
        this.name = name
    }
}
```

Aby określić niestandardowy getter, używamy słowa kluczowego `get` po definicji właściwości. Reszta jest równoznaczna z definiowaniem funkcji bez parametrów. Wewnątrz tej funkcji używamy słowa kluczowego `field`, aby odwołać się do pola wspierającego. Domyślny getter zwraca wartość `field`, ale możemy zmienić to zachowanie tak, aby wartość ta była w jakiś sposób modyfikowana przed jej zwróceniem. Gdy definiujemy getter, możemy użyć składni jednowyrażeniowej lub zwykłego ciała i słowa kluczowego `return`.

```kotlin
class User {
    var name: String = ""
        get() = field.uppercase()
    // lub
    // var name: String = ""
    //     get() {
    //         return field.uppercase()
    //     }
}

fun main() {
    val user = User()
    user.name = "norbert"
    println(user.name) // NORBERT
}
```

Getter musi zawsze mieć taką samą widoczność i typ wyniku jak właściwość. Gettery nie powinny zgłaszać wyjątków ani wykonywać intensywnych obliczeń.

Pamiętaj, że wszystkie użycia właściwości to użycia akcesorów. Wewnątrz akcesorów należy używać `field` zamiast nazwy właściwości, ponieważ w przeciwnym razie prawdopodobnie skończysz z nieskończoną rekurencją.

```kotlin
class User {
    // NIE RÓB TEGO
    var name: String = ""
        // Użycie nazwy właściwości wewnątrz gettera
        // prowadzi do nieskończonej rekurencji
        get() = name.uppercase()
}

fun main() {
    val user = User()
    user.name = "norbert"
    println(user.name) // Błąd: java.lang.StackOverflowError
}
```

Settery można określić w podobny sposób, ale musimy użyć słowa kluczowego `set` oraz potrzebujemy jednego parametru reprezentującego wartość, która jest ustawiana. Domyślny setter służy do przypisania nowej wartości do `field`, ale możemy zmodyfikować to zachowanie, na przykład, aby ustawić nową wartość tylko wtedy, gdy spełnia ona pewne warunki.

```kotlin
class User {
    var name: String = ""
        get() = field.uppercase()
        set(value) {
            if (value.isNotBlank()) {
                field = value
            }
        }
}

fun main() {
    val user = User()
    user.name = "norbert"
    user.name = ""
    user.name = "  "
    println(user.name) // NORBERT
}
```

Settery mogą mieć bardziej ograniczoną widoczność niż właściwości, co pokażemy w kolejnym rozdziale.

Jeśli niestandardowe akcesory właściwości nie używają słowa kluczowego `field`, pole wspierające nie zostanie wygenerowane. Na przykład możemy zdefiniować właściwość reprezentującą pełne imię i nazwisko, które jest obliczane na podstawie imienia i nazwiska. Oznacza to, że niektóre właściwości w ogóle mogą nie potrzebować pola.

```kotlin
class User {
    var name: String = ""
    var surname: String = ""
    val fullName: String
        get() = "$name $surname"
}

fun main() {
    val user = User()
    user.name = "Maja"
    user.surname = "Moskała"
    println(user.fullName) // Maja Moskała
}
```

Właściwość `fullName` potrzebuje tylko gettera, ponieważ jest to właściwość tylko do odczytu `val`. Kiedykolwiek poprosimy o wartość tej właściwości, pełne imię i nazwisko będzie obliczane na podstawie `name` i `surname`. Zauważ, że ta właściwość jest obliczana na żądanie, co stanowi zaletę w porównaniu z użyciem zwykłej właściwości.

```kotlin
class User {
    var name: String = ""
    var surname: String = ""
    val fullName1: String
        get() = "$name $surname"
    val fullName2: String = "$name $surname"
}

fun main() {
    val user = User()
    user.name = "Maja"
    user.surname = "Markiewicz"
    println(user.fullName1) // Maja Markiewicz
    println(user.fullName2) // Maja Markiewicz
    user.surname = "Moskała"
    println(user.fullName1) // Maja Moskała
    println(user.fullName2) // Maja Markiewicz
}
```

Ta różnica jest widoczna tylko wtedy, gdy wartości, na których opiera się nasza właściwość, są zmiennymi; dlatego gdy definiujemy niezmienny obiekt, obliczanie wartości właściwości w getterze lub podczas tworzenia klasy powinno dać ten sam wynik. Różnica polega na wydajności: obliczamy stałe wartości właściwości podczas tworzenia obiektu, ale wartości getterów są obliczane na żądanie za każdym razem, gdy są one potrzebne.

```kotlin
class Holder {
    val v1: Int get() = calculate("v1")
    val v2: Int = calculate("v2")

    private fun calculate(propertyName: String): Int {
        println("Obliczanie $propertyName")
        return 42
    }
}

fun main() {
    val h1 = Holder() // Obliczanie v2
    // h1 nigdy nie używał v1, więc nie został obliczony
    // obliczył v2, chociaż nie był używany
    val h2 = Holder() // Obliczanie v2
    println(h2.v1) // Obliczanie v1 i 42
    println(h2.v1) // Obliczanie v1 i 42
    println(h2.v2) // 42
    println(h2.v2) // 42
    // h2 użył v1 dwa razy i został obliczony dwa razy
    // obliczył v2 tylko raz,
    // mimo że był używany dwa razy
}
```

Jako kolejny przykład wyobraźmy sobie, że musimy przechować datę urodzenia użytkownika. Początkowo reprezentowaliśmy ją za pomocą `Date` z biblioteki standardowej Javy.

```kotlin
import java.util.Date

class User {
    // ...
    var birthdate: Date? = null
}
```

Upłynął czas, i `Date` nie jest już dobrym sposobem na reprezentowanie tego atrybutu. Być może mamy problemy z serializacją; być może musimy uczynić nasz obiekt wieloplatformowym; albo być może musimy reprezentować czas w innym kalendarzu, którego `Date` nie obsługuje. Dlatego postanowiliśmy użyć innego typu zamiast `Date`. Powiedzmy, że zdecydowaliśmy się użyć właściwości `Long` do przechowywania milisekund, ale nie możemy pozbyć się poprzedniej właściwości, ponieważ jest ona używana przez wiele innych części naszego kodu. Aby móc mieć ciastko i zjeść ciastko, możemy przekształcić naszą właściwość `birthdate`, aby w pełni zależała od nowej reprezentacji. W ten sposób zmieniliśmy sposób reprezentowania daty urodzenia, nie zmieniając wcześniejszego użycia.

```kotlin
class User {
    // ...
    var birthdateMillis: Long? = null

    var birthdate: Date?
        get() = birthdateMillis?.let(::Date)
        set(value) {
            birthdateMillis = value?.time
        }
}
```
> W powyższym getterze używam `let` oraz referencji do konstruktora. Oba te elementy Kotlinie są wyjaśnione w książce **Functional Kotlin**.

Taka właściwość `birthdate` może być również zdefiniowana jako funkcja rozszerzenia, co zostanie przedstawione w rozdziale *Rozszerzenia*.

### Konstruktory

Kiedy tworzymy obiekt, często chcemy zainicjować go określonymi wartościami. Właśnie do tego używamy konstruktorów. Jak już widzieliśmy, gdy nie są określone żadne konstruktory, generowany jest pusty konstruktor domyślny bez żadnych parametrów.

```kotlin
class A

val a = A()
```

Aby zdefiniować nasz niestandardowy konstruktor, klasyczny sposób polega na użyciu słowa kluczowego `constructor` w ciele klasy, a następnie definiujemy jego parametry i ciało.

```kotlin
class User {
    var name: String = ""
    var surname: String = ""

    constructor(name: String, surname: String) {
        this.name = name
        this.surname = surname
    }
}

fun main() {
    val user = User("Johnny", "Depp")
    println(user.name) // Johnny
    println(user.surname) // Depp
}
```

Konstruktory są zwykle używane do ustawiania początkowych wartości naszych właściwości. Aby to uprościć, Kotlinie wprowadził specjalny rodzaj konstruktora nazywany **konstruktorem głównym**. Definiuje się go zaraz po nazwie klasy, a jego parametry można użyć podczas inicjalizacji właściwości.

```kotlin
class User constructor(name: String, surname: String) {
    var name: String = name
    var surname: String = surname
}

fun main() {
    val user = User("Johnny", "Depp")
    println(user.name) // Johnny
    println(user.surname) // Depp
}
```

Gdy określamy konstruktor główny, użycie słowa kluczowego `constructor` jest opcjonalne, więc możemy go po prostu pominąć.

```kotlin
class User(name: String, surname: String) {
    var name: String = name
    var surname: String = surname
}

fun main() {
    val user = User("Johnny", "Depp")
    println(user.name) // Johnny
    println(user.surname) // Depp
}
```

Może być tylko jeden konstruktor główny. Możemy zdefiniować kolejny konstruktor, zwany **konstruktorem wtórnym**, ale musi on wywołać konstruktor główny za pomocą słowa kluczowego `this`.

```kotlin
class User(name: String, surname: String) {
    var name: String = name
    var surname: String = surname

    // Konstruktor wtórny
    constructor(user: User) : this(user.name, user.surname) {
        // opcjonalne ciało
    }
}

fun main() {
    val user = User("Johnny", "Depp")
    println(user.name) // Johnny
    println(user.surname) // Depp

    val user2 = User(user)
    println(user2.name) // Johnny
    println(user2.surname) // Depp
}
```

Główny konstruktor jest zwykle używany do określania wartości początkowych dla naszych właściwości. Te właściwości często mają te same nazwy co inne parametry, dlatego Kotlinie wprowadził lepsze wsparcie dla tego: możemy definiować właściwości wewnątrz konstruktora głównego. Takie właściwości definiują właściwość klasy i parametr konstruktora, które mają tę samą nazwę.

```kotlin
class User(
    var name: String,
    var surname: String,
) {
    // opcjonalne ciało
}

fun main() {
    val user = User("Johnny", "Depp")
    println(user.name) // Johnny
    println(user.surname) // Depp
}
```

Większość klas w Kotlinie jest definiowana właśnie w ten sposób: za pomocą konstruktora głównego z właściwościami. Rzadko używamy innych rodzajów konstruktorów.

Często definiujemy konstruktory główne z domyślnymi wartościami. Tutaj tworzymy instancję `User` bez podawania argumentu `surname`, więc podczas tworzenia `User` zostanie użyta określona przez nas wartość domyślna.

```kotlin
class User(
    var name: String = "",
    var surname: String = "Anonim",
)

fun main() {
    val user = User("Johnny")
    println(user.name) // Johnny
    println(user.surname) // Anonim
}
```

### Klasy reprezentujące dane w Kotlinie i Javie

Porównując klasy zdefiniowane w Kotlinie i Javie, możemy zauważyć, ile kodu szablonowego Kotlinie wyeliminował. W Javie, aby przedstawić prostego użytkownika, z imieniem, nazwiskiem i wiekiem, typowa implementacja wygląda następująco:

```java
public final class User {
    @NotNull
    private final String name;
    @NotNull
    private final String surname;
    private final int age;

    public User(
        @NotNull String name,
        @NotNull String surname,
        int age
    ) {
        this.name = name;
        this.surname = surname;
        this.age = age;
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public String getSurname() {
        return surname;
    }

    public int getAge() {
        return age;
    }
}
```

W Kotlinie reprezentujemy tę samą klasę w następujący sposób:

```kotlin
class User(
    val name: String,
    val surname: String,
    val age: Int?,
)
```

Wynik kompilacji jest praktycznie taki sam. Gettery i konstruktory są obecne. Jeśli w to nie wierzysz, sprawdź sam (jak przedstawiono w sekcji *Co kryje się pod maską JVM?* w rozdziale *Twój pierwszy program w Kotlin*). Kotlinie to zwięzły, ale potężny język.

### Klasy wewnętrzne

W Kotlinie możemy definiować klasy wewnątrz klas. Domyślnie są one statyczne, co oznacza, że nie mają dostępu do klas zewnętrznych, dlatego mogą być tworzone bez odniesienia do klasy zewnętrznej.

```kotlin
class Puppy(val name: String) {

    class InnerPuppy {
        fun think() {
            // nie mamy dostępu do name tutaj
            println("Wewnętrzny szczeniak myśli")
        }
    }
}

fun main() {
    val innerPuppy = Puppy.InnerPuppy()
    // Tworzymy InnerPuppy na klasie, nie obiekcie
    innerPuppy.think() // Wewnętrzny szczeniak myśli
}
```

Jeśli chcesz, aby Twoja klasa wewnętrzna miała odniesienie do swojej klasy zewnętrznej, musisz uczynić ją wewnętrzną za pomocą modyfikatora `inner`. Jednak tworzenie obiektów z takich klas wymaga instancji klasy zewnętrznej.

```kotlin
class Puppy(val name: String) {

    inner class InnerPuppy {
        fun think() {
            println("Wewnętrzny $name myśli")
        }
    }
}

fun main() {
    val puppy = Puppy("Cookie")
    val innerPuppy = puppy.InnerPuppy() // Potrzebujemy puppy
    innerPuppy.think() // Wewnętrzny Cookie myśli
}
```

Przykłady klas wewnętrznych w bibliotece standardowej to:
* prywatne implementacje iteratorów;
* klasy, gdzie istnieje ścisłe powiązanie między klasą zewnętrzną a klasą wewnętrzną, a klasa wewnętrzna służy do tego, aby nie definiować kolejnej nazwy w przestrzeni nazw biblioteki.

```kotlin
// Klasa z biblioteki standardowej Kotlin
class FileTreeWalk(
    ...
) : Sequence<File> {

    /** Zwraca iterator przechodzący przez pliki. */
    override fun iterator(): Iterator<File> =
        FileTreeWalkIterator()

    private inner class FileTreeWalkIterator
        : AbstractIterator<File>() {
        ...
    }

    ...
}
```

### Podsumowanie

Jak widać, w Kotlinie możemy definiować klasy za pomocą naprawdę zwięzłej składni, a wynik jest bardzo czytelny. Główny konstruktor to niesamowity wynalazek, podobnie jak fakt, że Kotlinie używa właściwości zamiast pól. Dowiedziałeś się także o klasach wewnętrznych. To wszystko jest wspaniałe, ale jeszcze nie poruszyliśmy tematu dziedziczenia, które jest tak ważne dla programistów lubiących styl zorientowany obiektowo. Omówimy to wraz z interfejsami i klasami abstrakcyjnymi w następnym rozdziale.

[^09_0]: Zobacz "Obiektowo-orientowany czy funkcyjny? Dwa sposoby widzenia świata" autorstwa Marcina Moskały, link: https://kt.academy/article/oop-vs-fp

