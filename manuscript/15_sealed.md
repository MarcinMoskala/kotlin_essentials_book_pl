## Sealed klasy i interfejsy

Klasy i interfejsy w Kotlinie nie służą tylko do reprezentowania zestawu operacji lub danych; możemy również używać klas i dziedziczenia do reprezentowania hierarchii. Na przykład, powiedzmy, że wysyłasz żądanie sieciowe; w rezultacie albo otrzymujesz żądane dane, albo żądanie kończy się niepowodzeniem z informacjami o tym, co poszło nie tak. Te dwa możliwe rezultaty można przedstawić za pomocą dwóch klas implementujących interfejs:

```kotlin
interface Result
class Success(val data: String) : Result
class Failure(val exception: Throwable) : Result
```

Alternatywnie można użyć klasy abstrakcyjnej:

```kotlin
abstract class Result
class Success(val data: String) : Result()
class Failure(val exception: Throwable) : Result()
```

W tym przypadku spodziewamy się, że gdy funkcja zwraca `Result`, może być to `Success` lub `Failure`.

```kotlin
val result: Result = getSomeData()
when (result) {
    is Success -> handleSuccess(result.data)
    is Failure -> handleFailure(result.exception)
}
```

Problem polega na tym, że przy użyciu zwykłego interfejsu lub klasy abstrakcyjnej nie ma gwarancji, że zdefiniowane podklasy są wszystkimi możliwymi podtypami tego interfejsu lub klasy abstrakcyjnej. Ktoś może zdefiniować inną klasę i sprawić, że będzie ona implementować lub rozszerzać `Result`. Ktoś może nawet użyć do tego wyrażenia tworzącego obiekt.

```kotlin
class FakeSuccess : Result

val res1: Result = FakeSuccess()
val res2: Result = object : Result {}
```

Hierarchia, której podklasy nie są znane z góry, nazywana jest hierarchią nieograniczoną. Dla `Result` wolelibyśmy zdefiniować hierarchię ograniczoną, co robimy, używając modyfikatora `sealed` przed klasą lub interfejsem[^14_0][^14_3].

```kotlin
sealed interface Result
class Success(val data: String) : Result
class Failure(val exception: Throwable) : Result

// lub

sealed class Result
class Success(val data: String) : Result()
class Failure(val exception: Throwable) : Result()
```

> Gdy używamy modyfikatora `sealed` przed klasą, sprawia to, że klasa staje się już abstrakcyjna, więc nie używamy dodatkowo modyfikatora `abstract`.

Wszystkie podklasy sealed klasy lub interfejsu muszą spełniać kilka wymagań:
* muszą być zdefiniowane w tym samym pakiecie i module, co ich rodzic,
* nie mogą być lokalne ani zdefiniowane za pomocą wyrażenia tworzącego obiekt.

Oznacza to, że gdy używasz modyfikatora `sealed`, kontrolujesz, jakie podklasy ma klasa lub interfejs. Klienci Twojej biblioteki lub modułu nie mogą dodać własnych bezpośrednich podklas[^14_2]. Nikt nie może po cichu dodać lokalnej klasy ani wyrażenia obiektu, które rozszerza sealed klasę lub interfejs. Kotlin uczynił to niemożliwym. Hierarchia podklas jest ograniczona.

> Sealed interfejsy zostały wprowadzone w nowszych wersjach Kotlina, aby umożliwić klasom uczestnictwo w wielu różnych ograniczonych hierarchiach (można rozszerzać tylko jedną klasę, ale implementować wiele interfejsów). Relacja między sealed klasą i interfejsem jest podobna do relacji między klasą abstrakcyjną a interfejsem. Mocą klas jest to, że mogą przechowywać stan (właściwości nieabstrakcyjne) i kontrolować otwartość swoich elementów (mogą mieć metody i właściwości końcowe). Mocą interfejsów jest to, że klasa może dziedziczyć tylko z jednej klasy, ale może implementować wiele interfejsów.

### Sealed klasy i wyrażenia `when`

Kiedy używamy `when` jako wyrażenia, zawsze musimy zwrócić jakąś wartość. W większości przypadków jedynym sposobem na osiągnięcie tego jest określenie gałęzi `else`.

```kotlin
fun commentValue(value: String) = when {
    value.isEmpty() -> "Nie powinno być puste"
    value.length < 5 -> "Zbyt krótkie"
    else -> "Poprawne"
}

fun main() {
    println(commentValue("")) // Nie powinno być puste
    println(commentValue("ABC")) // Zbyt krótkie
    println(commentValue("ABCDEF")) // Poprawne
}
```

Jednak istnieją również przypadki, w których Kotlin wie, że rozpatrzyliśmy wszystkie możliwe wartości. Na przykład, gdy używamy wyrażenia when z wartością typu enum i porównujemy tę wartość do wszystkich możliwych wartości enum.

```kotlin
enum class PaymentType {
    CASH,
    CARD,
    TRANSFER,
}

fun commentDecision(type: PaymentType) = when (type) {
    PaymentType.CASH -> "Zapłacę gotówką"
    PaymentType.CARD -> "Zapłacę kartą"
    PaymentType.TRANSFER -> "Zapłacę przelewem"
}
```

Dla wartości określone typem z modyfikatorem sealed można rozpatrzyć wszystkie możliwości poprzez sprawdzenie wszystkich możliwych podtypów. Do sprawdzenia typu używamy operatora `is`. W ten sposób nie musimy używać gałęzi `else`.

```kotlin
sealed class Response<out V>
class Success<V>(val value: V) : Response<V>()
class Failure(val error: Throwable) : Response<Nothing>()

fun handle(response: Response<String>) {
    val text = when (response) {
        is Success -> "Sukces z ${response.value}"
        is Failure -> "Błąd"
        // else nie jest potrzebne tutaj
    }
    print(text)
}
```

Ponadto IntelliJ automatycznie sugeruje dodanie pozostałych gałęzi. To sprawia, że sealed klasy i interfejsy są bardzo wygodne w użyciu, gdy musimy pokryć wszystkie możliwe warianty.

![](remaining_branches.png)

Zauważ, że gdy `else` nie jest używane, a my dodajemy kolejną podklasę sealed klasy lub interfejsu, należy dostosować użycie tego wyrażenia `when`, uwzględniając ten nowy typ. Jest to wygodne w lokalnym kodzie, ponieważ zmusza nas do obsługi nowego wariantu w wyczerpujących wyrażeniach `when`. Jest to jednak problem, gdy sealed klasa lub interfejs jest częścią publicznego API biblioteki, lub współdzielonego modułu, gdyż dodanie podtypu jest niekompatybilne wstecznie, ponieważ wszystkie moduły używające wyczerpującego `when` muszą obsłużyć jeden więcej możliwy typ.

### Sealed vs enum

Enumy reprezentują zestaw wartości. Sealed klasy lub interfejsy reprezentują zestaw typów. To istotna różnica. Klasa to coś więcej niż wartość. Może mieć wiele instancji i może być nośnikiem danych. Pomyśl o `Response`: gdyby była enumem, nie mogłaby przechowywać `value` ani `error`. Podklasy seled klasy lub interfejsy mogą przechowywać różne dane, podczas gdy enum to tylko zestaw wartości.

### Przypadki użycia

Używamy sealed klas, gdy chcemy wyrazić, że istnieje konkretna liczba podklas danej klasy.

```kotlin
sealed class MathOperation
class Plus(val left: Int, val right: Int) : MathOperation()
class Minus(val left: Int, val right: Int) : MathOperation()
class Times(val left: Int, val right: Int) : MathOperation()
class Divide(val left: Int, val right: Int) : MathOperation()

sealed interface Tree
class Leaf(val value: Any?) : Tree
class Node(val left: Tree, val right: Tree) : Tree

sealed interface Either<out L, out R>
class Left<out L>(val value: L) : Either<L, Nothing>
class Right<out R>(val value: R) : Either<Nothing, R>

sealed interface AdView
object FacebookAd : AdView
object GoogleAd : AdView
class OwnAd(val text: String, val imgUrl: String) : AdView
```

Kluczową korzyścią jest to, że wyrażenie when może łatwo pokryć wszystkie możliwe typy w hierarchii, używając operatora `is`. Warunek when zapewnia wtedy, że obsługiwane są wszystkie możliwe podtypy.

```kotlin
fun BinaryTree.height(): Int = when (this) {
    is Leaf -> 1
    is Node -> maxOf(this.left.height(), this.right.height())
}
```

Warto także dodać, że gdy używamy modyfikatora `sealed`, możemy użyć refleksji, aby znaleźć wszystkie podklasy[^14_1]:

```kotlin
sealed interface Parent
class A : Parent
class B : Parent
class C : Parent

fun main() {
    println(Parent::class.sealedSubclasses)
    // [class A, class B, class C]
}
```

### Podsumowanie

Sealed klasy oraz interfejsy powinny być używane do reprezentowania ograniczonych hierarchii. Wyrażenie when ułatwia obsługę każdego możliwego podtypu. Jest to wygodne i często wykorzystywane w Kotlinie. Jeśli chcemy kontrolować, jakie są podklasy danej klasy, powinniśmy użyć modyfikatora `sealed`.

Następnie omówimy ostatni specjalny rodzaj klasy, który służy do definiowania dodatkowych informacji o elementach naszego kodu: adnotacje.

[^14_0]: Ograniczone hierarchie są używane do reprezentowania wartości, które mogą przyjmować kilka różnych, ale stałych typów. W innych językach, ograniczone hierarchie mogą być reprezentowane przez sumę typów, koprodukty lub unie oznakowane.
[^14_1]: Wymaga to zależności `kotlin-reflect`. Więcej o refleksji w *Zaawansowany Kotlin*.
[^14_2]: Nadal można deklarować klasę abstrakcyjną lub interfejs jako podklasę sealed klasy, lub interfejsu i z niej już klient będzie mógł dziedziczyć w innym module.
[^14_3]: Słowo "sealed" można przetłumaczyć jako "zapieczętowany", tak jak pieczętowało się niegdyś koperty przed wysłaniem.  
