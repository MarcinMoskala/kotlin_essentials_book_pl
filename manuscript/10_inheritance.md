## Dziedziczenie

Starożytni filozofowie zauważyli, że otaczające nas obiekty i stworzenia można skategoryzować na klasy o wspólnych cechach. Arystoteles dokonał nawet dość precyzyjnej kategoryzacji dla znanych mu zwierząt na rodzaje. Na przykład wszystkie ssaki mają włosy lub futro, są stałocieplne i karmią swoje młode mlekiem[^10_0]. Podobnie jednak można powiedzieć, że wszystkie kubki do kawy albo suszarki do włosów mają wspólne cechy. W programowaniu reprezentujemy takie relacje za pomocą **dziedziczenia**.

Gdy klasa dziedziczy z innej klasy, posiada wszystkie jej funkcje i właściwości. Klasa, która dziedziczy, jest znana jako **podklasa** klasy, z której dziedziczy, zwaną **superklasą**. Nazywane są również **dzieckiem** i **rodzicem**.

W Kotlinie wszystkie klasy są domyślnie zamknięte, co oznacza, że nie można z nich dziedziczyć. Aby otworzyć klasę, używamy słowa kluczowego `open`, co pozwala na dziedziczenie z niej. Aby dziedziczyć z klasy, umieszczamy dwukropek po głównym konstruktorze (lub po nazwie klasy, jeśli nie definiujemy głównego konstruktora), a następnie wywołujemy konstruktor superklasy. W poniższym przykładzie klasa `Dog` dziedziczy po klasie `Mammal`. Ponieważ `Mammal` nie ma określonego konstruktora, używamy jego konstruktora domyślnego, a więc wywołujemy go bez argumentów (`Mammal()`). W ten sposób klasa `Dog` dziedziczy wszystkie właściwości i metody z klasy `Mammal`.

```kotlin
open class Mammal {
    val haveHairOrFur = true
    val warmBlooded = true
    var canFeed = false

    fun feedYoung() {
        if (canFeed) {
            println("Karmienie młodych mlekiem")
        }
    }
}

class Dog : Mammal() {
    fun makeVoice() {
        println("Hau hau")
    }
}

fun main() {
    val dog = Dog()
    dog.makeVoice() // Hau hau
    println(dog.haveHairOrFur) // true
    println(dog.warmBlooded) // true
    // Dog to Mammal, więc instance typu Dog można 
    // przypisać do zmiennej typu Mammal,
    // jest to tzw. rzutowanie w górę
    val mammal: Mammal = dog
    mammal.canFeed = true
    mammal.feedYoung() // Karmienie młodych mlekiem
}
```

Koncepcyjnie traktujemy podklasy tak, jakby *były* swoimi nadklasami: więc jeśli `Dog` dziedziczy po `Mammal`, mówimy, że `Dog` **jest** `Mammal`. Dlatego wszędzie tam, gdzie oczekujemy `Mammal`, możemy użyć instancji `Dog`. Biorąc to pod uwagę, dziedziczenie powinno być stosowane tylko wtedy, gdy istnieje prawdziwa relacja "jest" między dwiema klasami.

### Nadpisywanie elementów

Domyślnie podklasy nie mogą nadpisywać elementów zdefiniowanych w nadklasach, ponieważ Kotlin domyślnie traktuje wszystkie elementy jako zamknięte. Aby nadpisanie było możliwe, te elementy muszą być "otwarte" na nadpisanie, co określają przy pomocy modyfikatora `open`. Wtedy elementy podklasy mogą nadpisywać implementację elementów swoich rodziców, co wygląda tak jak definiowanie tych funkcji lub właściwości w dzieciach, ale z modyfikatorem `override` (ten modyfikator jest wymagany w Kotlinie).

```kotlin
open class Mammal {
    val haveHairOrFur = true
    val warmBlooded = true
    var canFeed = false
    open val hasFur = true

    open fun feedYoung() {
        if (canFeed) {
            println("Karmienie młodych mlekiem")
        }
    }
}

class Human : Mammal() {
    open override val hasFur = false
    
    override fun feedYoung() {
        if (canFeed) {
            println("Karmienie młodych mlekiem")
        } else {
            println("Karmienie młodych mlekiem z butelki")
        }
    }
}

fun main() {
    val dog = Mammal()
    println(dog.hasFur) // true
    dog.feedYoung() // Nic nie zostanie wydrukowane
    
    val human = Human()
    println(human.hasFur) // false
    human.feedYoung() // Karmienie młodych mlekiem z butelki
    human.canFeed = true
    human.feedYoung() // Karmienie młodych mlekiem
}
```

### Rodzice z niepustymi konstruktorami

Dotychczas dziedziczyliśmy tylko z klas o pustych konstruktorach, więc gdy określaliśmy nadklasę, używaliśmy pustych nawiasów. Jeśli jednak nadklasa ma parametry konstruktora, musimy zdefiniować w tych nawiasach pewne argumenty.

```kotlin
open class Animal(val name: String)

class Dodo : Animal("Dodo")
```

Możemy użyć właściwości konstruktora głównego jako argumentów konstruktora nadklasy lub do konstruowania tych argumentów.

```kotlin
open class Animal(val name: String)

class Dog(name: String) : Animal(name)

class Cat(name: String) : Animal("Mr $name")

class Human(
   firstName: String,
   lastName: String,
) : Animal("$firstName $lastName")

fun main() {
   val dog = Dog("Cookie")
   println(dog.name) // Cookie
   val cat = Cat("MiauMiau")
   println(cat.name) // Mr MiauMiau
}
```

### Wywołanie `super`

Gdy klasa rozszerza inną klasę, przejmuje zachowanie z nadklasy, ale także dodaje pewne zachowanie specyficzne dla podklasy. Dlatego nadpisywanie metod często wymaga uwzględnienia zachowania metod, które są nadpisywane. W tym celu przydatne jest wywołanie implementacji nadklasy w tych metodach podklasy. Robimy to za pomocą słowa kluczowego `super`, po którym następuje kropka, a następnie wywołujemy metodę, którą chcemy nadpisać.

Spójrz na klasy `Dog` i `BorderCollie`, które są przedstawione w poniższym przykładzie. Domyślne zachowanie dla psa to merdanie ogonem, gdy widzi psiego przyjaciela. Border Collie powinny zachowywać się tak samo, ale dodatkowo się kłaść. W tym przypadku, aby wywołać implementację nadklasy, musimy użyć `super.seeFriend()`.

```kotlin
open class Dog {
    open fun seeFriend() {
        println("Merdanie ogonem")
    }
}

class BorderCollie : Dog() {
    override fun seeFriend() {
        println("Kładzie się")
        super.seeFriend()
    }
}

fun main() {
    val dog = Dog()
    dog.seeFriend() 
    // Merdanie ogonem
    
    val borderCollie = BorderCollie()
    borderCollie.seeFriend()
    // Kładzie się
    // Merdanie ogonem
}
```

### Klasa abstrakcyjna

Ssak to grupa zwierząt, a nie konkretny gatunek. Definiuje zestaw cech, ale nie może istnieć jako taki. Aby zdefiniować klasę, która może być używana jedynie jako nadklasa innych klas, ale nie może tworzyć obiektu, używamy słowa kluczowego `abstract` przed jej definicją klasy. Innymi słowy, modyfikator `open` można interpretować jako "można dziedziczyć z tej klasy", podczas gdy `abstract` jako "trzeba dziedziczyć z tej klasy, aby jej użyć". Klasy abstrakcyjne są otwarte, więc nie ma potrzeby używania modyfikatora `open`, gdy klasa ma już modyfikator `abstract`.

```kotlin
abstract class Mammal {
   val haveHairOrFur = true
   val warmBlooded = true
   var canFeed = false

   fun feedYoung() {
       if (canFeed) {
           println("Karmienie młodych mlekiem")
       }
   }
}
```

Gdy klasa jest abstrakcyjna, może mieć abstrakcyjne funkcje i właściwości. Takie funkcje nie mają ciała, a każda podklasa musi je nadpisać. Dzięki temu, gdy mamy obiekt, którego typem jest klasa abstrakcyjna, możemy wywołać jego abstrakcyjne funkcje, ponieważ bez względu na rzeczywistą klasę tego obiektu, musi ona zdefiniować te funkcje.

```kotlin
abstract class Mammal {
    val haveHairOrFur = true
    val warmBlooded = true
    var canFeed = false

    abstract fun feedYoung()
}

class Dog : Mammal() {
    override fun feedYoung() {
        if (canFeed) {
            println("Karmienie młodych mlekiem")
        }
    }
}

class Human : Mammal() {
    override fun feedYoung() {
        if (canFeed) {
            println("Karmienie młodych mlekiem")
        } else {
            println("Karmienie młodych mlekiem z butelki")
        }
    }
}

fun feedYoung(mammal: Mammal) {
    // Możemy to zrobić, ponieważ karmMlode jest funkcją
    // abstrakcyjną w klasie Mammal
    mammal.feedYoung()
}

fun main() {
    val dog = Dog()
    dog.canFeed = true
    feedYoung(dog) // Karmienie młodych mlekiem
    feedYoung(Human()) // Karmienie młodych mlekiem z butelki
}
```

Klasa abstrakcyjna może również mieć metody nieabstrakcyjne, które mają swoje ciało. Takie metody mogą być używane przez inne metody. Dlatego klasy abstrakcyjne mogą być używane jako szablony z częściową implementacją dla innych klas. Spójrz na poniższą klasę abstrakcyjną `CoffeeMachine`, która określa, jak przygotować latte lub doppio, ale potrzebuje podklasy, która przesłoni metody `prepareEspresso` i `addMilk`. Jest to więc pewnego rodzaju szablon dla klas reprezentujących ekspresy do kawy, dostarcza implementację tylko dla niektórych metod i oczekuje zdefiniowanie innych. 

```kotlin
abstract class CoffeeMachine {
    abstract fun prepareEspresso()
    abstract fun addMilk()

    fun prepareLatte() {
        prepareEspresso()
        addMilk()
    }
    
    fun prepareDoppio() {
        prepareEspresso()
        prepareEspresso()
    }
}
```

Kotlin nie obsługuje wielokrotnego dziedziczenia, więc klasa może dziedziczyć tylko z jednej klasy otwartej. Nie uważam tego za problem, ponieważ dziedziczenie nie jest obecnie tak popularne, zamiast tego częściej implementowane są interfejsy.

### Interfejsy

Interfejs definiuje zestaw właściwości i metod, które powinna posiadać klasa. Interfejsy definiujemy za pomocą słowa kluczowego `interface`, nazwy oraz ciała z oczekiwanymi właściwościami i metodami.

```kotlin
interface CoffeeMaker {
    val type: String
    fun makeCoffee(size: Size): Coffee
}
```

Gdy klasa implementuje interfejs, musi ona zastąpić wszystkie elementy zdefiniowane przez ten interfejs. Dzięki temu możemy traktować instancję klasy jako instancję interfejsu. Interfejsy implementujemy podobnie do rozszerzania klas, ale bez wywoływania konstruktora, ponieważ interfejsy nie mogą mieć konstruktorów.

```kotlin
class User(val id: Int, val name: String)

interface UserRepository {
    fun findUser(id: Int): User?
    fun addUser(user: User)
}

class FakeUserRepository : UserRepository {
    private var users = mapOf<Int, User>()

    override fun findUser(id: Int): User? = users[id]

    override fun addUser(user: User) {
        users += user.id to user
    }
}

fun main() {
    val repo: UserRepository = FakeUserRepository()
    repo.addUser(User(123, "Zed"))
    val user = repo.findUser(123)
    println(user?.name) // Zed
}
```

Jak już wspomniano, interfejsy mogą określać, że oczekują, iż klasa będzie mieć określoną właściwość. Takie właściwości mogą być zdefiniowane jako zwykłe właściwości lub mogą być zdefiniowane przez akcesory (getter dla `val` lub getter i setter dla `var`).


```kotlin
interface Named {
    val name: String
    val fullName: String
}

class User(
    override val name: String,
    val surname: String,
) : Named {
    override val fullName: String
        get() = "$name $surname"
}
```

Właściwość tylko do odczytu `val` może zostać zastąpiona właściwością do odczytu i zapisu `var`. Wynika to z faktu, że właściwość `val` oczekuje gettera, a właściwość `var` dostarcza gettera oraz settera.

```kotlin
interface Named {
    val name: String
}

class NameBox : Named {
    override var name = "(domyślny)"
}
```

Klasa może implementować wiele interfejsów.

```kotlin
interface Drinkable {
    fun drink()
}

interface Spillable {
    fun spill()
}

class Mug : Drinkable, Spillable {
    override fun drink() {
        println("Mmmm")
    }
    override fun spill() {
        println("Au, au, AUUU")
    }
}
```

Interfejsy mogą określać domyślne ciała dla swoich metod. Takie metody nie muszą (ale mogą) być implementowane przez podklasy.

```kotlin
class User(val id: Int, val name: String)

interface UserRepository {
    fun findUser(id: Int): User? =
        getUsers().find { it.id == id }

    fun getUsers(): List<User>
}

class FakeUserRepository : UserRepository {
    private var users = listOf<User>()

    override fun getUsers(): List<User> = users

    fun addUser(user: User) {
        users += user
    }
}

fun main() {
    val repo = FakeUserRepository()
    repo.addUser(User(123, "Zed"))
    val user = repo.findUser(123)
    println(user?.name) // Zed
}
```

Możemy wywołać to domyślne ciało, używając słowa kluczowego `super` i zwykłego wywołania metody[^10_6].

```kotlin
interface NicePerson {
   fun cheer() {
       println("Cześć")
   }
}

class Alex : NicePerson

class Ben : NicePerson {
   override fun cheer() {
       super.cheer()
       println("Nazywam się Ben")
   }
}

fun main() {
   val alex = Alex()
   alex.cheer() 
    // Cześć

   val ben = Ben()
   ben.cheer()
   // Cześć
   // Nazywam się Ben
}
```

Gdy dwa interfejsy definiują metodę o tej samej nazwie i parametrach, klasa implementująca oba te interfejsy **musi** nadpisać tę metodę. Aby wywołać domyślne ciała tych metod, musimy użyć `super` wraz z nazwą klasy, której chcemy użyć, w nawiasach ostrokątnych. Czyli aby wywołać `start` z `Boat`, użyj `super<Boat>.start()`. Aby wywołać `start` z `Car`, użyj `super<Car>.start()`.

```kotlin
interface Boat {
   fun start() {
       println("Gotowy do pływania")
   }
}

interface Car {
   fun start() {
       println("Gotowy do jazdy")
   }
}

class Amphibian: Car, Boat {
   override fun start() {
       super<Car>.start()
       super<Boat>.start()
   }
}

fun main() {
   val vehicle = Amphibian()
   vehicle.start()
   // Gotowy do jazdy
   // Gotowy do pływania
}
```

### Widoczność

Projektując nasze klasy, staramy się ujawnić jak najmniej informacji[^10_3]. Jeśli nie ma powodu, dla którego element miałby być widoczny[^10_4], wolimy go ukryć. Dlatego, jeśli nie ma dobrego powodu, aby mieć mniej restrykcyjny typ widoczności, dobrym zwyczajem jest nadanie klasom i elementom jak najbardziej restrykcyjnej widoczności. Robimy to za pomocą modyfikatorów widoczności.

Dla elementów klasy możemy użyć 4 modyfikatorów widoczności:
* `public` (domyślny) - widoczny wszędzie przez funkcję mogących zobaczyć deklarującą klasę.
* `private` - widoczny tylko przez funkcje tej samej klasie.
* `protected` - widoczny tylko przez funkcje tej samej klasie i jej podklasach.
* `internal` - widoczny wewnątrz tego samego modułu dla funkcji mogących zobaczyć deklarującą klasę.

Elementy na najwyższym poziomie mają 3 modyfikatory widoczności:
* `public` (domyślny) - przez wszystkie funkcje. 
* `private` - widoczny przez funkcje w tym samym pliku.
* `internal` - widoczny przez funkcje wewnątrz tego samego modułu.

Zauważ, że moduł nie jest tym samym co pakiet. W Kotlinie moduł jest definiowany jako pliki kompilowane razem. Może to oznaczać:
* moduł Gradle,
* projekt Maven,
* moduł IntelliJ IDEA,
* zestaw plików kompilowanych za pomocą jednego wywołania zadania Ant.

Przyjrzyjmy się kilku przykładom, zaczynając od domyślnej widoczności `public`, która sprawia, że elementy są widoczne wszędzie.

```kotlin
// File1.kt
open class A {
   public val a = 10
   public fun b() {
       println(a) // Można użyć
   }
}

public val c = 20
public fun d() {}

class B: A() {
   fun e() {
       println(a) // Można użyć
       println(b()) // Można użyć
   }
}

fun main() {
   println(A().a) // Można użyć
   println(A().b()) // Można użyć
   println(c) // Można użyć
   println(d()) // Można użyć
}

// File2.kt w tym samym lub innym module niż File1.kt
fun main() {
   println(A().a) // Można użyć
   println(A().b()) // Można użyć
   println(c) // Można użyć
   println(d()) // Można użyć
}
```

Modyfikator `private` można interpretować jako "widoczny w zakresie tworzenia"; więc jeśli zdefiniujemy element w klasie, będzie on widoczny tylko w tej klasie; jeśli zdefiniujemy element w pliku, będzie on widoczny tylko w tym pliku.

```kotlin
// File1.kt
open class A {
    private val a = 10
    private fun b() {
        println(a) // Można użyć
    }
}

private val c = 20
private fun d() {}

class B : A() {
    fun e() {
        println(a) // Błąd, nie można użyć a!
        println(b()) // Błąd, nie można użyć b!
    }
}

fun main() {
    println(A().a) // Błąd, nie można użyć a!
    println(A().b()) // Błąd, nie można użyć b!
    println(c) // Można użyć
    println(d()) // Można użyć
}

// File2.kt w tym samym lub innym module niż File1.kt
fun main() {
    println(A().a) // Błąd, nie można użyć a!
    println(A().b()) // Błąd, nie można użyć b!
    println(c) // Błąd, nie można użyć c!
    println(d()) // Błąd, nie można użyć d!
}
```

Modyfikator `protected` można interpretować jako "widoczny w klasie i jej podklasach". `protected` ma sens tylko dla elementów zdefiniowanych w klasach. Jest podobny do `private`, ale chronione elementy są również widoczne w podklasach klasy, w której te elementy są zdefiniowane.

```kotlin
// File1.kt
open class A {
   protected val a = 10
   protected fun b() {
       println(a) // Można użyć
   }
}

open class B: A() {
   fun e() {
       println(a) // Można użyć!
       println(b()) // Można użyć!
   }
}

class C: A() {
   fun f() {
       println(a) // Można użyć!
       println(b()) // Można użyć!
   }
}

fun main() {
   println(A().a) // Błąd, nie można użyć a!
   println(A().b()) // Błąd, nie można użyć b!
}

// File2.kt w tym samym lub innym module niż File1.kt
fun main() {
   println(A().a) // Błąd, nie można użyć a!
   println(A().b()) // Błąd, nie można użyć b!
}
```

Modyfikator `internal` sprawia, że elementy są widoczne w tym samym module. Jest to przydatne dla twórców bibliotek, którzy używają modyfikatora `internal` dla elementów, które mają być widoczne w ich projekcie, ale nie chcą ich udostępniać użytkownikom tych bibliotek. Jest również przydatny w projektach wielomodułowych, aby ograniczyć dostęp do pojedynczego modułu. Jest bezużyteczny w projektach jednomodułowych[^10_5].

```kotlin
// File1.kt
open class A {
   internal val a = 10
   internal fun b() {
       println(a) // Można użyć
   }
}

internal val c = 20
internal fun d() {}

class B: A() {
   fun e() {
       println(a) // Można użyć
       println(b()) // Można użyć
   }
}

fun main() {
   println(A().a) // Można użyć
   println(A().b()) // Można użyć
   println(c) // Można użyć
   println(d()) // Można użyć
}

// File2.kt w tym samym module co File1.kt
fun main() {
   println(A().a) // Można użyć
   println(A().b()) // Można użyć
   println(c) // Można użyć
   println(d()) // Można użyć
}

// File3.kt w innym module niż File1.kt
fun main() {
   println(A().a) // Błąd, nie można użyć a!
   println(A().b()) // Błąd, nie można użyć b!
   println(c) // Błąd, nie można użyć c!
   println(d()) // Błąd, nie można użyć d!
}
```

Jeśli twój moduł może być używany przez inny moduł, zmień widoczność publicznych elementów, których nie chcesz udostępniać, na `internal`. Jeśli element jest przeznaczony do dziedziczenia i jest używany tylko w klasie i podklasach, nadaj mu modyfikator `protected`. Jeśli używasz elementu tylko w tym samym pliku lub klasie, nadaj mu modyfikator `private`.

Zmiana widoczności właściwości oznacza zmianę widoczności jej akcesorów. Pole właściwości zawsze jest prywatne. Aby zmienić widoczność settera, umieść modyfikator widoczności przed słowem kluczowym `set`. Getter musi mieć taką samą widoczność jak właściwość.

```kotlin
class View {
   var isVisible: Boolean = true
       private set

   fun hide() {
       isVisible = false
   }
}

fun main() {
   val view = View()
   println(view.isVisible) // true
   view.hide()
   println(view.isVisible) // false
   view.isVisible = true // BŁĄD
   // Nie można przypisać do isVisible,
   // setter jest prywatny w View
}
```

### `Any`

Jeśli klasa nie ma wyraźnego rodzica, jej domyślnym rodzicem jest `Any`, który jest nadklasą wszystkich klas w Kotlinie. Oznacza to, że gdy oczekujemy parametru typu `Any?`, akceptujemy wszystkie możliwe wartości jako argumenty.

```kotlin
fun consumeAnything(a: Any?) {
    println("Om nom $a")
}

fun main() {
    consumeAnything(null) // Om nom null
    consumeAnything(123) // Om nom 123
    consumeAnything("ABC") // Om nom ABC
}
```

Możesz myśleć o `Any` jak o otwartej klasie z trzema metodami: `toString`, `equals` i `hashCode`. Zostaną one lepiej wyjaśnione w następnym rozdziale, *Data klasy*. Nadpisywanie metod zdefiniowanych przez `Any` jest opcjonalne, ponieważ każda z nich to otwarta funkcja z domyślnym ciałem.

### Podsumowanie

W tym rozdziale nauczyliśmy się, jak korzystać z dziedziczenia w Kotlinie. Zapoznaliśmy się z otwartymi i abstrakcyjnymi klasami, interfejsami oraz modyfikatorami widoczności. Są one przydatne, gdy chcemy reprezentować hierarchie klas.

Zamiast używać klas do reprezentowania hierarchii, możemy również używać je do przechowywania danych; w tym celu bardzo przydatny jest modyfikator `data`, który jest przedstawiony w następnym rozdziale.

[^10_0]: Ciekawostką dla właścicieli psów czy kotów jest to, że, podobnie jak wszystkie ssaki, mają one pępek; jednak często nie jest łatwo go znaleźć, ponieważ jest mały i czasami ukryty pod futrem.
[^10_3]: Głębsze wyjaśnienie powodów stojących za tą ogólną zasadą programowania przedstawione jest w *Efektywny Kotlin*, *Temat 30: Minimalizuj widoczność elementów*.
[^10_4]: Widoczność określa, gdzie można używać elementu. Jeśli element nie jest widoczny, nie będzie sugerowany przez IDE i nie można go używać.
[^10_5]: Jednakże widziałem przypadki, gdy zespoły używały modyfikatora widoczności `internal` jako substytutu dla modyfikatora package-private w Java. Pomimo że ma on inne zachowanie, niektórzy programiści traktują ten modyfikator jako formę dokumentacji, która powinna być interpretowana jako "ten element nie powinien być używany w różnych pakietach". Nie jestem fanem takich praktyk. 
[^10_6]: Domyślne metody sprawiają, że interfejsy są czymś więcej niż to, co uważano za interfejs w tradycyjnym ujęciu. Umożliwiają one interfejsom definiowanie zachowań, które są dziedziczone przez klasy implementujące te interfejsy. Koncepcja, która reprezentuje zbiór metod, które można wykorzystać do rozszerzenia funkcjonalności klasy, jest znana w programowaniu jako **trait**. Dlatego we wczesnych wersjach Kotlina używaliśmy słowa kluczowego `trait` zamiast słowa kluczowego `interface`. Jednak wersja 8 Javy wprowadziła domyślne ciała dla metod interfejsów, więc twórcy Kotlina założyli, że społeczność JVM poszerzyła znaczenie pojęcia interfejsu i dlatego teraz używamy słowa kluczowego `interface`. Koncepcja traitów jest używana w Kotlinie. Przykład można znaleźć w moim artykule *Traits for testing in Kotlin*, który można znaleźć pod adresem `http://kt.academy/article/traits-testing`.

