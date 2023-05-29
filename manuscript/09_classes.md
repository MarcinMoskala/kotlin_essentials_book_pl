## Klasy

Gdy spojrzysz na świat wokół siebie, prawdopodobnie zauważysz mnóstwo obiektów. Może to być książka, czytnik Ebooków, monitor lub kubek kawy. Jesteśmy otoczeni obiektami. Ta obserwacja prowadzi do wniosku, że żyjemy w świecie obiektów, a zatem nasze programy powinny być zbudowane w ten sam sposób. To jest koncepcyjna podstawa programowania obiektowego. Nie wszyscy podzielają ten światopogląd, niektórzy wolą widzieć świat jako miejsce możliwych działań[^09_0], co jest podstawą programowania funkcyjnego. Niezależnie od tego, które podejście preferujemy, klasy i obiekty są ważnymi strukturami w programowaniu Kotlin i omówimy je w tym rozdziale. 

Klasa to szablon, który służy do tworzenia obiektu o konkretnych cechach. Aby utworzyć klasę w Kotlinie, używamy słowa kluczowego `class`, a następnie określamy jej nazwę. To dosłownie wszystko, czego potrzebujemy, aby utworzyć najprostszą klasę, ponieważ ciało klasy jest opcjonalne. Aby utworzyć obiekt, który jest instancją klasy, używamy konstruktora, czyli nazwy klasy z nawiasami okrągłymi. W przeciwieństwie do innych języków, takich jak C++ czy Java, w Kotlinie nie używamy słowa kluczowego `new`.

```kotlin
// Najprostsza definicja klasy
class A

fun main() {
    // Tworzenie obiektu z klasy A
    val a: A = A()
}
```

### Metody klasy

Wewnątrz klas możemy definiować funkcje. Aby to zrobić, najpierw musimy otworzyć nawiasy klamrowe w definicji klasy, aby określić ciało klasy.

```kotlin
class A {
    // ciało klasy
}
```

W ciele możemy określić funkcje. Funkcje zdefiniowane w ten sposób mają dwie ważne cechy:
* Muszą być wywołane na instancji tej klasy. Oznacza to, że aby wywołać funkcję, najpierw musi zostać utworzony obiekt.
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

Wszystkie funkcje zdefiniowane w ciele klasy nazywać będziemy metodami klasy. Są one również **metodami**, choć metodami są również funkcje rozszerzające.

Koncepcyjnie rzecz biorąc, metody reprezentują to, co obiekt może robić. Na przykład, ekspres do kawy powinien być w stanie produkować kawę, co możemy reprezentować za pomocą metody `makeCoffee` w klasie `CoffeeMachine`. W ten sposób klasy z metodami pomagają nam modelować świat.

### Właściwości

Wewnątrz ciał klas możemy również definiować zmienne. Zmienne zdefiniowane wewnątrz klas nazywane są **polami**. Istnieje ważna koncepcja związana z "enkapsulacją", która oznacza, że pola nie powinny być używane bezpośrednio spoza klasy, ponieważ w takim przypadku tracimy kontrolę nad ich stanem. Zamiast tego, pola powinny być używane przez akcesory:
* getter - funkcja służąca do pobierania aktualnej wartości pola,
* setter - funkcja służąca do ustawiania nowych wartości pola.

Ten wzorzec jest bardzo powszechny; w projektach Javy można zobaczyć mnóstwo funkcji getterów i setterów. Są one potrzebne do osiągnięcia enkapsulacji, ale sprawiają, że kod jest rozwlekły i mało czytelny. Dlatego twórcy języków wymyślili "właściwości". **Właściwość** to zmienna w klasie, która jest automatycznie enkapsulowana, a więc używa gettera i settera w sposób niejawny. W Kotlinie wszystkie zmienne zdefiniowane wewnątrz klas są właściwościami, a nie polami.

Niektóre języki, takie jak JavaScript, mają wbudowane wsparcie dla właściwości. Java do nich nie należy, dlatego wygenerowany z kodu w Kotlinie bytecode JVM zawiera metody getterów i setterów.

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
// równoważny kod Java
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

Każda właściwość w Kotlinie ma akcesory, dlatego nie powinniśmy definiować getterów ani setterów za pomocą jawnie określonych funkcji. Jeśli chcesz zmienić domyślny akcesor, użyj istniejącej do tego celu specjalnej składni.

```kotlin
class User {
    private var name: String = ""

    // NIE RÓB TEGO! ZAMIAST TEGO ZDEFINIUJ GETTER
    fun getName() = name

    // NIE RÓB TEGO! ZAMIAST TEGO ZDEFINIUJ SETTER
    fun setName(name: String) {
        this.name = name
    }
}
```

Tą specjalną składnią jest użycie słowa kluczowego `get` po definicji właściwości. Reszta jest równoznaczna z definiowaniem funkcji bez parametrów. Wewnątrz niej używamy słowa kluczowego `field`, aby odwołać się do pola właściwości. Domyślny getter zwraca właśnie wartość `field`, ale możemy ją w określony sposób zmodyfikować przed zwróceniem. Gdy definiujemy getter, możemy użyć składni jednowyrażeniowej lub zwykłego ciała i słowa kluczowego `return`.

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

Pamiętaj, że wszystkie użycia właściwości to użycia akcesorów. Wewnątrz akcesorów należy używać `field` zamiast nazwy właściwości, w przeciwnym razie prawdopodobnie skończysz z nieskończoną rekurencją.

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

Settery można określić w podobny sposób, ale musimy użyć słowa kluczowego `set` i dodatkowo potrzebujemy jednego parametru reprezentującego nową wartość dla danego pola. Domyślny setter służy do przypisania nowej wartości do `field`, ale możemy zmodyfikować to zachowanie - na przykład ustawić nową wartość tylko wtedy, gdy spełnia ona pewne warunki.

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

Jeśli niestandardowe akcesory właściwości nie używają słowa kluczowego `field`, pole nie zostanie wygenerowane dla właściwości. Przykładowo możemy zdefiniować właściwość reprezentującą pełne imię i nazwisko, które jest obliczane na podstawie imienia i nazwiska. Taka właściwość nie zawiera pola.

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

Właściwość `fullName` potrzebuje tylko gettera, ponieważ jest to właściwość tylko do odczytu `val`. Za każdym razem, gdy poprosimy o wartość tej właściwości, pełne imię i nazwisko będzie obliczane na podstawie `name` i `surname`. Zauważ, że ta właściwość jest obliczana na żądanie, co stanowi zaletę w porównaniu z użyciem zwykłej właściwości.

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

Ta różnica jest widoczna tylko wtedy, gdy wartości, z których korzysta nasza właściwość, są zmienne; gdy definiujemy niezmienny obiekt, wartość obliczona w getterze oraz podczas tworzenia klasy zawsze powinna być ta sama. Różnica w takim przypadku polega tylko na wydajności: czy wartość będzie obliczana podczas tworzenia obiektu, czy przy wywołaniu gettera.

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
    // h1 nigdy nie używał ani v1 ani v2
    // wartość v1 nie została obliczona ani razu
    // wartość v2 została obliczona raz
    val h2 = Holder() // Obliczanie v2
    println(h2.v1) // Obliczanie v1 i 42
    println(h2.v1) // Obliczanie v1 i 42
    println(h2.v2) // 42
    println(h2.v2) // 42
    // h2 użył v1 i v2 po dwa razy 
    // wartość v1 została obliczona dwa razy
    // wartość v2 została obliczona tylko raz
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

Po pewnym czasie zadecydowaliśmy, że `Date` nie jest już dobrym sposobem na reprezentowanie tego atrybutu. Być może mamy problemy z serializacją; być może musimy uczynić nasz obiekt wieloplatformowym; a być może musimy reprezentować czas w inny sposób, którego `Date` nie obsługuje. Dlatego postanowiliśmy użyć innego typu. Powiedzmy, że zdecydowaliśmy się użyć właściwości typu `Long` do przechowywania milisekund, ale nie chcemy pozbyć się poprzedniej właściwości, ponieważ jest ona używana w wielu innych częściach naszego kodu. Aby mieć ciastko i zjeść ciastko, możemy przekształcić naszą właściwość `birthdate`, aby w pełni zależała od nowej reprezentacji. W ten sposób zmieniliśmy sposób reprezentowania daty urodzenia, nie zmieniając wcześniejszego użycia.

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

> W powyższym getterze używam `let` oraz referencji do konstruktora. Oba te elementy Kotlinie są wyjaśnione w książce **Funkcyjny Kotlin**.

Taka właściwość `birthdate` może być również zdefiniowana jako funkcja rozszerzenia, co zostało przedstawione w rozdziale *Rozszerzenia*.

### Konstruktory

Kiedy tworzymy obiekt, często chcemy zainicjować go określonymi wartościami. Do tego używamy konstruktorów. Jak już widzieliśmy, gdy nie są określone żadne konstruktory, generowany jest pusty konstruktor domyślny bez żadnych parametrów.

```kotlin
class A

val a = A()
```

Aby zdefiniować nasz niestandardowy konstruktor, klasyczny sposób polega na użyciu słowa kluczowego `constructor` w ciele klasy, a następnie zdefiniowaniu jego parametrów i ciała.

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

Konstruktory są zwykle używane do ustawiania początkowych wartości naszych właściwości. Aby to uprościć, w Kotlinie wprowadzono specjalny rodzaj konstruktora nazywany **konstruktorem głównym** (**primary constructor**). Definiuje się go zaraz po nazwie klasy, a jego parametry można użyć do inicjalizacji właściwości.

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

Może być tylko jeden konstruktor główny. Możemy zdefiniować kolejny konstruktor, zwany **konstruktorem wtórnym** (**secondary constructor**), ale musi on wywołać konstruktor główny za pomocą słowa kluczowego `this`.

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

Główny konstruktor jest zwykle używany do określania wartości początkowych dla naszych właściwości. Te właściwości często mają te same nazwy co inne parametry, dlatego Kotlinie wprowadził specjalną notację: możemy definiować właściwości wewnątrz konstruktora głównego. Jeśli przed parametrem konstruktora głównego użyjemy `val` lub `var`, to definiujemy właściwość o takiej samej nazwie jak ten parametr, której wartością będzie wartość tego parametru. Takie parametry nazywamy **właściwościami konstruktora głównego**. 

```kotlin
class User(
    // właściwości konstruktora głównego
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

W praktyce rzadko używamy innych rodzajów konstruktorów niż konstruktor główny, a w nim większość parametrów określamy jako właściwości. Równie często definiujemy konstruktory główne z domyślnymi wartościami. W poniższym przykładzie tworzymy instancję `User` bez podawania argumentu `surname`, więc podczas tworzenia obiektu zostanie użyta określona przez nas wartość domyślna.

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

Porównując klasy zdefiniowane w Kotlinie i Javie możemy zauważyć, ile powtarzalnego kodu zostało wyeliminowanego dzięki zwięzłej składni Kotlina. W Javie w celu reprezentacji prostego użytkownika, z imieniem, nazwiskiem i wiekiem, typowa implementacja wygląda następująco:

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

W Kotlinie definiujemy tę samą klasę w następujący sposób:

```kotlin
class User(
    val name: String,
    val surname: String,
    val age: Int?,
)
```

Wynik kompilacji jest praktycznie identyczny. Gettery i konstruktory są obecne. Jeśli w to nie wierzysz, sprawdź sam (jak to zrobić przedstawiłem w sekcji *Co kryje się pod maską na JVM?* w rozdziale *Twój pierwszy program w Kotlinie*). Kotlin to zwięzły, ale potężny język.

### Klasy wewnętrzne

W Kotlinie możemy definiować klasy wewnątrz klas. Domyślnie są one statyczne, co oznacza, że nie mają dostępu do funkcji i właściwości klas zewnętrznych, dlatego mogą być tworzone bez odniesienia do klasy zewnętrznej.

```kotlin
class Puppy(val name: String) {

    class InnerPuppy {
        fun think() {
            // tutaj nie mamy dostępu do name
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

Jeśli chcesz, aby Twoja klasa wewnętrzna miała odniesienie do swojej klasy zewnętrznej, musisz uczynić ją wewnętrzną przy pomocy modyfikatora `inner`. Jednak tworzenie obiektów z takich klas wymaga instancji klasy zewnętrznej.

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
* klasy, w których istnieje ścisłe powiązanie między klasą zewnętrzną a klasą wewnętrzną, a klasa wewnętrzna służy do tego, aby nie definiować kolejnej nazwy w przestrzeni nazw biblioteki.

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

Jak widać, w Kotlinie możemy definiować klasy za pomocą naprawdę zwięzłej składni, a wynik jest bardzo czytelny. Główny konstruktor to niesamowity wynalazek, podobnie jak fakt, że w Kotlinie używamy właściwości zamiast pól. Dowiedziałeś się także o klasach wewnętrznych. Wszystko pięknie, ale jeszcze nie poruszyliśmy tematu dziedziczenia, jakże ważnego tematu dla programistów piszących w paradygmacie obiektowym. Omówimy je wraz z interfejsami i klasami abstrakcyjnymi w następnym rozdziale.

[^09_0]: Zobacz artykuł "Object-oriented or functional? Two ways to see the world", link: https://kt.academy/article/oop-vs-fp

