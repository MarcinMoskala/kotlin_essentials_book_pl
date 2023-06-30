## Wyjątki

Wyjątek to zazwyczaj niepożądane zdarzenie, które przerywa działanie programu. Może wystąpić, gdy wykonasz niedozwoloną operację. Wyjątki zawierają informacje, które pomagają deweloperom dowiedzieć się, co doprowadziło do problemu.

Przyjrzyjmy się przykładowi. Gdy podzielisz liczbę całkowitą przez 0, zostanie rzucony wyjątek typu `ArithmeticException`. Każdy wyjątek może zawierać wiadomość, która powinna wyjaśnić, co poszło nie tak. W tym przypadku wiadomością będzie "/ by zero". Każdy wyjątek zawiera również stacktrace, czyli listę wywołań metod, określających gdzie znajdowała się aplikacja, gdy rzucony został wyjątek. W tym przypadku obejmuje informacje, że wyjątek został rzucony z funkcji `calculate`, która została wywołana z funkcji `printCalculated`, która została wywołana z funkcji `main`. Wyjątek przerywa wykonanie programu, więc polecenia po nim nie zostaną wykonane. W poniższym przykładzie zauważ, że "After" nigdy nie zostaje wypisane.

```kotlin
private fun calculate(): Int {
    return 1 / 0
}

private fun printCalculated() {
    println(calculate())
}

fun main() {
    println("Before")
    printCalculated()
    println("After")
}
// Przed
// Wyjątek java.lang.ArithmeticException: / by zero
//     at PlaygroundKt.calculate(Playground.kt:2)
//     at PlaygroundKt.printCalculated(Playground.kt:6)
//     at PlaygroundKt.main(Playground.kt:11)
//     at PlaygroundKt.main(Playground.kt)
```

Jako kolejny przykład możemy przekształcić stringa na liczbę całkowitą za pomocą metody `toInt`. Ta operacja działa tylko wtedy, gdy string jest liczbą. Gdy tak nie jest, rzucony zostanie wyjątek `NumberFormatException` z informacją, jaki string został użyty.

```kotlin
fun main() {
    val i1 = "10".toInt()
    println(i1)
    val i2 = "ABC".toInt()
    println(i2)
}
// 10
// Exception in thread "main" java.lang.NumberFormatException:
// For input string: "ABC"
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
    throw ArithmeticException("Some message")
}

fun main() {
    println("Before")
    functionThrowing()
    println("After")
}
// Before
// Exception in thread "main" java.lang.ArithmeticException:
// Some message
//  at PlaygroundKt.functionThrowing(Playground.kt:2)
//  at PlaygroundKt.main(Playground.kt:7)
//  at PlaygroundKt.main(Playground.kt)
```

Wyjątki informują, że wystąpiła sytuacja, na którą funkcja nie jest przygotowana, lub której nie akceptuje. Nie jest to koniecznie oznaka błędu; to raczej zdarzenie powiadamiające, które może być obsłużone w innym miejscu, skonfigurowanym, aby rzucony wyjątek przechwycić.

### Definiowanie wyjątków

Możemy definiować własne wyjątki. Są to klasy lub deklaracje obiektów, które rozszerzają klasę `Throwable`. Każda instancja wyjątku może być rzucona za pomocą `throw`.

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

Wyjątki rzucamy przy użyciu słówka `throw`, a łapiemy je przy pomocy bloku `catch` w konstrukcji try-catch. Aby złapać wyjątek, potrzebna jest cała struktura try-catch, która zawiera blok try i blok catch. Wyjątek rzucony w funkcji natychmiast kończy jej wykonanie, a proces powtarza się w funkcji, która ją wywołała i w której rzucony został wyjątek. To się zmienia, gdy wyjątek zostanie rzucony wewnątrz bloku try, ponieważ wtedy sprawdzane są jego bloki catch. Każdy blok catch może określić, jakiego rodzaju wyjątki przechwytuje. Pierwszy blok catch, który akceptuje rzucony wyjątek, przechwytuje go, a następnie wykonuje swoje ciało. Jeśli wyjątek zostanie przechwycony, wykonanie programu będzie kontynuowane po bloku try.

```kotlin
class MyException : Throwable("Wiadomość")

fun someFunction() {
    throw MyException()
    println("Nie zostanie wypisane")
}

fun main() {
    try {
        someFunction()
        println("Nie zostanie wypisane")
    } catch (e: MyException) {
        println("Przechwycono $e")
    }
    println("To zostanie wypisane")
}
// Przechwycono MyException: Wiadomość
// To zostanie wypisane
```

Zobaczmy w akcji try-catch z większą liczbą bloków catch. Pamiętaj, że zawsze wybierany jest pierwszy blok, który akceptuje rzucony wyjątek. Blok catch akceptuje wyjątek, jeśli jest on podtypem typu określonego w bloku catch. Wszystkie wyjątki muszą rozszerzać `Throwable`, więc przechwytywanie tego typu oznacza przechwytywanie wszystkich możliwych wyjątków. Z tego powodu stosując więcej niż jeden block catch, ważne jest zachowanie odpowiedniej kolejności przechwytywania: od najbardziej do najmniej szczegółowego wyjątku.

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

Wyrażenie try-catch może być używane do zapewnienia alternatywnej wartości w sytuacji, w której występuje problem. W poniższym kodzie próbujemy odczytać zawartość pliku, który nie istnieje, więc funkcja `readText` rzuci wyjątek `FileNotFoundException`. My ten wyjątek przechwytujemy, po czym zwracamy pusty string. W ten sposób możemy kontynuować działanie programu.

```kotlin
import java.io.File
import java.io.FileNotFoundException

fun main() {
    val content = try {
        File("AAA").readText()
    } catch (e: FileNotFoundException) {
        ""
    }
    println(content) // (pusty string)
}
```

Praktycznym przykładem może być odczytanie ciągu znaków zawierającego obiekt w formacie JSON. Używamy biblioteki Gson, której metoda `fromJson` rzuca wyjątek `JsonSyntaxException`, gdy string nie zawiera prawidłowego obiektu JSON. W takich przypadkach wolelibyśmy funkcję zwracającą wartość `null`; możemy to zaimplementować, używając try-catch jako wyrażenia.

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

W strukturze try można również użyć bloku finally. Jego zadaniem jest określenie, co powinno być zawsze wywołane, nawet jeśli wystąpi wyjątek. Ten blok nie przechwytuje żadnych wyjątków; jest używany, aby zagwarantować, że pewne operacje zostaną wykonane, niezależnie od wyjątków.

Spójrz na poniższy kod. Wyjątek jest rzucony wewnątrz `someFunction`, czym kończy wykonanie funkcji i pomija resztę bloku try. Ponieważ nie mamy bloku catch, ten wyjątek nie zostanie złapany, a więc zakończy wykonanie funkcji `main`. Jednak istnieje także blok finally, którego ciało jest wywoływane, nawet jeśli wystąpi wyjątek.

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

Blok finally jest również wywoływany wtedy, gdy blok try zakończy się bez wyjątku.

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
- `IllegalArgumentException` - używamy tego wyjątku, gdy argument ma nieprawidłową wartość. Na przykład, gdy oczekujemy, że wartość argumentu będzie większa niż 0, ale tak nie jest.
- `IllegalStateException` - używamy tego wyjątku, gdy stan naszego systemu jest niepoprawny. Oznacza to, że wartości właściwości nie są akceptowane przez wywołanie funkcji.

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
        throw IllegalStateException("Missing user name")
    }
    // ...
}
```

W Kotlinie używamy funkcji `require` i `check`, aby odpowiednio zgłosić wyjątki `IllegalArgumentException` i `IllegalStateException`, gdy określone przez te funkcje warunki nie są spełnione[^e_1].

```kotlin
fun pop(num: Int): List<T> {
    require(num <= size)
    // zgłasza IllegalArgumentException, jeśli num > size
    check(isOpen)
    // zgłasza IllegalStateException, jeśli isOpen to false
    val ret = collection.take(num)
    collection = collection.drop(num)
    return ret
}
```

W bibliotece standardowej Kotlin znajduje się również funkcja `error`, która rzuca wyjątek `IllegalArgumentException` z wiadomością określoną jako argument. Często używana jest jako ciało dla gałęzi w wyrażeniu warunkowym when, a także po prawej stronie operatora Elvisa lub w wyrażeniu if-else.

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

Najważniejsze podtypy `Throwable` to `Error` i `Exception`. Reprezentują one dwa rodzaje wyjątków:
* Typ `Error` reprezentuje wyjątki, po których dalsze, poprawne działanie programu nie powinno być możliwe i które nie powinny być obsługiwane, a przynajmniej nie bez ponownego rzucenia ich w bloku catch. Dobrym przykładem jest `OutOfMemoryError`, który jest rzucany, gdy naszej aplikacji skończy się pamięć. 
* Typ `Exception` reprezentuje wyjątki, które można złapać w bloku catch. Ta grupa obejmuje `IllegalArgumentException`, `IllegalStateException`, `ArithmeticException` oraz `NumberFormatException`.

W większości przypadków do definiowania własnych wyjątków powinniśmy używać nadklasy `Exception`; gdy przechwytujemy wyjątki, powinniśmy zgłaszać tylko podtypy klasy `Exception`.

![](exception_hierarchy.png)

W Kotlinie nie jesteśmy zmuszeni do łapania żadnych rodzajów wyjątków, w przeciwieństwie do niektórych innych języków. 

### Podsumowanie

W tym rozdziale dowiedzieliśmy się o wyjątkach, które są ważną częścią programowania w Kotlinie. Nauczyliśmy się, jak rzucać, łapać i definiować wyjątki. Dowiedzieliśmy się również o bloku finally oraz hierarchii wyjątków.

Kontynuując tematykę specjalnych rodzajów klas, porozmawiajmy o enumach, które są używane do reprezentowania zestawu możliwych wartości.

[^e_1]: Ten temat jest lepiej opisany w książce Effective Kotlin, *Temat 5: Określ swoje oczekiwania względem argumentów i stanu*.
