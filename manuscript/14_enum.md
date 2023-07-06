## Enumy

W tym rozdziale zapoznamy się z enumami, czyli specjalnym rodzajem klasy, służącym do reprezentowania stałego zbioru wartości. Zacznijmy od przykładu. Załóżmy, że implementujesz metodę płatności, która musi obsługiwać trzy możliwe opcje: płatność gotówką, płatność kartą i przelew bankowy. Najprostszym sposobem na reprezentowanie stałego zestawu wartości w Kotlinie jest enum[^14_0]. W jego ciele wymieniamy wszystkie wartości, oddzielając je przecinkami. Wartości nazywamy przy użyciu notacji UPPER_SNAKE_CASE (np. `CASH`). Do wartości klasy enum można odwołać się przez nazwę tej wartości, poprzedzoną nazwą klasy i kropką (np. `PaymentOption.CASH`). Wszystkie te wartości mają typ enuma (w tym przypadku `PaymentOption`).

```kotlin
enum class PaymentOption {
    CASH,
    CARD,
    TRANSFER,
}

fun printOption(option: PaymentOption) {
    println(option)
}

fun main() {
    val option: PaymentOption = PaymentOption.CARD
    println(option) // CARD
    printOption(option) // CARD
}
```

Każdy enum ma następujące funkcje statyczne:

* `values`, która zwraca tablicę wszystkich wartości tego enuma;
* `valueOf`, która zamienia stringa na wartość o pasującej nazwie (z uwzględnieniem wielkości liter) lub rzuca wyjątek.

```kotlin
enum class PaymentOption {
    CASH,
    CARD,
    TRANSFER,
}

fun main() {
    val option: PaymentOption =
        PaymentOption.valueOf("TRANSFER")
    println(option)

    println("Wszystkie opcje: ")
    val paymentOptions: Array<PaymentOption> =
        PaymentOption.values()
    for (paymentOption in paymentOptions) {
        println(paymentOption)
    }
}
// TRANSFER
// Wszystkie opcje:
// CASH
// CARD
// TRANSFER
```

Zamiast tych metod możemy również użyć funkcji `enumValues` i `enumValueOf`, w których typ enuma określamy przy użyciu argumentu generycznego.

```kotlin
enum class PaymentOption {
    CASH,
    CARD,
    TRANSFER,
}

fun main() {
    val option = enumValueOf<PaymentOption>("TRANSFER")
    println(option)

    println("Wszystkie opcje: ")
    val paymentOptions = enumValues<PaymentOption>()
    for (paymentOption in paymentOptions) {
        println(paymentOption)
    }
}
// TRANSFER
// Wszystkie opcje:
// CASH
// CARD
// TRANSFER
```

Jak widać, kolejność elementów w enumach jest ważna. Funkcje `values` oraz `enumValues` zawsze zwracają wartości w kolejności, w jakiej są one zdefiniowane. Warto też dodać, że każda wartość enuma ma dwie właściwości:

* `name` - nazwa tej wartości,
* `ordinal` - pozycja tej wartości (zaczynając od 0).

```kotlin
enum class PaymentOption {
    CASH,
    CARD,
    TRANSFER,
}

fun main() {
    val option = PaymentOption.TRANSFER
    println(option.name) // TRANSFER
    println(option.ordinal) // 2
}
```

Każdy enum jest podklasą abstrakcyjnej klasy `Enum`. Ta nadrzędna klasa gwarantuje właściwości `name` i `ordinal`. Enumy mają właściwości, które implementują `toString`, `equals` oraz `hashCode`, ale w przeciwieństwie do klas danych, mają także `compareTo` (ich naturalna kolejność to kolejność definicji).

Enumy można używać w warunkach when. Co więcej, nie ma potrzeby używania gałęzi else, gdy uwzględniamy wszystkie możliwe wartości enuma.

```kotlin
fun transactionFee(paymentOption: PaymentOption): Double =
    when (paymentOption) {
        PaymentOption.CASH -> 0.0
        PaymentOption.CARD, PaymentOption.TRANSFER -> 0.05
    }
```

Enumy są bardzo wygodne, ponieważ można je łatwo przetwarzać na ciągi znaków lub odczytywać. Są popularnym sposobem reprezentowania skończonego zestawu możliwych wartości.

### Dane w wartościach wyliczeń

W Kotlinie każda wartość wyliczenia może przechowywać stan. Można zdefiniować konstruktor główny dla enuma, a następnie każda wartość musi określić swoje dane obok nazwy. **Dobrą praktyką jest, aby wartości wyliczeń były zawsze niemutowalne, więc ich stan nigdy nie powinien ulegać zmianie**.

```kotlin
import java.math.BigDecimal

enum class PaymentOption(val commission: BigDecimal) {
    CASH(BigDecimal.ONE),
    CARD(BigDecimal.TEN),
    TRANSFER(BigDecimal.ZERO)
}

fun main() {
    println(PaymentOption.CARD.commission) // 10
    println(PaymentOption.TRANSFER.commission) // 0

    val paymentOption: PaymentOption =
        PaymentOption.values().random()
    println(paymentOption.commission) // 0, 1 lub 10
}
```

### Enumy z własnymi metodami

Enumy w Kotlinie mogą mieć abstrakcyjne metody, których implementacje są specyficzne dla elementu. Kiedy je definiujemy, enum musi zdefiniować abstrakcyjną metodę, a każdy element musi ją nadpisać:

```kotlin
enum class PaymentOption {
    CASH {
        override fun startPayment(
            transaction: Transaction
        ) {
            showCashPaymentInfo(transaction)
        }
    },
    CARD {
        override fun startPayment(
            transaction: Transaction
        ) {
            moveToCardPaymentPage(transaction)
        }
    },
    TRANSFER {
        override fun startPayment(
            transaction: Transaction
        ) {
            showMoneyTransferInfo()
            setupPaymentWatcher(transaction)
        }
    };

    abstract fun startPayment(transaction: Transaction)
}
```

Ta opcja nie jest popularna, ponieważ zazwyczaj wolimy używać funkcyjnych właściwości konstruktora głównego[^14_1] lub funkcji rozszerzających[^14_2].

### Podsumowanie

Enumy to wygodny sposób reprezentowania konkretnego zestawu możliwych wartości. Każda wartość ma właściwości `name` i `ordinal`. Możemy uzyskać tablicę wszystkich wartości za pomocą funkcji statycznej `values` lub funkcji `enumValues`. Możemy także zamienić `String` na enuma za pomocą funkcji statycznej `valueOf` lub funkcji `enumValueOf`.

W następnym rozdziale porozmawiamy o sealed klasach, które często traktowane są jako podobne do enumów, ale reprezentują zupełnie inną (potężniejszą) abstrakcję. Enumy reprezentują zestaw stałych wartości, podczas gdy sealed klasy mogą tworzyć zamkniętą hierarchię klas.

[^14_0]: W polskiej literaturze często używa się nazwy *wyliczenie* zamiast *enum*, ale w tej książce będę używał angielskiej nazwy.
[^14_1]: Zmienne funkcyjne są opisane w książce *Funkcyjny Kotlin*. Przykład użycia enuma z funkcjonalnymi właściwościami konstruktora głównego przedstawiłem w książce *Efektywny Kotlin*, *Temat 41: Użyj wyliczenia do reprezentowania listy wartości*.
[^14_2]: Funkcje rozszerzeń są opisane później w tej książce.
