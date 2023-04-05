## Podstawowe typy, ich literały i operacje

Każdy język potrzebuje wygodnego sposobu reprezentowania podstawowych rodzajów wartości, takich jak liczby czy znaki. Wszystkie języki muszą mieć wbudowane **typy** i **literały**. Typy są używane do reprezentowania pewnych rodzajów wartości. Przykłady typów to `Int`, `Boolean` czy `String`. Literały to wbudowane notacje, które są używane do tworzenia instancji. Przykłady literałów to literał łańcuchowy, który to tekst w cudzysłowach, czy literał całkowitoliczbowy, który to goła liczba.

W tym rozdziale poznamy podstawowe typy Kotlin i ich literały:
* liczby (`Int`, `Long`, `Double`, `Float`, `Short`, `Byte`),
* wartości logiczne (`Boolean`),
* znaki (`Char`),
* łańcuchy znaków (`String`).

W Kotlin istnieje także prymitywny typ tablicy, który zostanie omówiony w rozdziale *Kolekcje*.

W Kotlin wszystkie wartości są traktowane jako obiekty (nie ma typów prymitywnych), więc wszystkie mają metody, a ich typy mogą być używane jako argumenty typów generycznych (to zostanie omówione później). Typy reprezentujące liczby, wartości logiczne i znaki mogą być zoptymalizowane przez kompilator Kotlin i używane jako typy prymitywne, ale ta optymalizacja nie wpływa na programistów Kotlin, dlatego nie musisz nawet o tym myśleć.

Zacznijmy omawiać podstawowe typy w Kotlin, jeden po drugim.

### Liczby

W Kotlin istnieje szereg różnych typów służących do reprezentowania liczb. Mogą być one podzielone na te reprezentujące liczby całkowite (bez miejsc dziesiętnych) oraz te reprezentujące liczby zmiennoprzecinkowe (z miejscami dziesiętnymi). W tych grupach różnica polega na liczbie bitów używanych do reprezentowania tych liczb, co determinuje możliwy rozmiar liczby i precyzję.

Aby reprezentować liczby całkowite, używamy `Int`, `Long`, `Byte` i `Short`.

| Typ     | Rozmiar (bity) | Wartość min | Wartość max   |
|---------|---------------|-------------|----------------|
| `Byte`  | 8             | -128        | 127            |
| `Short` | 16            | -32768      | 32767          |
| `Int`   | 32            | `-2^{31}`$  | `2^{31} - 1`$  |
| `Long`  | 64            | `-2^{63}`$  | `2^{63} - 1`$  |

Aby reprezentować liczby zmiennoprzecinkowe, używamy `Float` i `Double`.

| Typ      | Rozmiar (bity) | Bity znaczące | Bity wykładnika | Cyfry dziesiętne |
|----------|----------------|---------------|-----------------|------------------|
| `Float`  | 32             | 24            | 8               | 6-7              |
| `Double` | 64             | 53            | 11              | 15-16            |

Zwykła liczba bez kropki dziesiętnej jest interpretowana jako `Int`. Zwykła liczba z kropką dziesiętną jest interpretowana jako `Double`.

![](05_int_double.png)

Możesz utworzyć `Long`, dodając przyrostek `L` po liczbie. `Long` jest również używany dla literałów liczbowych, które są zbyt duże dla `Int`.

![](05_long.png)

Podobnie, możesz utworzyć `Float`, kończąc liczbę przyrostkiem `F` lub `f`.

![](05_float.png)

Nie ma przyrostka do tworzenia typów `Byte` ani `Short`. Jednak liczba wyraźnie określona jako jeden z tych typów stworzy instancję tego typu. To samo dotyczy `Long`.

```kotlin
fun main() {
    val b: Byte = 123
    val s: Short = 345
    val l: Long = 345
}
```


To nie jest konwersja! Kotlin nie obsługuje niejawnej konwersji typów, więc nie można użyć `Byte` ani `Long`, gdzie oczekiwany jest `Int`.

![](05_int_long_error.png)

Jeśli musimy jawnie przekształcić jedną liczbę na inny typ, używamy jawnego funkcji konwersji, takich jak `toInt` lub `toLong`.

```kotlin
fun main() {
    val b: Byte = 123
    val l: Long = 123L
    val i: Int = 123

    val i1: Int = b.toInt()
    val i2: Int = l.toInt()
    val l1: Long = b.toLong()
    val l2: Long = i.toLong()
}
```

#### Podkreślenia w liczbach

W literałach liczbowych możemy użyć podkreślenia `_` pomiędzy cyframi. Ten znak jest ignorowany, ale czasami używamy go do formatowania długich liczb dla lepszej czytelności.

```kotlin
fun main() {
    val million = 1_000_000
    println(million) // 1000000
}
```

#### Inne systemy liczbowe

Aby zdefiniować liczbę w systemie szesnastkowym, zacznij od `0x`. Aby zdefiniować liczbę w systemie binarnym, zacznij od `0b`. System ósemkowy nie jest obsługiwany.

```kotlin
fun main() {
    val hexBytes = 0xA4_D6_FE_FE
    println(hexBytes) // 2765553406
    val bytes = 0b01010010_01101101_11101000_10010010
    println(bytes) // 1382934674
}
```

#### `Number` i funkcje konwersji

Wszystkie podstawowe typy reprezentujące liczby są podtypem typu `Number`.

```kotlin
fun main() {
    val i: Int = 123
    val b: Byte = 123
    val l: Long = 123L

    val n1: Number = i
    val n2: Number = b
    val n3: Number = l
}
```

Typ `Number` określa funkcje przekształcania: z bieżącej liczby na dowolny inny podstawowy typ reprezentujący liczbę.

```kotlin
abstract class Number {
    abstract fun toDouble(): Double
    abstract fun toFloat(): Float
    abstract fun toLong(): Long
    abstract fun toInt(): Int
    abstract fun toChar(): Char
    abstract fun toShort(): Short
    abstract fun toByte(): Byte
}
```

Oznacza to, że dla każdej podstawowej liczby można przekształcić ją na inną podstawową liczbę za pomocą funkcji `to{new type}`. Takie funkcje są znane jako *funkcje konwersji*.

```kotlin
fun main() {
    val b: Byte = 123
    val l: Long = b.toLong()
    val f: Float = l.toFloat()
    val i: Int = f.toInt()
    val d: Double = i.toDouble()
    println(d) // 123.0
}
```

#### Operacje na liczbach

Liczby w Kotlin obsługują podstawowe operacje matematyczne:
* dodawanie (`+`),
* odejmowanie (`-`),
* mnożenie (`*`),
* dzielenie (`/`).

```kotlin
fun main() {
    val i1 = 12
    val i2 = 34
    println(i1 + i2) // 46
    println(i1 - i2) // -22
    println(i1 * i2) // 408
    println(i1 / i2) // 0

    val d1 = 1.4
    val d2 = 2.5
    println(d1 + d2) // 3.9
    println(d1 - d2) // -1.1
    println(d1 * d2) // 3.5
    println(d1 / d2) // 0.5599999999999999
}
```

> Zauważ, że poprawny wynik `1.4 / 2.5` powinien wynosić `0.56`, a nie `0.5599999999999999`. Ten problem zostanie rozwiązany wkrótce.

Uważaj, że gdy dzielimy `Int` przez `Int`, wynik jest również `Int`, więc część dziesiętna jest tracona.

```kotlin
fun main() {
    println(5 / 2) // 2, not 2.5
}
```

Rozwiązaniem jest najpierw przekształcenie liczby całkowitej na reprezentację zmiennoprzecinkową, a następnie jej podzielenie.

```kotlin
fun main() {
    println(5.toDouble() / 2) // 2.5
}
```

Istnieje również operator reszty[^04_2] `%`:

```kotlin
fun main() {
    println(1 % 3) // 1
    println(2 % 3) // 2
    println(3 % 3) // 0
    println(4 % 3) // 1
    println(5 % 3) // 2
    println(6 % 3) // 0
    println(7 % 3) // 1
    println(0 % 3) // 0
    println(-1 % 3) // -1
    println(-2 % 3) // -2
    println(-3 % 3) // 0
}
```

Kotlin obsługuje również operacje modyfikujące zmienną odczyt-zapis `var`:
* `+=`, gdzie `a += b` jest równoznaczne z `a = a + b`,
* `-=`, gdzie `a -= b` jest równoznaczne z `a = a - b`,
* `*=`, gdzie `a *= b` jest równoznaczne z `a = a * b`,
* `/=`, gdzie `a /= b` jest równoznaczne z `a = a / b`,
* `%=`, gdzie `a %= b` jest równoznaczne z `a = a % b`,
* post-inkrementacja i pre-inkrementacja `++`, które zwiększają wartość zmiennej o `1`,
* post-dekrementacja i pre-dekrementacja `--`, które zmniejszają wartość zmiennej o `1`.

```kotlin
fun main() {
    var i = 1
    println(i) // 1
    i += 10
    println(i) // 11
    i -= 5
    println(i) // 6
    i *= 3
    println(i) // 18
    i /= 2
    println(i) // 9
    i %= 4
    println(i) // 1

    // Post-inkrementacja
    // zwiększa wartość i zwraca poprzednią wartość
    println(i++) // 1
    println(i) // 2

    // Pre-inkrementacja
    // zwiększa wartość i zwraca nową wartość
    println(++i) // 3
    println(i) // 3

    // Post-dekrementacja
    // zmniejsza wartość i zwraca poprzednią wartość
    println(i--) // 3
    println(i) // 2

    // Pre-dekrementacja
    // zmniejsza wartość i zwraca nową wartość
    println(--i) // 1
    println(i) // 1
}
```

#### Operacje na bitach

Kotlin obsługuje również operacje na bitach za pomocą następujących metod, które można wywoływać za pomocą notacji infiksowej (czyli między dwiema wartościami):
* `and` zachowuje tylko bity, które mają `1` w tych samych pozycjach binarnych w obu liczbach.
* `or` zachowuje tylko bity, które mają `1` w tych samych pozycjach binarnych w jednej lub obu liczbach.
* `xor` zachowuje tylko bity, które mają dokładnie jedno `1` w tych samych pozycjach binarnych w obu liczbach.
* `shl` przesuwa wartość po lewej stronie w lewo o prawą ilość bitów.
* `shr` przesuwa wartość po lewej stronie w prawo o prawą ilość bitów.

```kotlin
fun main() {
    println(0b0101 and 0b0001) // 1, czyli 0b0001
    println(0b0101 or 0b0001)  // 5, czyli 0b0101
    println(0b0101 xor 0b0001) // 4, czyli 0b0100
    println(0b0101 shl 1) // 10, czyli 0b1010
    println(0b0101 shr 1) // 2,  czyli 0b0010
}
```

#### `BigDecimal` i `BigInteger`

Wszystkie podstawowe typy w Kotlin mają ograniczony rozmiar i precyzję, co może prowadzić do nieprecyzyjnych lub błędnych wyników w niektórych sytuacjach.

```kotlin
fun main() {
    println(0.1 + 0.2) // 0.30000000000000004
    println(2147483647 + 1) // -2147483648
}
```

To standardowy kompromis w programowaniu, z którym w większości przypadków musimy się pogodzić. Jednak są przypadki, gdy potrzebujemy mieć doskonałą precyzję i nieograniczony rozmiar liczby. Na JVM, dla nieograniczonego rozmiaru liczby powinniśmy użyć `BigInteger`, który reprezentuje liczbę bez części dziesiętnej. Dla nieograniczonego rozmiaru i precyzji powinniśmy użyć `BigDecimal`, który reprezentuje liczbę mającą część dziesiętną. Oba można utworzyć za pomocą konstruktorów[^04_1], funkcji fabrycznych (takich jak `valueOf`) lub konwersji z podstawowych typów reprezentujących liczby (metody `toBigDecimal` i `toBigInteger`).

```kotlin
import java.math.BigDecimal
import java.math.BigInteger

fun main() {
    val i = 10
    val l = 10L
    val d = 10.0
    val f = 10.0F

    val bd1: BigDecimal = BigDecimal(123)
    val bd2: BigDecimal = BigDecimal("123.00")
    val bd3: BigDecimal = i.toBigDecimal()
    val bd4: BigDecimal = l.toBigDecimal()
    val bd5: BigDecimal = d.toBigDecimal()
    val bd6: BigDecimal = f.toBigDecimal()
    val bi1: BigInteger = BigInteger.valueOf(123)
    val bi2: BigInteger = BigInteger("123")
    val bi3: BigInteger = i.toBigInteger()
    val bi4: BigInteger = l.toBigInteger()
}
```

`BigDecimal` i `BigInteger` obsługują również podstawowe operatory matematyczne:

```kotlin
import java.math.BigDecimal
import java.math.BigInteger

fun main() {
    val bd1 = BigDecimal("1.2")
    val bd2 = BigDecimal("3.4")
    println(bd1 + bd2) // 4.6
    println(bd1 - bd2) // -2.2
    println(bd1 * bd2) // 4.08
    println(bd1 / bd2) // 0.4

    val bi1 = BigInteger("12")
    val bi2 = BigInteger("34")
    println(bi1 + bi2) // 46
    println(bi1 - bi2) // -22
    println(bi1 * bi2) // 408
    println(bi1 / bi2) // 0
}
```

Na platformach innych niż Kotlin/JVM do reprezentowania liczb o nieograniczonym rozmiarze i precyzji potrzebne są zewnętrzne biblioteki.

### Boole

Innym podstawowym typem jest `Boolean`, który ma dwie możliwe wartości: `true` i `false`.

```kotlin
fun main() {
    val b1: Boolean = true
    println(b1) // true
    val b2: Boolean = false
    println(b2) // false
}
```

Używamy wartości logicznych do wyrażania odpowiedzi tak/nie, na przykład:
* Czy użytkownik jest administratorem?
* Czy użytkownik zaakceptował politykę plików cookie?
* Czy dwie liczby są identyczne?

W praktyce wartości logiczne są często wynikiem pewnego rodzaju porównania.

#### Równość

Wartość `Boolean` często jest wynikiem porównania równości. W Kotlin porównujemy dwa obiekty pod względem równości, używając podwójnego znaku równości `==`. Aby sprawdzić, czy dwa obiekty nie są równe, używamy znaku nierówności `!=`.

```kotlin
fun main() {
    println(10 == 10) // true
    println(10 == 11) // false
    println(10 != 10) // false
    println(10 != 11) // true
}
```

Liczby i wszystkie obiekty, które można porównać (tzn. implementują interfejs `Comparable`), można również porównać za pomocą `>`, `<`, `>=` i `<=`.

```kotlin
fun main() {
    println(10 > 10) // false
    println(10 > 11) // false
    println(11 > 10) // true

    println(10 < 10) // false
    println(10 < 11) // true
    println(11 < 10) // false

    println(10 >= 10) // true
    println(10 >= 11) // false
    println(11 >= 10) // true

    println(10 <= 10) // true
    println(10 <= 11) // true
    println(11 <= 10) // false
}
```

#### Operacje logiczne

W Kotlin mamy trzy podstawowe operatory logiczne:
* and `&&`, który zwraca `true`, gdy wartości po obu jego stronach są `true`; w przeciwnym razie zwraca `false`.
* or `||`, który zwraca `true`, gdy wartość po którejkolwiek ze swoich stron jest `true`; w przeciwnym razie zwraca `false`.
* not `!`, który zamienia `true` na `false` i `false` na `true`.

```kotlin
fun main() {
    println(true && true) // true
    println(true && false) // false
    println(false && true) // false
    println(false && false) // false

    println(true || true) // true
    println(true || false) // true
    println(false || true) // true
    println(false || false) // false

    println(!true) // false
    println(!false) // true
}
```

Kotlin nie obsługuje żadnego rodzaju automatycznej konwersji na `Boolean` (ani żaden inny typ), więc operatory logiczne należy używać tylko z obiektami typu `Boolean`.

### Znaki

Aby reprezentować pojedynczy znak, używamy typu `Char`. Znak określamy za pomocą apostrofów.

```kotlin
fun main() {
    println('A') // A
    println('Z') // Z
}
```

Każdy znak jest reprezentowany jako liczba Unicode. Aby dowiedzieć się, jaka jest wartość Unicode danego znaku, użyj właściwości `code`.

```kotlin
fun main() {
    println('A'.code) // 65
}
```

Kotlin akceptuje znaki Unicode. Aby opisać je za pomocą ich kodu, zaczynamy od `\u`, a następnie musimy użyć formatu szesnastkowego, tak jak w Javie.

```kotlin
fun main() {
    println('\u00A3') // £
}
```

### Ciągi znaków

Ciągi znaków to po prostu sekwencje znaków tworzące fragment tekstu. W Kotlin tworzymy ciąg znaków używając cudzysłowów `"` lub potrójnych cudzysłowów `"""`.

```kotlin
fun main() {
    val text1 = "ABC"
    println(text1) // ABC
    val text2 = """DEF"""
    println(text2) // DEF
}
```

Ciąg znaków otoczony pojedynczymi cudzysłowami wymaga tekstu w jednym wierszu. Jeśli chcemy zdefiniować znak nowej linii, musimy użyć specjalnego znaku `\n`. To nie jedyna rzecz, która potrzebuje (lub może potrzebować) ukośnika wstecznego do wyrażenia w ciągu znaków.

| Sekwencja ucieczki | Znaczenie               |
|--------------------|-------------------------|
| `\t`               | Tabulator               |
| `\b`               | Cofnięcie               |
| `\r`               | Powrót karetki          |
| `\f`               | Przewijanie formularza  |
| `\n`               | Nowa linia              |
| `\'`               | Pojedynczy cudzysłów    |
| `\"`               | Cudzysłów               |
| `\\`               | Ukośnik wsteczny        |
| `\$`               | Dolar                   |

Ciągi znaków w potrójnych cudzysłowach mogą być wielowierszowe; w tych ciągach można bezpośrednio używać znaków specjalnych, a formy poprzedzone ukośnikiem wstecznym nie działają.

```kotlin
fun main() {
    val text1 = "Let\'s say:\n\"Hooray\""
    println(text1)
    // Let's say:
    // "Hooray"
    val text2 = """Let\'s say:\n\"Hooray\""""
    println(text2)
    // Let\'s say:\n\"Hooray\"
    val text3 = """Let's say:
"Hooray""""
    println(text3)
    // Let's say:
    // "Hooray"
}
```

Aby lepiej sformatować ciągi znaków w potrójnych cudzysłowach, używamy funkcji `trimIndent`, która ignoruje stałą liczbę spacji dla każdej linii.

```kotlin
fun main() {
    val text = """
   Let's say:
   "Hooray"
   """.trimIndent()
    println(text)
    // Let's say:
    // "Hooray"

    val description = """
      A
      B
          C
  """.trimIndent()
    println(description)
    // A
    // B
    //     C
}
```

Ciągi znaków mogą zawierać wyrażenia szablonowe, które są fragmentami kodu, które są ewaluowane, a ich wyniki są łączone w jeden ciąg. Wyrażenie szablonowe zaczyna się od znaku dolara (`$`) i składa się albo z nazwy zmiennej (takiej jak `"tekst to $text"`), albo z wyrażenia w nawiasach klamrowych (takiego jak `"1 + 2 = ${1 + 2}"`).

```kotlin
fun main() {
    val name = "Cookie"
    val surname = "DePies"
    val age = 6

    val fullName = "$name $surname ($age)"
    println(fullName) // Cookie DePies (6)

    val fullNameUpper =
        "${name.uppercase()} ${surname.uppercase()} ($age)"
    println(fullNameUpper) // COOKIE DEPIES (6)

    val description = """
       Name: $name
       Surname: $surname
       Age: $age
   """.trimIndent()
    println(description)
    // Name: Cookie
    // Surname: DePies
    // Age: 6
}
```

Jeśli potrzebujesz użyć specjalnego znaku wewnątrz ciągu znaków otoczonych potrójnymi cudzysłowami, najłatwiejszym sposobem jest zdefiniowanie go za pomocą zwykłego ciągu znaków i dołączenie go przy użyciu składni szablonu.

```kotlin
fun main() {
    val text1 = """ABC\nDEF"""
    println(text1) // ABC\nDEF
    val text2 = """ABC${"\n"}DEF"""
    println(text2)
    // ABC
    // DEF
}
```

W ciągach znaków Kotlin używamy Unicode, więc możemy również zdefiniować znak Unicode, używając liczby zaczynającej się od `\u`, a następnie podając kod znaku Unicode w składni szesnastkowej.

![](unicode.png)

### Podsumowanie

W tym rozdziale poznaliśmy podstawowe typy Kotlin oraz literały używane do ich tworzenia:
* Liczby reprezentowane przez typy `Int`, `Long`, `Double`, `Float`, `Short` i `Byte` są tworzone za pomocą samych wartości liczbowych z możliwością dodania sufiksów dla dostosowania typu. Możemy definiować liczby ujemne oraz części dziesiętne. Możemy również używać podkreślników dla lepszego formatowania liczb.
* Wartości logiczne `true` i `false` są reprezentowane przez typ `Boolean`.
* Znaki, które są reprezentowane przez typ `Char`. Wartość znaku definiujemy za pomocą pojedynczych cudzysłowów.
* Ciągi znaków, które służą do reprezentowania tekstu, są reprezentowane przez typ `String`. Każdy ciąg to tylko seria znaków. Ciągi znaków definiujemy wewnątrz podwójnych cudzysłowów.

Mamy więc podstawy do korzystania z Kotlin. Przejdźmy do bardziej skomplikowanych struktur sterujących, które określają, jak zachowuje się nasz kod.

[^04_1]: Konstruktory zostaną omówione w rozdziale *Klasy*.
[^04_2]: Ten operator jest podobny do modulo. Zarówno operacja reszty, jak i modulo działają tak samo dla liczb dodatnich, ale inaczej dla liczb ujemnych. Wynik -5 reszty 4 to -1, ponieważ -5 = 4 * (-1) + (-1). Wynik -5 modulo 4 to 3, ponieważ -5 = 4 * (-2) + 3.
