## Instrukcje warunkowe: if, when, try oraz while

Większość instrukcji warunkowych, takich jak warunek if czy pętla while[^05_0], wygląda tak samo w Kotlin, Javie, C++, JavaScript i większości innych nowoczesnych języków. Na przykład, instrukcja if jest nie do odróżnienia we wszystkich tych językach:

```kotlin
if (predicate) {
    // ciało
}
```

Jednak warunek if w Kotlin jest bardziej zaawansowany i posiada możliwości, których nie obsługują poprzednicy Kotlina. Zakładam, że czytelnicy tej książki mają ogólne doświadczenie w programowaniu, więc skoncentruję się na różnicach, które Kotlin wprowadził w porównaniu z innymi językami programowania.

### Instrukcja if

Zacznijmy od wspomnianej instrukcji if. Wykonuje ona swoje ciało, gdy jej warunek jest spełniony (zwraca `true`). Możemy dodatkowo dodać blok `else`, który jest wykonywany, gdy warunek nie jest spełniony (zwraca `false`).

```kotlin
fun main() {
    val i = 1 // lub 5
    if (i < 3) { // i < 3 jest używane jako warunek
        // zostanie wykonane, gdy warunek zwróci true
        println("Mniejsze")
    } else {
        // zostanie wykonane, gdy warunek zwróci false
        println("Większe")
    }
    // Wypisze Mniejsze, jeśli i == 1, lub Większe, jeśli i == 5
}
```

Jednym z supermoców Kotlina jest to, że instrukcja if-else może być używana jako wyrażenie[^05_1], a więc generuje wartość.

val value = if (condition) {
    // ciało 1
} else {
    // ciało 2
}

Jaką wartość zwraca? Dla każdego bloku ciała jest to wynik ostatniego wyrażenia (lub `Unit` dla pustego ciała lub wyrażenia, które nie jest wyrażeniem[^05_2]).

```kotlin
fun main() {
    var isOne = true
    val number1: Int = if (isOne) 1 else 0
    println(number1) // 1
    isOne = false
    val number2: Int = if (isOne) 1 else 0
    println(number2) // 0

    val superuser = true
    val hasAccess: Boolean = if (superuser) {
        println("Dzień dobry, panie Adminie")
        true
    } else {
        false
    }
    println(hasAccess) // true
}
```

Gdy ciało ma tylko jedno wyrażenie, jego wynik jest wynikiem naszego wyrażenia if-else. W takim przypadku nie potrzebujemy nawiasów.

```kotlin
val r: Int = if (one) 1 else 0
// bardziej czytelna alternatywa dla
val r: Int = if (one) {
    1
} else {
    0
}
```

Sposób użycia instrukcji if-statement jest alternatywą w Kotlin dla operatora trójargumentowego używanego w Java lub JavaScript.

```
// Java
final String name = user == null ? "" : user.name
// JavaScript
const name = user === null ? "" : user.name
```

```kotlin
// Kotlin
val name = if (user == null) "" else user.name
```

Należy zaznaczyć, że if-else jest dłuższe niż składnia operatora trójargumentowego. Wierzę, że jest to główny powód, dla którego niektórzy deweloperzy chcieliby wprowadzenia składni operatora trójargumentowego w Kotlin. Jednak jestem przeciwny temu, ponieważ if-else to dobre zastępstwo, które jest bardziej czytelne i może być lepiej sformatowane. Co więcej, mamy dodatkowe narzędzia Kotlin, które są również zamiennikami dla niektórych przypadków użycia operatora trójargumentowego: operator Elvisa, rozszerzenia dla typów nullable (takie jak `orEmpty`) czy safe-calls. Wszystkie te elementy zostaną wyjaśnione szczegółowo w rozdziale *Nullability*.

```
// Java
String name = user == null ? "" : user.name

// Kotlin
val name = user?.name ?: ""
// lub
val name = user?.name.orEmpty()
```

Zauważ, że jeśli używasz tzw. instrukcji if-else-if, są to po prostu wielokrotnie połączone instrukcje if-else.

```kotlin
fun main() {
    println("Czy będzie padać deszcz?")
    val probability = 70
    if (probability < 40) {
        println("Nie bardzo")
    } else if (probability <= 80) {
        println("Prawdopodobnie")
    } else if (probability < 100) {
        println("Tak")
    } else {
        println("Święty Krab")
    }
}
```

W rzeczywistości nie ma czegoś takiego jak wyrażenie if-else-if: to po prostu jedno wyrażenie if-else umieszczone wewnątrz innego, co można zobaczyć w dziwnych przypadkach, gdy na całym wyrażeniu if-else-if wykonywana jest jakaś metoda. Spójrz na poniższą zagadkę i spróbuj przewidzieć wynik tego kodu.

```kotlin
// Funkcja, którą możemy wykonać na dowolnym obiekcie, aby go wydrukować
// 10.print() drukuje 10
// "ABC".print() drukuje ABC
fun Any?.print() {
    print(this)
}

fun printNumberSign(num: Int) {
    if (num < 0) {
        "ujemna"
    } else if (num > 0) {
        "dodatnia"
    } else {
        "zero"
    }.print()
}

fun main(args: Array<String>) {
    printNumberSign(-2)
    print(",")
    printNumberSign(0)
    print(",")
    printNumberSign(2)
}
```

Odpowiedź **nie** brzmi "ujemna,zero,dodatnia", ponieważ nie ma czegoś takiego jak pojedyncze wyrażenie if-else-if (tylko dwie zagnieżdżone instrukcje if-else). W związku z tym, powyższa implementacja `printNumberSign` daje taki sam wynik jak poniższa implementacja.

```kotlin
fun printNumberSign(num: Int) {
    if (num < 0) {
        "ujemna"
    } else {
        if (num > 0) {
            "dodatnia"
        } else {
            "zero"
        }.print()
    }
}
```

Więc, gdy wywołujemy metodę `print` na wyniku, jest ona wywoływana tylko na wyniku drugiego wyrażenia if-else (tego z "dodatnia" i "zero"). Oznacza to, że powyższy kod wydrukuje ",zero,dodatnia". Jak możemy to naprawić? Moglibyśmy użyć nawiasów, ale zazwyczaj zaleca się, aby zamiast używać if-else-if, używać instrukcji when, gdy mamy więcej niż jeden warunek. Może to pomóc uniknąć błędów, takich jak ten w powyższej zagadce, i sprawia, że kod jest bardziej przejrzysty i łatwiejszy do odczytania.

```kotlin
// Funkcja, którą możemy wykonać na dowolnym obiekcie, aby go wydrukować
// 10.print() drukuje 10
// "ABC".print() drukuje ABC
fun Any?.print() {
    print(this)
}

fun printNumberSign(num: Int) {
    when {
        num < 0 -> "ujemna"
        num > 0 -> "dodatnia"
        else -> "zero"
    }.print()
}

fun main(args: Array<String>) {
    printNumberSign(-2) // ujemna
    print(",") // ,
    printNumberSign(0) // zero
    print(",") // ,
    printNumberSign(2) // dodatnia
}
```

### Instrukcja when

Instrukcja when jest alternatywą dla if-else-if. W każdym z gałęzi określamy predykat i ciało, które powinno zostać wykonane, jeśli ten predykat zwróci `true` (a wcześniejsze predykaty nie). Działa więc tak samo jak if-else-if, ale należy go preferować, ponieważ jego składnia jest lepiej dostosowana do wielu warunków.

```kotlin
fun main() {
    println("Czy będzie padać deszcz?")
    val prawdopodobienstwo = 70
    when {
        prawdopodobienstwo < 40 -> {
            println("Nie bardzo")
        }
        prawdopodobienstwo <= 80 -> {
            println("Prawdopodobnie")
        }
        prawdopodobienstwo < 100 -> {
            println("Tak")
        }
        else -> {
            println("Święty Krab")
        }
    }
}
```

Podobnie jak w instrukcji if, nawiasy klamrowe są potrzebne tylko dla ciał z więcej niż jednym poleceniem.

```kotlin
fun main() {
    println("Czy będzie padać deszcz?")
    val prawdopodobienstwo = 70
    when {
        prawdopodobienstwo < 40 -> println("Nie bardzo")
        prawdopodobienstwo <= 80 -> println("Prawdopodobnie")
        prawdopodobienstwo < 100 -> println("Tak")
        else -> println("Święty Krab")
    }
}
```

```kotlin
fun main() {
    println("Czy będzie padać deszcz?")
    val prawdopodobienstwo = 70
    val tekst = when {
        prawdopodobienstwo < 40 -> "Nie bardzo"
        prawdopodobienstwo <= 80 -> "Prawdopodobnie"
        prawdopodobienstwo < 100 -> "Tak"
        else -> "Święty Krab"
    }
    println(tekst)
}
```

Instrukcja when często używana jest jako wyrażenie ciała funkcji[^05_3]:

```kotlin
private fun getEmailErrorId(email: String) = when {
    email.isEmpty() -> R.string.error_field_required
    emailInvalid(email) -> R.string.error_invalid_email
    else -> null
}
```

### Instrukcja when z wartością

Istnieje także inna forma instrukcji when. Jeśli dodamy wartość w nawiasach po słowie kluczowym `when`, nasza instrukcja when staje się alternatywą dla switch-case. Jest jednak znacznie potężniejsza alternatywa, ponieważ może nie tylko porównywać wartości pod względem równości, ale także sprawdzać, czy obiekt jest danego typu (używając `is`) lub czy obiekt zawiera tę wartość (używając `in`). Każdy blok może mieć wiele wartości, z którymi porównujemy, oddzielonych przecinkami.

```kotlin
private val magiczneLiczby = listOf(7, 13)

fun opisz(a: Any?) {
    when (a) {
        null -> println("Nic")
        1, 2, 3 -> println("Mała liczba")
        in magiczneLiczby -> println("Magiczna liczba")
        in 4..100 -> println("Duża liczba")
        is String -> println("To tylko $a")
        is Long, is Int -> println("To Int lub Long")
        else -> println("Naprawdę nie wiem")
    }
}

fun main() {
    opisz(null) // Nic
    opisz(1) // Mała liczba
    opisz(3) // Mała liczba
    opisz(7) // Magiczna liczba
    opisz(9) // Duża liczba,
    // ponieważ 9 jest w zakresie od 4 do 100
    opisz("AAA") // To tylko AAA
    opisz(1L) // To Int lub Long
    opisz(-1) // To Int lub Long
    opisz(1.0) // Naprawdę nie wiem,
    // ponieważ 1.0 to Double
}
```

```kotlin
private val magiczneLiczby = listOf(7, 13)

fun opisz(a: Any?): String = when (a) {
    null -> "Nic"
    1, 2, 3 -> "Mała liczba"
    in magiczneLiczby -> "Magiczna liczba"
    in 4..100 -> "Duża liczba"
    is String -> "To tylko $a"
    is Long, is Int -> "To Int lub Long"
    else -> "Naprawdę nie wiem"
}

fun main() {
    println(opisz(null)) // Nic
    println(opisz(1)) // Mała liczba
    println(opisz(3)) // Mała liczba
    println(opisz(7)) // Magiczna liczba
    println(opisz(9)) // Duża liczba,
    // ponieważ 9 jest w zakresie od 4 do 100
    println(opisz("AAA")) // To tylko AAA
    println(opisz(1L)) // To Int lub Long
    println(opisz(-1)) // To Int lub Long
    println(opisz(1.0)) // Naprawdę nie wiem,
    // ponieważ 1.0 to Double
}
```

Należy zauważyć, że jeśli używamy warunku when jako wyrażenia, jego warunki muszą być wyczerpujące: powinny obejmować wszystkie możliwe warunki gałęzi lub dostarczyć gałąź else, jak w powyższym przykładzie. Jeśli nie są spełnione wszystkie warunki, pokazany jest błąd kompilatora.

> Kotlin nie obsługuje instrukcji switch-case, ponieważ zamiast tego używamy instrukcji when.

W nawiasach "when", gdzie określamy wartość, możemy również zdefiniować zmienną, a jej wartość zostanie użyta w każdym warunku.

```kotlin
fun showUsers() =
    when (val response = requestUsers()) {
        is Success -> showUsers(response.body)
        is HttpError -> showException(response.exception)
    }
```

### Sprawdzanie is

Skoro już wspomnieliśmy o operatorze `is`, omówmy go nieco dokładniej. Sprawdza on, czy dana wartość jest określonego typu. Wiemy już, że `123` jest typu `Int`, a `"ABC"` jest typu `String`. Z pewnością `123` nie jest typu `String`, a `"ABC"` nie jest typu `Int`. Możemy to potwierdzić, używając sprawdzenia `is`.

```kotlin
fun main() {
    println(123 is Int) // true
    println("ABC" is String) // true
    println(123 is String) // false
    println("ABC" is Int) // false
}
```

Zauważ, że `123` jest `Int`, ale jest również `Number`; sprawdzenie `is` zwraca `true` dla obu tych typów.

```kotlin
fun main() {
    println(123 is Int) // true
    println(123 is Number) // true
    println(3.14 is Double) // true
    println(3.14 is Number) // true

    println(123 is Double) // false
    println(3.14 is Int) // false
}
```

Kiedy chcemy sprawdzić, czy wartość **nie jest** określonego typu, możemy użyć `!is`; jest to odpowiednik sprawdzenia is-check, ale jego wartość wynikowa jest zanegowana.

```kotlin
fun main() {
    println(123 !is Int) // false
    println("ABC" !is String) // false
    println(123 !is String) // true
    println("ABC" !is Int) // true
}
```

### Rzutowanie jawne

Zawsze można użyć wartości, której typem jest `Int`, jako `Number`, ponieważ każdy `Int` jest `Number`. Ten proces nazywa się *rzutowaniem w górę*, ponieważ zmieniamy typ wartości z niższego (bardziej konkretnego) na wyższy (mniej konkretny).

```kotlin
fun main() {
    val i: Int = 123
    val l: Long = 123L
    val d: Double = 3.14

    var number: Number = i // rzutowanie w górę z Int na Number
    number = l // rzutowanie w górę z Long na Number
    number = d // rzutowanie w górę z Double na Number
}
```

Możemy rzutować niejawne z niższego typu na wyższy, ale nie na odwrót. Każdy `Int` jest `Number`, ale nie każdy `Number` jest `Int`, ponieważ istnieje więcej podtypów `Number`, takich jak `Double` czy `Long`. Dlatego nie możemy użyć `Number`, gdzie oczekiwany jest `Int`. Jednak czasami mamy sytuację, gdy jesteśmy pewni, że wartość jest określonego typu, nawet jeśli używany jest jej supertyp. Jawna zmiana z wyższego typu na niższy nazywa się *rzutowaniem w dół* i wymaga operatora `as` w Kotlin.

```kotlin
var i: Number = 123

fun main() {
    val j = (i as Int) + 10
    println(j) // 133
}
```

Ogólnie unikamy używania `as`, gdy nie jest to konieczne, ponieważ uważamy, że jest niebezpieczne. Rozważ powyższy przykład. Co jeśli ktoś zmieni `123` na `3.14`? Obie wartości są typu `Number`, więc kod będzie kompilować się bez żadnych problemów czy ostrzeżeń. Ale `3.14` to `Double`, a nie `Int`, i rzutowanie nie jest możliwe; w związku z tym powyższy kod zakończy się błędem z wyjątkiem `ClassCastException`.

```kotlin
var i: Number = 3.14

fun main() {
    val j = (i as Int) + 10 // BŁĄD W CZASIE WYKONANIA!
    println(j)
}
```

Istnieją dwa sposoby radzenia sobie z tym. Pierwszy to użycie jednej z wielu alternatyw Kotlin do bezpiecznego rzutowania naszej wartości. Jednym z przykładów jest użycie rzutowania inteligentnego (smart-casting), które zostanie opisane w kolejnej sekcji. Innym przykładem jest funkcja konwersji, taka jak metoda `toInt`, która przekształca `Number` na `Int` (i ewentualnie traci część dziesiętną).

```kotlin
var i: Number = 3.14

fun main() {
    val j = i.toInt() + 10
    println(j) // 13
}
```

Drugą opcją jest operator `as?`, który zamiast rzucać wyjątkiem, zwraca `null`, gdy rzutowanie nie jest możliwe. Omówimy obsługę wartości nullable później.

```kotlin
var n: Number = 123

fun main() {
    val i: Int? = n as? Int
    println(i) // 123
    val d: Double? = n as? Double
    println(d) // null
}
```

W Kotlinie uważamy `as?` za bezpieczniejszą opcję niż `as`, ale zbyt częste używanie obu tych operatorów jest uważane za code smell[^05_4]. Opiszmy rzutowanie inteligentne (smart-casting), które jest ich popularną alternatywą.

### Rzutowanie inteligentne (Smart-casting)

Kotlin ma potężną funkcję o nazwie rzutowanie inteligentne (smart-casting), która pozwala na automatyczne rzutowanie typów, gdy kompilator może być pewien, że zmienna jest określonego typu. Spójrz na poniższy przykład:

```kotlin
fun convertToInt(num: Number): Int =
    if (num is Int) num  // typ num tutaj to Int
    else num.toInt()
```

Funkcja `convertToInt` konwertuje argument typu `Number` na `Int` w następujący sposób: jeśli argument jest już typu `Int`, jest zwracany; w przeciwnym razie jest konwertowany za pomocą metody `toInt`. Zauważ, że aby ten kod się skompilował, `num` wewnątrz pierwszego bloku musi być typu `Int`. W większości języków musi być rzutowany, ale w Kotlinie dzieje się to automatycznie. Spójrz na kolejny przykład:

```kotlin
fun lengthIfString(a: Any): Int {
    if (a is String) {
        return a.length // typ a to String
    }
    return 0
}
```

Wewnątrz predykatu warunku if sprawdziliśmy, czy `a` jest typu `String`. Ciało tego wyrażenia zostanie wykonane tylko wtedy, gdy sprawdzenie typu zakończy się powodzeniem. Dlatego `a` jest typu `String` wewnątrz tego bloku, dlatego możemy sprawdzić jego długość. Taka konwersja, z `Any` na `String`, jest wykonana niejawnie przez kompilator Kotlin. Może się to zdarzyć tylko wtedy, gdy Kotlin jest pewien, że żaden inny wątek nie może zmienić naszej właściwości, więc kiedy jest to stała lub lokalna zmienna. Nie zadziała to dla nie-lokalnych właściwości `var`, ponieważ w takich przypadkach nie ma gwarancji, że nie zostały one zmodyfikowane między sprawdzeniem a użyciem (np. przez inny wątek).

```kotlin
var obj: Any = "AAA"

fun main() {
    if (obj is String) {
        // println(obj.length) nie zostanie skompilowany,
        // ponieważ `obj` może być zmodyfikowany przez jakiś
        // inny wątek, więc Kotlin nie może być pewien,
        // czy w tym momencie jest jeszcze typu String
    }
}
```

Smart-casting jest często używany razem z instrukcjami when. Kiedy są używane razem, czasami nazywane są "bezpiecznym wzorcem dopasowania typów Kotlin", ponieważ mogą ładnie pokryć różne możliwe typy, którymi może być wartość. Więcej przykładów zostanie przedstawionych, gdy omówimy modyfikator sealed.

```kotlin
fun handleResponse(response: Result<T>) {
    when (response) {
        is Success<*> -> showMessages(response.data) 
        // response rzutowane inteligentnie na Success
        is Failure -> showError(response.throwable)
        // response rzutowane inteligentnie na Failure
    }
}
```

### Pętle while i do-while

Ostatnimi ważnymi strukturami sterującymi, o których musimy wspomnieć, są pętle while i do-while. Wyglądają i działają dokładnie tak samo, jak w Java, C++ i wielu innych językach.

```kotlin
fun main() {
    var i = 1
    // pętla while
    while (i < 10) {
        print(i)
        i *= 2
    }
    // 1248

    var j = 1
    // pętla do-while
    do {
        print(j)
        j *= 2
    } while (j < 10)
    // 1248
}
```

Mam nadzieję, że nie potrzebują one dalszego wyjaśnienia. Pętle while i do-while nie mogą być używane jako wyrażenia. Dodam tylko, że zarówno pętle while, jak i do-while są rzadko używane w Kotlinie. Zamiast tego używamy funkcji przetwarzania kolekcji lub sekwencji, które zostaną omówione w książce *Funkcyjny Kotlin*. Na przykład powyższy kod można zastąpić następującym:

```kotlin
fun main() {
    generateSequence(1) { it * 2 }
        .takeWhile { it < 10 }
        .forEach(::print)
    // 1248
}
```

{pagebreak}

### Podsumowanie

Jak widać, Kotlin wprowadził wiele potężnych funkcji do instrukcji warunkowych. Warunki if i when mogą być używane jako wyrażenia. Instrukcja when jest bardziej zaawansowaną alternatywą dla if-else-if lub switch-case. Obsługiwane są sprawdzanie typów ze smart-castingiem. Wszystkie te funkcje sprawiają, że praca z wartościami nullable jest zarówno bezpieczna, jak i przyjemna, co sprawia, że wartość `null` jest naszym przyjacielem, a nie wrogiem. Teraz zobaczmy, co Kotlin zmienił w funkcjach.

[^05_0]: Jak opisałem we wstępie, w tej książce zdecydowałem się użyć myślnika między „if”, „when”, „try”, „while”, „for” a słowem je opisującym, jak „warunek”, „pętla”, „instrukcja” lub „wyrażenie”. Robię to, aby poprawić czytelność. Będę więc pisał „if-warunek” zamiast „if warunek”. „if” i „when” to warunki; „while” i „for” to pętle. Wszystkie mogą być używane jako instrukcje, ale tylko „if”, „when” i „try” mogą być używane jako wyrażenia. Zdecydowałem się nie używać myślnika po „if-else” czy „if-else-if” i ich deskryptorze.
[^05_1]: Wyrażenie w programowaniu to część kodu, która zwraca wartość.
[^05_2]: `Unit` to obiekt bez specjalnego znaczenia. Przypomina mi to `Void` w Javie.
[^05_3]: Wyrażenie ciała to specjalna składnia do implementacji ciał funkcji z jednym wyrażeniem. Zostanie to omówione w kolejnym rozdziale.
[^05_4]: Termin "code smell" będę używał do opisania praktyk, które nie są wyraźnie błędne, ale powinny być generalnie unikane.
