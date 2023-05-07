## Przeładowanie operatorów

W Kotlinie możemy dodać element do listy za pomocą operatora `+`. W ten sam sposób możemy dodać do siebie dwa ciągi znaków. Możemy sprawdzić, czy kolekcja zawiera element, używając operatora `in`. Możemy również dodawać, odejmować lub mnożyć elementy typu `BigDecimal`, który jest klasą JVM używaną do reprezentowania potencjalnie dużych liczb o nieograniczonej precyzji.

```kotlin
import java.math.BigDecimal

fun main() {
    val list: List<String> = listOf("A", "B")
    val newList: List<String> = list + "C"
    println(newList) // [A, B, C]

    val str1: String = "AB"
    val str2: String = "CD"
    val str3: String = str1 + str2
    println(str3) // ABCD

    println("A" in list) // true
    println("C" in list) // false

    val money1: BigDecimal = BigDecimal("12.50")
    val money2: BigDecimal = BigDecimal("3.50")
    val money3: BigDecimal = money1 * money2
    println(money3) // 43.7500
}
```

Stosowanie operatorów między obiektami jest możliwe dzięki funkcji Kotlin o nazwie *przeładowanie operatorów*, która pozwala na zdefiniowanie specjalnych rodzajów metod, które mogą być używane jako operatory. Zobaczmy to na przykładzie własnej klasy.

### Przykład przeładowania operatorów

Załóżmy, że musisz reprezentować liczby zespolone w swojej aplikacji. Są to specjalne rodzaje liczb w matematyce, które są reprezentowane przez dwie części: rzeczywistą i urojoną. Liczby zespolone są użyteczne w różnego rodzaju obliczeniach w fizyce i inżynierii.

```kotlin
data class Complex(val real: Double, val imaginary: Double)
```

W matematyce istnieje szereg operacji, które możemy wykonać na liczbach zespolonych. Na przykład możemy dodać dwie liczby zespolone lub odjąć liczbę zespoloną od innej liczby zespolonej. Robi się to za pomocą operatorów `+` i `-`. W związku z tym rozsądne jest, abyśmy obsługiwali te operatory dla naszej klasy `Complex`. Aby obsłużyć operator `+`, musimy zdefiniować metodę mającą modyfikator `operator` o nazwie `plus` i pojedynczy parametr. Aby obsłużyć operator `-`, musimy zdefiniować metodę mającą modyfikator `operator` o nazwie `minus` i pojedynczy parametr.

```kotlin
data class Complex(val real: Double, val imaginary: Double) {

    operator fun plus(another: Complex) = Complex(
        real + another.real,
        imaginary + another.imaginary
    )

    operator fun minus(another: Complex) = Complex(
        real = real - another.real,
        imaginary = imaginary - another.imaginary
    )
}

// przykład użycia
fun main() {
    val c1 = Complex(1.0, 2.0)
    val c2 = Complex(2.0, 3.0)
    println(c1 + c2) // Complex(real=3.0, imaginary=5.0)
    println(c2 - c1) // Complex(real=1.0, imaginary=1.0)
}
```

Użycie operatorów `+` i `-` jest równoznaczne z wywołaniem funkcji `plus` i `minus`. Te dwa można stosować zamiennie.

```kotlin
c1 + c2 // pod spodem jest c1.plus(c2)
c1 - c2 // pod spodem jest c1.minus(c2)
```

Kotlin definiuje konkretny zestaw operatorów, dla każdego z nich istnieje określona nazwa i liczba obsługiwanych argumentów. Dodatkowo, wszystkie operatory muszą być metodami (czyli albo funkcją członkowską, albo funkcją rozszerzenia), a te metody muszą mieć modyfikator `operator`.

Dobrze używane operatory mogą pomóc nam poprawić czytelność kodu tak samo, jak źle używane operatory mogą jej zaszkodzić[^18_1]. Przedyskutujmy wszystkie operatory Kotlin.

### Operatory arytmetyczne

Zacznijmy od operatorów arytmetycznych, takich jak plus czy razy. Te są najłatwiejsze dla kompilatora Kotlin, ponieważ musi tylko przekształcić lewą kolumnę na prawą.

| Wyrażenie | Przekształca się na |
|-----------|--------------------|
| `a + b`   | `a.plus(b)`        |
| `a - b`   | `a.minus(b)`       |
| `a * b`   | `a.times(b)`       |
| `a / b`   | `a.div(b)`         |
| `a % b`   | `a.rem(b)`         |
| `a..b `   | `a.rangeTo(b)`     |

Zauważ, że `%` przekształca się na `rem`, co jest skrótem od "reszta". Ten operator zwraca resztę pozostałą po podzieleniu jednego operandu przez drugi operand, więc jest podobny do operacji modulo[^18_0].

```kotlin
fun main() {
    println(13 % 4) // 1
    println(7 % 4)  // 3
    println(1 % 4)  // 1
    println(0 % 4)  // 0
    println(-1 % 4) // -1
    println(-5 % 4) // -1
    println(-7 % 4) // -3
}
```

Innym interesującym operatorem jest `rangeTo`, dzięki któremu można tworzyć zakres, używając dwóch kropek między dwoma wartościami. Gdy używamy `rangeTo` między dwoma liczbami typu `Int`, wynikiem jest `IntRange`. Aby utworzyć `ClosedRange`, można użyć `..` między dowolnymi dwoma liczbami, które są porównywalne.

```kotlin
fun main() {
    val intRange: IntRange = 1..10
    val comparableRange: ClosedRange<String> = "A".."Z"
}
```

### Operator rangeUntil

Kotlin 1.7.20 wprowadził eksperymentalne wsparcie dla nowego operatora o nazwie `rangeUntil`, który jest zasadniczo zamiennikiem funkcji `until`. Jest on zaimplementowany za pomocą funkcji `rangeUntil` i można go używać z operatorem `..<`.

```kotlin
fun main() {
    for (a in 1..<5) {
        println(a)
    }
}
// 1
// 2
// 3
// 4
```

### Operator `in`

Jednym z moich ulubionych operatorów jest `in`. Wyrażenie `a in b` przekształca się na `b.contains(a)`. Istnieje także `!in`, które przekształca się na negację.

| Wyrażenie | Przekształca się na |
|-----------|--------------------|
| `a in b`  | `b.contains(a)`    |
| `a !in b` | `!b.contains(a)`   |

Jest kilka sposobów użycia tego operatora. Po pierwsze, dla kolekcji, zamiast sprawdzać, czy lista zawiera element, można sprawdzić, czy element znajduje się na liście.

```kotlin
fun main() {
    val letters = setOf("A", "B", "C")
    println("A" in letters) // true
    println("D" in letters) // false
    println(letters.contains("A")) // true
    println(letters.contains("D")) // false
}
```

Dlaczego by to robić? Głównie dla czytelności. Czy zapytałbyś "Czy lodówka zawiera piwo?" czy "Czy w lodówce jest piwo?"? Użycie operatora `in` daje nam możliwość wyboru.

Często używamy również operatora `in` razem z zakresami. Wyrażenie `1..10` generuje obiekt typu `IntRange`, który ma metodę `contains`. Dlatego można użyć `in` i zakresu, aby sprawdzić, czy liczba znajduje się w tym zakresie.

```kotlin
fun main() {
    println(5 in 1..10) // true
    println(11 in 1..10) // false
}
```

Można utworzyć zakres z dowolnych obiektów, które są porównywalne, a wynikowy `ClosedRange` również ma metodę `contains`. Dlatego można użyć sprawdzania zakresu dla dowolnych obiektów, które są porównywalne, takich jak duże liczby czy obiekty reprezentujące czas.

```kotlin
import java.math.BigDecimal
import java.time.LocalDateTime

fun main() {
    val amount = BigDecimal("42.80")
    val minPrice = BigDecimal("5.00")
    val maxPrice = BigDecimal("100.00")
    val correctPrice = amount in minPrice..maxPrice
    println(correctPrice) // true

    val now = LocalDateTime.now()
    val actionStarts = LocalDateTime.of(1410, 7, 15, 0, 0)
    val actionEnds = actionStarts.plusDays(1)
    println(now in actionStarts..actionEnds) // false
}
```

### Operator iterator

Można użyć pętli for do iterowania po dowolnym obiekcie, który ma metodę operatora `iterator`. Każdy obiekt implementujący interfejs `Iterable` musi obsługiwać metodę `iterator`.

```kotlin
public interface Iterable<out T> {
    /**
     * Zwraca iterator po elementach tego obiektu.
     */
    public operator fun iterator(): Iterator<T>
}
```

Można zdefiniować obiekty, które można iterować, ale nie implementują interfejsu `Iterable`. `Map` jest świetnym przykładem. Nie implementuje interfejsu `Iterable`, jednak można iterować po niej używając pętli for. Jak to możliwe? Dzięki operatorowi `iterator`, który jest zdefiniowany jako funkcja rozszerzenia w bibliotece standardowej Kotlin.

```kotlin
// Część biblioteki standardowej Kotlin
inline operator fun <K, V>
Map<out K, V>.iterator(): Iterator<Map.Entry<K, V>> =
    entries.iterator()

fun main() {
    val map = mapOf('a' to "Alex", 'b' to "Bob")
    for ((letter, name) in map) {
        println("$letter jak w $name")
    }
}
// a jak w Alex
// b jak w Bob
```

Aby lepiej zrozumieć, jak działa pętla for, rozważ poniższy kod.

```kotlin
fun main() {
    for (e in Tree()) {
        // ciało
    }
}

class Tree {
    operator fun iterator(): Iterator<String> = ...
}
```

Pod maską pętla for jest kompilowana do bajtkodu, który używa pętli while do iteracji po iteratorze obiektu, jak pokazano na poniższym fragmencie kodu.

```kotlin
fun main() {
    val iterator = Tree().iterator()
    while (iterator.hasNext()) {
        val e = iterator.next()
        // ciało
    }
}
```

### Operatory równości i nierówności

W Kotlinie występują dwa rodzaje równości:

* Równość strukturalna - sprawdzana za pomocą metody `equals` lub operatora `==` (i jego negowanej wersji `!=`). `a == b` przekłada się na `a.equals(b)` gdy `a` nie jest nullowanych, w przeciwnym razie przekłada się na `a?.equals(b) ?: (b === null)`. Równość strukturalna jest zwykle preferowana nad równością referencyjną. Metodę `equals` można nadpisać w niestandardowej klasie.

* Równość referencyjna - sprawdzana za pomocą operatora `===` (i jego negowanej wersji `!==`); zwraca `true`, gdy obie strony wskazują na ten sam obiekt. `===` i `!==` (sprawdzenia tożsamości) nie są przeciążalne.

Ponieważ `equals` jest zaimplementowane w `Any`, które jest nadklasą każdej klasy, możemy sprawdzić równość dowolnych dwóch obiektów.

| Wyrażenie | Przekłada się na                   |
|-----------|-------------------------------------|
| `a == b`  | `a?.equals(b) ?: (b === null)`      |
| `a != b`  | `!(a?.equals(b) ?: (b === null))`   |

### Operatory porównania

Niektóre klasy mają naturalny porządek, który jest używany domyślnie, gdy porównujemy dwie instancje danej klasy. Dobrym przykładem są liczby: 10 jest mniejszą liczbą niż 100. Istnieje popularna konwencja w Javie, że klasy o naturalnym porządku powinny implementować interfejs `Comparable`, który wymaga metody `compareTo`, która służy do porównywania dwóch obiektów.

```kotlin
public interface Comparable<in T> {
    /**
     * Porównuje ten obiekt z określonym obiektem pod
     * względem kolejności. Zwraca zero, jeśli ten obiekt
     * jest równy określonemu obiektowi [other], liczbę
     * ujemną, jeśli jest mniejszy niż [other] lub liczbę
     * dodatnią, jeśli jest większy niż [other].
     */
    public operator fun compareTo(other: T): Int
}
```

W rezultacie istnieje konwencja, że powinniśmy porównywać dwa obiekty za pomocą metody `compareTo`. Jednak bezpośrednie użycie metody `compareTo` nie jest bardzo intuicyjne. Powiedzmy, że widzisz `a.compareTo(b) > 0` w kodzie. Co to oznacza? Kotlin upraszcza to, czyniąc z `compareTo` operatora, który może być zastąpiony intuicyjnymi matematycznymi operatorami porównania: `>`, `<`, `>=` i `<=`.

| Wyrażenie | Tłumaczy się na       |
|-----------|-----------------------|
| `a > b`   | `a.compareTo(b) > 0`  |
| `a < b`   | `a.compareTo(b) < 0`  |
| `a >= b`  | `a.compareTo(b) >= 0` |
| `a <= b`  | `a.compareTo(b) <= 0` |

Często używam operatorów porównania do porównywania wartości przechowywanych w obiektach typu `BigDecimal` lub `BigInteger`.

```kotlin
import java.math.BigDecimal

fun main() {
    val amount1 = BigDecimal("42.80")
    val amount2 = BigDecimal("5.00")
    println(amount1 > amount2) // true
    println(amount1 >= amount2) // true
    println(amount1 < amount2) // false
    println(amount1 <= amount2) // false
    println(amount1 > amount1) // false
    println(amount1 >= amount1) // true
    println(amount1 < amount2) // false
    println(amount1 <= amount2) // false
}
```

Lubię również porównywać odniesienia czasowe w ten sam sposób.

```kotlin
import java.time.LocalDateTime

fun main() {
    val now = LocalDateTime.now()
    val actionStarts = LocalDateTime.of(2010, 10, 20, 0, 0)
    val actionEnds = actionStarts.plusDays(1)
    println(now > actionStarts) // true
    println(now <= actionStarts) // false
    println(now < actionEnds) // false
    println(now >= actionEnds) // true
}
```

### Operator indeksowania

W programowaniu istnieją dwie popularne konwencje pozwalające na pobieranie lub ustawianie elementów w kolekcjach. Pierwsza z nich używa nawiasów kwadratowych, a druga metod `get` i `set`. W Javie pierwszą konwencję stosujemy dla tablic, a drugą dla innych rodzajów kolekcji. W Kotlinie obie konwencje można stosować wymiennie, ponieważ metody `get` i `set` są operatorami, które można używać z nawiasami kwadratowymi.

| Wyrażenie              | Tłumaczy się na          |
|------------------------|--------------------------|
| `a[i]`                 | `a.get(i)`               |
| `a[i, j]`              | `a.get(i, j)`            |
| `a[i_1, ..., i_n]`     | `a.get(i_1, ..., i_n)`   |
| `a[i] = b`             | `a.set(i, b)`            |
| `a[i, j] = b`          | `a.set(i, j, b)`         |
| `a[i_1, ..., i_n] = b` | `a.set(i_1, ..., i_n, b)`|

```kotlin
fun main() {
    val mutableList = mutableListOf("A", "B", "C")
    println(mutableList[1]) // B
    mutableList[2] = "D"
    println(mutableList) // [A, B, D]

    val animalFood = mutableMapOf(
        "Dog" to "Meat",
        "Goat" to "Grass"
    )
    println(animalFood["Dog"]) // Meat
    animalFood["Cat"] = "Meat"
    println(animalFood["Cat"]) // Meat
}
```

Nawiasy kwadratowe są tłumaczone na wywołania `get` i `set` z odpowiednią liczbą argumentów. Warianty funkcji `get` i `set` z większą liczbą argumentów mogą być używane przez biblioteki przetwarzania danych. Na przykład możemy mieć obiekt reprezentujący tabelę i używać nawiasów kwadratowych z dwoma argumentami: współrzędnymi `x` i `y`.

### Przypisania rozszerzone

Gdy ustawiamy nową wartość dla zmiennej, ta nowa wartość często opiera się na poprzedniej wartości. Na przykład możemy chcieć dodać wartość do poprzedniej. W tym celu wprowadzono przypisania rozszerzone[^18_3]. Na przykład `a += b` to krótsze oznaczenie `a = a + b`. Istnieją podobne oznaczenia dla innych operacji arytmetycznych.

| Wyrażenie | Tłumaczy się na |
|-----------|-----------------|
| `a += b`  | `a = a + b`     |
| `a -= b`  | `a = a - b`     |
| `a *= b`  | `a = a * b`     |
| `a /= b`  | `a = a / b`     |
| `a %= b`  | `a = a % b`     |

Zauważ, że przypisania rozszerzone można używać dla wszystkich typów, które obsługują odpowiednią operację arytmetyczną, w tym dla list czy łańcuchów znaków. Takie przypisania rozszerzone wymagają, aby zmienna była zmienną do odczytu i zapisu, czyli `var`, a wynik operacji matematycznej musi mieć właściwy typ (aby przetłumaczyć `a += b` na `a = a + b`, zmienna `a` musi być `var`, a `a + b` musi być podtypem typu `a`).

```kotlin
fun main() {
    var str = "ABC"
    str += "D" // tłumaczy się na str = str + "D"
    println(str) // ABCD

    var l = listOf("A", "B", "C")
    l += "D" // tłumaczy się na l = l + "D"
    println(l) // [A, B, C, D]
}
```

Przypisania rozszerzone można stosować w inny sposób: do modyfikowania obiektów zmiennych. Na przykład możemy użyć `+=` do dodania elementu do zmiennej listy. W takim przypadku `a += b` tłumaczy się na `a.plusAssign(b)`.

| Wyrażenie | Tłumaczy się na      |
|-----------|----------------------|
| `a += b`  | `a.plusAssign(b)`    |
| `a -= b`  | `a.minusAssign(b)`   |
| `a *= b`  | `a.timesAssign(b)`   |
| `a /= b`  | `a.divAssign(b)`     |
| `a %= b`  | `a.remAssign(b)`     |

```kotlin
fun main() {
    val names = mutableListOf("Jake", "Ben")
    names += "Jon"
    names -= "Ben"
    println(names) // [Jake, Jon]

    val tools = mutableMapOf(
        "Grass" to "Lawnmower",
        "Nail" to "Hammer"
    )
    tools += "Screw" to "Screwdriver"
    tools -= "Grass"
    println(tools) // {Nail=Hammer, Screw=Screwdriver}
}
```

Jeśli oba rodzaje rozszerzonego przypisania mogą być zastosowane, domyślnie Kotlin wybiera modyfikację obiektu zmiennego.

### Jednoargumentowe operatory przedrostkowe

Plus, minus lub negacja przed pojedynczą wartością to także operator. Operatory używane tylko z jedną wartością nazywane są **operatorami jednoargumentowymi**[^18_4]. Kotlin obsługuje przeciążanie operatorów dla następujących operatorów jednoargumentowych:

| Wyrażenie | Tłumaczenie na   |
|------------|------------------|
| `+a`       | `a.unaryPlus()`  |
| `-a`       | `a.unaryMinus()` |
| `!a`       | `a.not()`        |

Oto przykład przeciążania operatora `unaryMinus`.

```kotlin
data class Point(val x: Int, val y: Int)

operator fun Point.unaryMinus() = Point(-x, -y)

fun main() {
    val point = Point(10, 20)
    println(-point)  // wyświetla "Point(x=-10, y=-20)"
}
```

Operator `unaryPlus` jest często używany jako część Kotlin DSL, które są opisane szczegółowo w następnej książce tej serii, *Funkcyjny Kotlin*.

### Inkrementacja i dekrementacja

W ramach wielu algorytmów używanych w starszych językach często musieliśmy dodawać lub odejmować wartość `1` od zmiennej, dlatego wynaleziono inkrementację i dekrementację. Operator `++` służy do dodawania `1` do zmiennej; więc jeśli `a` jest liczbą całkowitą, to `a++` przekłada się na `a = a + 1`. Operator `--` służy do odejmowania `1` od zmiennej; więc jeśli `a` jest liczbą całkowitą, to `a--` przekłada się na `a = a - 1`.

Zarówno inkrementacja, jak i dekrementacja mogą być używane przed lub po zmiennej, a to determinuje wartość zwracaną przez tę operację.
* Jeśli użyjesz `++` **przed** zmienną, nazywa się to **preinkrementacja**; inkrementuje zmienną, a następnie zwraca wynik tej operacji.
* Jeśli użyjesz `++` **po** zmiennej, nazywa się to **postinkrementacja**; inkrementuje zmienną, ale zwraca wartość przed operacją.
* Jeśli użyjesz `--` **przed** zmienną, nazywa się to **predekrementacja**; dekrementuje zmienną, a następnie zwraca wynik tej operacji.
* Jeśli użyjesz `--` **po** zmiennej, nazywa się to **postdekrementacja**; dekrementuje zmienną, ale zwraca wartość przed operacją.

```kotlin
fun main() {
    var i = 10
    println(i++) // 10
    println(i) // 11
    println(++i) // 12
    println(i) // 12

    i = 10
    println(i--) // 10
    println(i) // 9
    println(--i) // 8
    println(i) // 8
}
```

Na podstawie metod `inc` i `dec` Kotlin obsługuje przeciążanie inkrementacji i dekrementacji, które powinny inkrementować lub dekrementować niestandardowy obiekt. Nigdy nie widziałem, aby ta funkcja była używana w praktyce, więc myślę, że wystarczy wiedzieć, że istnieje.

| Wyrażenie | Tłumaczenie na (uproszczone) |
|------------|------------------------------|
| `++a`      | `a.inc(); a`                 |
| `a++`      | `val tmp = a; a.inc(); tmp`  |
| `--a`      | `a.dec(); a`                 |
| `a--`      | `val tmp = a; a.dec(); tmp`  |

### Operator invoke

Obiekty z operatorem `invoke` można wywoływać jak funkcje, czyli z nawiasami bezpośrednio po zmiennej reprezentującej ten obiekt. Wywołanie obiektu przekłada się na wywołanie metody `invoke` z takimi samymi argumentami.

| Wyrażenie         | Tłumaczenie na            |
|--------------------|---------------------------|
| `a()`              | `a.invoke()`              |
| `a(i)`             | `a.invoke(i)`             |
| `a(i, j)`          | `a.invoke(i, j)`          |
| `a(i_1, ..., i_n)` | `a.invoke(i_1, ..., i_n)` |

Operator `invoke` jest używany dla obiektów reprezentujących funkcje, takie jak wyrażenia lambda[^18_2] lub obiekty UseCases z Clean Architecture.

```kotlin
class CheerUseCase {
    operator fun invoke(who: String) {
        println("Hello, $who")
    }
}

fun main() {
    val hello = {
        println("Hello")
    }
    hello() // Hello

    val cheerUseCase = CheerUseCase()
    cheerUseCase("Czytelniku") // Hello, Czytelniku
}
```

### Priorytet

Jaki jest wynik wyrażenia `1 + 2 * 3`? Odpowiedź brzmi `7`, a nie `9`, ponieważ w matematyce mnożymy przed dodawaniem. Mówimy, że mnożenie ma wyższy priorytet niż dodawanie.

Priorytet jest również niezwykle ważny w programowaniu, ponieważ gdy kompilator ocenia wyrażenie takie jak `1 + 2 == 3`, musi wiedzieć, czy powinien najpierw dodać `1` do `2`, czy porównać `2` i `3`. Poniższa tabela porównuje priorytet wszystkich operatorów, w tym tych, które można przeciążyć, i tych, których nie można.

| Priorytet | Tytuł                       | Symbole                                                 |
|-----------|-----------------------------|---------------------------------------------------------|
| Najwyższy | Postfiksowy                 | ++, --, . (zwykłe wywołanie), ?. (bezpieczne wywołanie) |
|           | Prefiksowy                  | -, +, ++, --, !                                         |
|           | Rzutowanie typów            | as, as?                                                 |
|           | Mnożenie                    | *, /, %                                                 |
|           | Dodawanie                   | +, -                                                    |
|           | Zakres                      | ..                                                      |
|           | Funkcja infiksowa           | simpleIdentifier                                        |
|           | Elvis                       | ?:                                                      |
|           | Sprawdzenia nazwane         | in, !in, is, !is                                        |
|           | Porównanie                  | <, >, <=, >=                                            |
|           | Równość                     | ==, !=, ===, !==                                        |
|           | Koniunkcja                  | &&                                                      |
|           | Alternatywa                 | \|\|                                                    |
|           | Operator rozprzestrzeniania | *                                                       |
| Najniższy | Przypisanie                 | =, +=, -=, *=, /=, %=                                   |

Czy na podstawie tej tabeli potrafisz przewidzieć, co wydrukuje poniższy kod?

```kotlin
fun main() {
    println(-1.plus(1))
}
```

To popularna zagadka Kotlin. Odpowiedź brzmi `-2`, a nie `0`, ponieważ pojedynczy minus przed funkcją jest operatorem, którego priorytet jest niższy niż wywołanie metody `plus`. Najpierw wywołujemy metodę, a następnie wywołujemy `unaryMinus` na wyniku, więc zmieniamy z `2` na `-2`. Aby użyć dosłownie `-1`, umieść go w nawiasach.

```kotlin
fun main() {
    println((-1).plus(1)) // 0
}
```

### Podsumowanie

W Kotlin używamy wielu operatorów, z których wiele można przeciążyć. Można to wykorzystać do poprawy czytelności naszego kodu. Z poznawczego punktu widzenia, używanie intuicyjnego operatora może być ogromną poprawą w porównaniu z używaniem wszędzie metod. Dlatego warto wiedzieć, jakie opcje są dostępne i być otwartym na używanie operatorów zdefiniowanych przez Kotlin stdlib, ale również warto umieć zdefiniować własne operatory.

[^18_0]: Ten operator wcześniej nazywał się `mod`, co pochodzi od "modulo", ale teraz ta nazwa jest przestarzała. W matematyce zarówno operacje reszty, jak i modulo działają tak samo dla liczb dodatnich, ale różnica polega na liczbach ujemnych. Wynik -5 reszty 4 to -1, ponieważ -5 = 4 * (-1) + (-1). Wynik -5 modulo 4 to 3, ponieważ -5 = 4 * (-2) + 3. Operator `%` w Kotlinie implementuje zachowanie reszty, dlatego jego nazwa musiała zostać zmieniona z `mod` na `rem`.
[^18_1]: Więcej na ten temat można znaleźć w *Effective Kotlin*, *Pozycja 12: Znaczenie operatora powinno być zgodne z nazwą funkcji* i *Pozycja 13: Używaj operatorów, aby zwiększyć czytelność*.
[^18_2]: Więcej o wyrażeniach lambda będzie w kolejnej książce serii, *Functional Kotlin*.
[^18_3]: Nie jestem pewien, który język wprowadził pierwszy przypisania rozszerzone, ale są one obsługiwane nawet przez tak stare języki jak C.
[^18_4]: Operatory jednoargumentowe są używane tylko z jedną wartością (operandy). Operatory używane z dwoma wartościami nazywane są operatorami binarnymi; jednak, ponieważ większość operatorów jest binarna, ten typ często traktowany jest jako domyślny. Operatory używane z trzema wartościami nazywane są operatorami trójargumentowymi. Ponieważ w głównych językach programowania istnieje tylko jeden operator trójargumentowy, mianowicie **operator warunkowy**, często nazywany jest **ternarym operatorem**.
