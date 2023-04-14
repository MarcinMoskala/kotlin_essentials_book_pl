## Wyjątki

Wyjątek to zazwyczaj niepożądane zdarzenie, które przerywa normalne działanie programu. Może wystąpić, gdy wykonasz niedozwoloną operację. Wyjątki zawierają informacje, które pomagają deweloperom dowiedzieć się, co doprowadziło do tego problemu.

Przyjrzyjmy się przykładowi. Gdy podzielisz liczbę całkowitą przez 0, zostanie zgłoszony wyjątek typu `ArithmeticException`. Każdy wyjątek może zawierać wiadomość, która powinna wyjaśnić, co poszło nie tak. W tym przypadku wiadomością będzie "/ przez zero". Każdy wyjątek zawiera również ślad stosu, który to lista wywołań metod, w których środku znajdowała się aplikacja, gdy zgłoszono wyjątek. W tym przypadku obejmuje informacje, że wyjątek został zgłoszony z funkcji `calculate`, która została wywołana z funkcji `printCalculated`, która została wywołana z funkcji `main`. Wyjątek przerywa wykonanie programu, więc polecenia po nim nie zostaną wykonane. W poniższym przykładzie zauważ, że "After" nigdy nie zostaje wydrukowane.

```kotlin
private fun calculate(): Int {
    return 1 / 0
}

private fun printCalculated() {
    println(calculate())
}

fun main() {
    println("Przed")
    printCalculated()
    println("Po")
}
// Przed
// Wyjątek java.lang.ArithmeticException: / przez zero
//     at PlaygroundKt.calculate(Playground.kt:2)
//     at PlaygroundKt.printCalculated(Playground.kt:6)
//     at PlaygroundKt.main(Playground.kt:11)
//     at PlaygroundKt.main(Playground.kt)
```

Jako kolejny przykład możemy przekształcić ciąg znaków na liczbę całkowitą za pomocą metody `toInt`, ale działa to tylko wtedy, gdy ciąg znaków jest liczbą. Gdy tak nie jest, zobaczymy `NumberFormatException` z wiadomością wyjaśniającą, który ciąg znaków został użyty.

```kotlin
fun main() {
    val i1 = "10".toInt()
    println(i1)
    val i2 = "ABC".toInt()
    println(i2)
}
// 10
// Wyjątek w wątku "main" java.lang.NumberFormatException:
// Dla ciągu wejściowego: "ABC"
//   at java.base/java.lang.NumberFormatException.
//   forInputString(NumberFormatException.java:67)
//   at java.base/java.lang.Integer.parseInt(Integer.java:660)
//   at java.base/java.lang.Integer.parseInt(Integer.java:778)
//   at PlaygroundKt.main(Playground.kt:4)
//   at PlaygroundKt.main(Playground.kt)
```

### Rzucanie wyjątków

Możemy rzucać wyjątki samodzielnie, używając słowa kluczowego `throw` i wartości, która może być użyta jako wyjątek, takiej jak wspomniane `ArithmeticException` czy `NumberFormatException`.

```kotlin
private fun functionThrowing() {
    throw ArithmeticException("Jakieś pytanie")
}

fun main() {
    println("Przed")
    functionThrowing()
    println("Po")
}
// Przed
// Wyjątek w wątku "main" java.lang.ArithmeticException:
// Jakieś pytanie
//  at PlaygroundKt.functionThrowing(Playground.kt:2)
//  at PlaygroundKt.main(Playground.kt:7)
//  at PlaygroundKt.main(Playground.kt)
```

Wyjątki komunikują warunki, których funkcja nie jest przygotowana do obsłużenia lub za które nie jest odpowiedzialna. Nie jest to koniecznie oznaka błędu; to raczej zdarzenie powiadamiające, które może być obsłużone w innym miejscu, które jest skonfigurowane, aby je przechwycić.

### Definiowanie wyjątków

Możemy również definiować własne wyjątki. Są to zwykłe klasy lub deklaracje obiektów, które rozszerzają klasę `Throwable`. Każda taka klasa może być rzucona za pomocą `throw`.

```kotlin
class MyException : Throwable("Jakieś pytanie")
object MyExceptionObject : Throwable("Jakieś pytanie")

private fun functionThrowing() {
    throw MyException()
    // lub throw MyExceptionObject
}

fun main() {
    println("Przed")
    functionThrowing()
    println("Po")
}
// Przed
// Wyjątek w wątku "main" MyException: Jakieś pytanie
//  at PlaygroundKt.functionThrowing(Playground.kt:4)
//  at PlaygroundKt.main(Playground.kt:9)
//  at PlaygroundKt.main(Playground.kt)
```

### Przechwytywanie wyjątków

Tak samo jak wyjątki można rzucać, można je przechwycić za pomocą struktury try-catch, która zawiera blok try i blok catch. Wyjątek zgłoszony w funkcji natychmiast kończy jej wykonanie, a proces powtarza się w funkcji, która wywołała funkcję, w której zgłoszono wyjątek. To się zmienia, gdy wyjątek zostanie zgłoszony wewnątrz bloku try, ponieważ wtedy sprawdzane są jego bloki catch. Każdy blok catch może określić, jakiego rodzaju wyjątki przechwytuje. Pierwszy blok catch, który akceptuje zgłoszony wyjątek, przechwytuje go, a następnie wykonuje swoje ciało. Jeśli wyjątek zostanie przechwycony, wykonanie programu będzie kontynuowane po bloku try.

```kotlin
class MyException : Throwable("Jakieś pytanie")

fun someFunction() {
    throw MyException()
    println("Nie zostanie wydrukowane")
}

fun main() {
    try {
        someFunction()
        println("Nie zostanie wydrukowane")
    } catch (e: MyException) {
        println("Przechwycono $e")
        // Przechwycono MyException: Jakieś pytanie
    }
}
```

Zobaczmy try-catch z większą ilością bloków catch w akcji. Pamiętaj, że zawsze wybierany jest pierwszy blok, który akceptuje wyjątek. Blok catch akceptuje wyjątek, jeśli ten wyjątek jest podtypem typu określonego w bloku catch. Zauważ, że wszystkie wyjątki muszą rozszerzać `Throwable`, więc przechwytywanie tego typu oznacza przechwytywanie wszystkich możliwych wyjątków.

```kotlin
import java.lang.NumberFormatException

class MyException : Throwable("Jakieś pytanie")

fun testTryCatch(exception: Throwable) {
    try {
        throw exception
    } catch (e: ArithmeticException) {
        println("Dostałem ArithmeticException")
    } catch (e: MyException) {
        println("Dostałem MyException")
    } catch (e: Throwable) {
        println("Dostałem jakiś wyjątek")
    }
}

fun main() {
    testTryCatch(ArithmeticException())
    // Dostałem ArithmeticException
    testTryCatch(MyException())
    // Dostałem MyException
    testTryCatch(NumberFormatException())
    // Dostałem jakiś wyjątek
}
```

### Blok try-catch użyty jako wyrażenie

Struktura try-catch może być używana jako wyrażenie. Zwraca ona wynik bloku try, jeśli nie wystąpił żaden wyjątek. Jeśli wystąpi wyjątek i zostanie przechwycony, wyrażenie try-catch zwraca wynik bloku catch.

```kotlin
fun main() {
    val a = try {
        1
    } catch (e: Error) {
        2
    }
    println(a) // 1

    val b = try {
        throw Error()
        1
    } catch (e: Error) {
        2
    }
    println(b) // 2
}
```

Wyrażenie try-catch może być używane do zapewnienia alternatywnej wartości dla sytuacji, w której występuje problem:

```kotlin
import java.io.File
import java.io.FileNotFoundException

fun main() {
    val content = try {
        File("AAA").readText()
    } catch (e: FileNotFoundException) {
        ""
    }
    println(content) // (pusty ciąg znaków)
}
```

Praktycznym przykładem może być odczytanie ciągu znaków zawierającego obiekt w formacie JSON. Używamy biblioteki Gson, której metoda `fromJson` rzuca wyjątek `JsonSyntaxException`, gdy ciąg znaków nie zawiera prawidłowego obiektu JSON. W takich przypadkach wolelibyśmy funkcję zwracającą wartość `null`; możemy to zaimplementować, używając try-catch jako wyrażenia.

```kotlin
fun <T : Any> fromJsonOrNull(
    json: String,
    clazz: KClass<T>
): T? = try {
    gson.fromJson(json, clazz.java)
} catch (e: JsonSyntaxException) {
    null
}
```

### Blok finally

W strukturze try można również użyć bloku finally, który służy do określenia, co powinno być zawsze wywołane, nawet jeśli wystąpi wyjątek. Ten blok nie przechwytuje żadnych wyjątków; jest używany, aby zagwarantować, że niektóre operacje zostaną wykonane, niezależnie od wyjątków.

Spójrz na poniższy kod. Wyjątek jest rzucony wewnątrz `someFunction`. Ten wyjątek kończy wykonanie tej funkcji i pomija resztę bloku try. Ponieważ nie mamy bloku catch, ten wyjątek nie zostanie przechwycony, kończąc tym samym wykonanie funkcji `main`. Jednak istnieje także blok finally, którego ciało jest wywoływane nawet jeśli wystąpi wyjątek.

```kotlin
fun someFunction() {
    throw Throwable("Jakiś błąd")
}

fun main() {
    try {
        someFunction()
    } finally {
        println("Blok finally został wywołany")
    }
    println("Nie zostanie wydrukowane")
}
// Blok finally został wywołany
```

Blok finally jest również wywoływany, gdy blok try zakończy się bez wyjątku.

```kotlin
fun someFunction() {
    println("Funkcja wywołana")
}

fun main() {
    try {
        someFunction()
        println("Po wywołaniu")
    } finally {
        println("Blok finally został wywołany")
    }
    println("Po try-finally")
}
// Funkcja wywołana
// Po wywołaniu
// Blok finally został wywołany
// Po try-finally
```

Blok finally używamy do wykonywania operacji, które zawsze powinny być wykonywane, niezależnie od tego, czy wystąpi wyjątek, czy nie. Zwykle obejmuje to zamykanie połączeń lub czyszczenie zasobów.

### Ważne wyjątki

W Kotlinie zdefiniowano kilka rodzajów wyjątków, które stosujemy w określonych sytuacjach. Najważniejsze z nich to:
- `IllegalArgumentException` - używamy tego, gdy argument ma nieprawidłową wartość. Na przykład, gdy oczekujesz, że wartość Twojego argumentu będzie większa niż 0, ale tak nie jest.
- `IllegalStateException` - używamy tego, gdy stan naszego systemu jest niepoprawny. Oznacza to, że wartości właściwości nie są akceptowane przez wywołanie funkcji.

```kotlin
fun findClusters(number: Int) {
    if (number < 1) {
        throw IllegalArgumentException("...")
    }
    // ...
}

var userName = ""

fun printUserName() {
    if (userName == "") {
        throw IllegalStateException("Nazwa nie może być pusta")
    }
    // ...
}
```

W Kotlinie używamy funkcji `require` i `check`, aby zgłosić wyjątek `IllegalArgumentException` i `IllegalStateException`, gdy ich warunki nie są spełnione[^e_1].

```kotlin
fun pop(num: Int): List<T> {
    require(num <= size)
    // zgłasza IllegalArgumentException, jeśli num > size
    check(isOpen)
    // zgłasza IllegalStateException, jeśli nie jest otwarty
    val ret = collection.take(num)
    collection = collection.drop(num)
    return ret
}
```

W bibliotece standardowej Kotlin znajduje się również funkcja `error`, która zgłasza wyjątek `IllegalArgumentException` z wiadomością określoną jako argument. Często używana jest jako ciało dla gałęzi w wyrażeniu warunkowym when, po prawej stronie operatora Elvisa lub w wyrażeniu if-else.

```kotlin
fun makeOperation(
    operation: String,
    left: Int,
    right: Int? = null
): Int = when (operation) {
    "add" ->
        left + (right ?: error("Wymagane są dwie liczby"))
    "subtract" ->
        left - (right ?: error("Wymagane są dwie liczby"))
    "opposite" -> -left
    else -> error("Nieznana operacja")
}

fun main() {
    println(makeOperation("add", 1, 2)) // 3
    println(makeOperation("subtract", 1, 2)) // -1
    println(makeOperation("opposite", 10)) // -10

    makeOperation("add", 1) // BŁĄD!
    // IllegalStateException: Wymagane są dwie liczby
    makeOperation("subtract", 1) // BŁĄD!
    // IllegalStateException: Wymagane są dwie liczby
    makeOperation("other", 1, 2) // BŁĄD!
    // IllegalStateException: Nieznana operacja
}
```

### Hierarchia wyjątków

Najważniejsze podtypy klasy `Throwable` to `Error` i `Exception`. Reprezentują one dwa rodzaje wyjątków:
* Typ `Error` reprezentuje wyjątki, od których nie można się odzyskać i które nie powinny być przechwytywane, przynajmniej nie bez ponownego zgłoszenia ich w bloku catch. Wyjątki, od których nie można się odzyskać, obejmują `OutOfMemoryError`, który jest zgłaszany, gdy brakuje miejsca na stercie JVM.
* Typ `Exception` reprezentuje wyjątki, od których można się odzyskać za pomocą bloku try-catch. Ta grupa obejmuje `IllegalArgumentException`, `IllegalStateException`, `ArithmeticException` oraz `NumberFormatException`.

W większości przypadków, gdy definiujemy własne wyjątki, powinniśmy używać nadklasy `Exception`; gdy przechwytujemy wyjątki, powinniśmy zgłaszać tylko podtypy klasy `Exception`.

![](exception_hierarchy.png)

W Kotlinie nie jesteśmy zmuszeni do łapania żadnych rodzajów wyjątków; w przeciwieństwie do niektórych innych języków, nie ma tu wyjątków sprawdzanych.

### Podsumowanie

W tym rozdziale dowiedzieliśmy się o wyjątkach, które są ważną częścią programowania w Kotlinie. Nauczyliśmy się, jak rzucać, łapać i definiować wyjątki. Dowiedzieliśmy się również o bloku finally oraz hierarchii wyjątków.

Kontynuując tematykę specjalnych rodzajów klas, porozmawiajmy o klasach wyliczeniowych, które są używane do reprezentowania zestawu wartości instancji obiektów.

[^e_1]: Ten temat jest lepiej opisany w książce Effective Kotlin, *Pozycja 5: Określ swoje oczekiwania względem argumentów i stanów*.
