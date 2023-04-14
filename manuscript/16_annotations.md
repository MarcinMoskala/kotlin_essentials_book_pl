## Klasy adnotacji

Innym specjalnym rodzajem klasy w Kotlinie są adnotacje, które używamy do dostarczania dodatkowych informacji o elemencie. Oto przykład klasy, która używa adnotacji `JvmField`, `JvmStatic` oraz `Throws`[^15_2].

```kotlin
import java.math.BigDecimal
import java.math.MathContext

class Money(
    val amount: BigDecimal,
    val currency: String,
) {
    @Throws(IllegalArgumentException::class)
    operator fun plus(other: Money): Money {
        require(currency == other.currency)
        return Money(amount + other.amount, currency)
    }

    companion object {
        @JvmField
        val MATH = MathContext(2)

        @JvmStatic
        fun eur(amount: Double) =
            Money(amount.toBigDecimal(MATH), "EUR")

        @JvmStatic
        fun usd(amount: Double) =
            Money(amount.toBigDecimal(MATH), "USD")

        @JvmStatic
        fun pln(amount: Double) =
            Money(amount.toBigDecimal(MATH), "PLN")
    }
}
```

Możesz również zdefiniować własne adnotacje. Oto przykład deklaracji i użycia adnotacji:

```kotlin
annotation class Factory
annotation class FactoryFunction(val name: String)

@Factory
class CarFactory {

    @FactoryFunction(name = "toyota")
    fun makeToyota(): Car = Toyota()

    @FactoryFunction(name = "skoda")
    fun makeSkoda(): Car = Skoda()
}

abstract class Car
class Toyota : Car()
class Skoda : Car()
```

Możesz się zastanawiać, co robią te adnotacje. Odpowiedź jest zaskakująco prosta: absolutnie nic. Adnotacje same w sobie nie są aktywne i nie zmieniają sposobu działania naszego kodu. Przechowują jedynie informacje. Jednak wiele bibliotek zależy od adnotacji i zachowuje się zgodnie z tym, co za ich pomocą określamy.

Wiele ważnych bibliotek korzysta z mechanizmu zwanego *przetwarzaniem adnotacji*. Działa to prosto: istnieją klasy zwane *procesorami adnotacji*, które są uruchamiane podczas budowy naszego kodu. Analizują nasz kod i generują dodatkowy kod. Ogólnie rzecz biorąc, są one silnie związane z adnotacjami. Ten nowy kod nie jest częścią źródeł naszego projektu, ale możemy uzyskać do niego dostęp po jego wygenerowaniu. Z tego faktu korzystają biblioteki używające przetwarzania adnotacji. Spójrz więc na tę klasę, która używa biblioteki Java `Mockito` z procesorem adnotacji:

```kotlin
class DoctorServiceTest {

    @Mock
    lateinit var doctorRepository: DoctorRepository

    lateinit var doctorService: DoctorService

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
        doctorService = DoctorService(doctorRepository)
    }

    // ...
}
```

Właściwość `doctorRepository` jest oznaczona jako `Mock`, co jest interpretowane przez procesor Mock, aby ta zmienna mogła uzyskać wartość atrapy. Ten procesor generuje klasę, która tworzy i ustawia wartość dla właściwości `doctorRepository` w `DoctorServiceTest`. Oczywiście, ta wygenerowana klasa nie będzie działać sama przez się, ponieważ musi być uruchomiona. Właśnie po to jest `MockitoAnnotations.initMocks(this)`: używa refleksji, aby wywołać tę wygenerowaną klasę.

Przetwarzanie adnotacji jest lepiej opisane w *Zaawansowane Kotlin*, w rozdziale *Przetwarzanie adnotacji*.

### Meta-adnotacje

Adnotacje, które służą do oznaczania innych adnotacji, są znane jako meta-adnotacje. W bibliotece stdlib Kotlin są cztery kluczowe meta-adnotacje:
* `Target` wskazuje rodzaje elementów kodu, które są możliwymi celami adnotacji. Jako argumenty przyjmuje wartości wyliczenia `AnnotationTarget`, które obejmują wartości takie jak `CLASS`, `PROPERTY`, `FUNCTION`, itp.
* `Retention` określa, czy adnotacja jest przechowywana w binarnym wyniku kompilacji i jest widoczna dla refleksji. Domyślnie obie wartości są prawdziwe.
* `Repeatable` określa, że adnotacja może być stosowana dwa razy lub więcej w pojedynczym elemencie kodu.
* `MustBeDocumented` określa, że adnotacja jest częścią publicznego API i dlatego powinna być uwzględniona w wygenerowanej dokumentacji dla elementu, do którego adnotacja jest stosowana.

Oto przykłady użycia niektórych z tych adnotacji:

```kotlin
@MustBeDocumented
@Target(AnnotationTarget.CLASS)
annotation class Factory

@Repeatable
@Target(AnnotationTarget.FUNCTION)
annotation class FactoryFunction(val name: String)
```

### Adnotowanie konstruktora głównego

Aby oznaczyć konstruktor główny adnotacją, należy użyć słowa kluczowego `constructor` jako części jego definicji, przed nawiasami.

```kotlin
// JvmOverloads oznacza konstruktor główny
class User @JvmOverloads constructor(
    val name: String,
    val surname: String,
    val age: Int = -1,
)
```

### Listy literalne

Gdy określamy adnotację z wartością tablicy, możemy użyć specjalnej składni zwanej "literałem tablicy". Oznacza to, że zamiast używać `arrayOf`, możemy zadeklarować tablicę za pomocą nawiasów kwadratowych.

```kotlin
annotation class AnnotationWithList(
    val elements: Array<String>
)

@AnnotationWithList(["A", "B", "C"])
val str1 = "ABC"

@AnnotationWithList(elements = ["D", "E"])
val str2 = "ABC"

@AnnotationWithList(arrayOf("F", "G"))
val str3 = "ABC"
```

Ten zapis jest dozwolony tylko dla adnotacji i nie działa przy definiowaniu tablic w żadnym innym kontekście w naszym kodzie.

### Podsumowanie

Adnotacje służą do opisywania naszego kodu. Mogą być interpretowane przez procesory adnotacji lub przez klasy używające refleksji w czasie wykonywania. Narzędzia i biblioteki wykorzystują to do automatyzacji niektórych działań dla nas. Adnotacje same w sobie są prostą funkcją, ale możliwości oferowane przez nie są niesamowite[^15_3].

Przejdźmy teraz do słynnej funkcji Kotlin, która daje nam możliwość rozszerzenia dowolnego typu o metody lub właściwości: rozszerzenia.

[^15_2]: Adnotacje `JvmField`, `JvmStatic` i `Throws` są opisane w książce *Zaawansowany Kotlin* i służą do dostosowywania sposobu, w jaki elementy Kotlin mogą być używane w kodzie Java.
[^15_3]: W książce *Zaawansowany Kotlin* zobaczymy, jak można zaimplementować procesory adnotacji i co można z nimi zrobić.
