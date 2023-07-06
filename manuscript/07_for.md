## Supermoce pętli for

W Javie i innych starszych językach pętla for zwykle ma trzy części: pierwsza inicjuje zmienną przed rozpoczęciem pętli; druga zawiera warunek wykonania bloku kodu; trzecia jest wykonywana po bloku kodu.

```
// pętla for w Javie
for(int i=0; i < 5; i++){
   System.out.println(i);
}
```

Taki zapis jest jednak dość skomplikowany i łatwo w nim o błąd. Wyobraź sobie, że ktoś przypadkiem użyje `>` lub `<=` zamiast `<`. Taka mała różnica nie jest łatwa do zauważenia, ale istotnie wpływa na zachowanie tej pętli for.

Zamiast klasycznej pętli for, wiele języków wprowadziło nowocześniejszą alternatywę zaprojektowaną do iterowania po kolekcjach. Dlatego w językach takich jak Java czy JavaScript mamy dwie zupełnie różne rodzaje pętli for, obie zdefiniowane tym samym słowem kluczowym. W Kotlinie jest prościej, mamy jedną uniwersalną pętlę for, która może być używana do iterowania po kolekcji, mapie, zakresie liczb i wielu innych.

Ogólnie rzecz biorąc, pętla for jest używana w Kotlinie do iterowania po czymś, co jest iterowalne[^07_1].

![](for_described.jpg)

Możemy iterować po listach lub zbiorach.

```kotlin
fun main() {
    val list = listOf("A", "B", "C")
    for (letter in list) {
        print(letter)
    } // ABC

    // Typ zmiennej może być jawnie określony
    for (str: String in setOf("D", "E", "F")) {
        print(str)
    } // DEF
}
```

Możemy również iterować po dowolnym innym obiekcie, o ile zawiera metodę `iterator` bez parametrów i z modyfikatorem `operator`, zwracającej typ `Iterator`. Ten warunek jest spełniony przez wszystkie klasy implementujące interfejs `Iterable`.

```kotlin
fun main() {
    val tree = Tree(
        value = "B",
        left = Tree("A"),
        right = Tree("D", left = Tree("C"))
    )

    for (value in tree) {
        print(value) // ABCD
    }
}

class Tree(
    val value: String,
    val left: Tree? = null,
    val right: Tree? = null,
) : Iterable<String> {

    override fun iterator(): Iterator<String> = iterator {
        if (left != null) yieldAll(left)
        yield(value)
        if (right != null) yieldAll(right)
    }
}
```

Wnioskowany typ zmiennej zdefiniowanej wewnątrz pętli for pochodzi z argumentu typu `Iterable`. Gdy iterujemy po `Iterable<User>`, wnioskowany typ elementu to `User`. Gdy iterujemy po `Iterable<Long?>`, wnioskowany typ elementu to `Long?`. To samo dotyczy wszystkich innych typów.

Ten mechanizm, oparty na `Iterable`, jest naprawdę potężny i pozwala nam pokryć liczne przypadki użycia, z których jednym z najpopularniejszych jest wykorzystanie *zakresów*.

### Zakresy

Jeśli umieścisz dwie kropki pomiędzy dwiema liczbami, jak w `1..5`, utworzysz `IntRange`, czyli zakres liczb od 1 do 5. `IntRange` implementuje `Iterable<Int>`, więc możemy jej użyć w pętli for:

```kotlin
fun main() {
    for (i in 1..5) {
        print(i)
    }
}
// 12345
```

To rozwiązanie jest nie tylko wygodne, ale również wydajne, ponieważ kompilator Kotlina optymalizuje je do efektywnej iteracji po numerach (a więc jeśli nie jest to naprawdę konieczne, obiekt klasy `IntRange` nie powstaje).

Zakresy utworzone za pomocą `..` obejmują ostatnią wartość (co oznacza, że są to **zamknięte zakresy**). Jeśli chcesz zakresu, który kończy się przed ostatnią wartością, użyj zamiast tego funkcji infiksowej `until`.

```kotlin
fun main() {
    for (i in 1 until 5) {
        print(i)
    }
}
// 1234
```

Zarówno `..`, jak i `until` zaczynają się od wartości po lewej stronie i zmierzają w kierunku prawej liczby z przyrostem o jeden. Jeśli użyjesz większej liczby po lewej stronie, wynikiem będzie pusty zakres.

```kotlin
fun main() {
    for (i in 5..1) {
        print(i)
    }
    for (i in 5 until 1) {
        print(i)
    }
}
// (nic nie jest drukowane)
```

Jeśli chcesz iterować w przeciwnym kierunku, od większych do mniejszych liczb, użyj funkcji `downTo`.

```kotlin
fun main() {
    for (i in 5 downTo 1) {
        print(i)
    }
}
// 54321
```

Domyślnym krokiem we wszystkich tych przypadkach jest `1`. Jeśli chcesz użyć innego kroku, powinieneś użyć funkcji infiksowej `step`.

```kotlin
fun main() {
    for (i in 1..10 step 3) {
        print("$i ")
    } // 1 4 7 10 

    for (i in 1 until 10 step 3) {
        print("$i ")
    } // 1 4 7 

    for (i in 10 downTo 1 step 3) {
        print("$i ")
    } // 10 7 4 1 
}
```

### Break oraz continue

Wewnątrz pętli możemy użyć słów kluczowych `break` i `continue`:

* `break` - kończy najbliższą otaczającą pętlę.
* `continue` - przechodzi do następnego kroku najbliższej otaczającej pętli.

```kotlin
fun main() {
    for (i in 1..5) {
        if (i == 3) break
        print(i)
    }
    // 12

    println()

    for (i in 1..5) {
        if (i == 3) continue
        print(i)
    }
    // 1245
}
```

> Oba są stosowane raczej rzadko, a nawet miałem trudności ze znalezieniem choćby jednego realnego przykładu w większości komercyjnych projektów, które współtworzyłem. Zakładam również, że są dobrze znane programistom, którzy przyszli do Kotlina ze starszych języków. Dlatego tak krótko je przedstawiam.

### Przypadki użycia

Programiści z doświadczeniem w starszych językach często iterują po liczbach tam, gdzie powinny być używane nieco bardziej nowoczesne alternatywy. Na przykład, w niektórych projektach można znaleźć pętlę for, która służy do iterowania po elementach z indeksami.

```kotlin
fun main() {
    val names = listOf("Alex", "Bob", "Celina")

    for (i in 0 until names.size) {
        val name = names[i]
        println("[$i] $name")
    }
}
// [0] Alex
// [1] Bob
// [2] Celina
```

To nie jest dobre rozwiązanie. W Kotlinie istnieje wiele sposobów, aby zrobić to lepiej.

Po pierwsze, zamiast jawnie iterować po zakresie `0 until names.size`, moglibyśmy użyć właściwości `indices`, która zwraca zakres dostępnych indeksów.

```kotlin
fun main() {
    val names = listOf("Alex", "Bob", "Celina")

    for (i in names.indices) {
        val name = names[i]
        println("[$i] $name")
    }
}
// [0] Alex
// [1] Bob
// [2] Celina
```

Po drugie, zamiast iterować po indeksach i znajdować element dla każdego z nich, moglibyśmy iterować po indeksowanych wartościach. Możemy tworzyć indeksowane wartości za pomocą `withIndex`. Każda indeksowana wartość zawiera zarówno indeks, jak i wartość. Takie obiekty można destrukturyzować w pętli for[^07_2].

```kotlin
fun main() {
    val names = listOf("Alex", "Bob", "Celina")

    for ((i, name) in names.withIndex()) {
        println("[$i] $name")
    }
}
// [0] Alex
// [1] Bob
// [2] Celina
```

Po trzecie, jeszcze lepszym rozwiązaniem jest użycie `forEachIndexed`, które jest wyjaśnione w kolejnej książce: *Funkcyjny Kotlin*.

```kotlin
fun main() {
    val names = listOf("Alex", "Bob", "Celina")

    names.forEachIndexed { i, name ->
        println("[$i] $name")
    }
}
// [0] Alex
// [1] Bob
// [2] Celina
```

Innym popularnym przypadkiem użycia jest iterowanie po mapie. Programiści z doświadczeniem w Javie często robią to w ten sposób:

```kotlin
fun main() {
    val capitals = mapOf(
        "USA" to "Washington",
        "Polski" to "Warszawa",
        "Ukrainy" to "Kijów"
    )

    for (entry in capitals.entries) {
        println("Stolicą ${entry.key} jest ${entry.value}.")
    }
}
// Stolicą USA jest Washington.
// Stolicą Polski jest Warszawa.
// Stolicą Ukrainy jest Kijów.
```

Ten kod można poprawić, iterując bezpośrednio po mapie, więc wywołanie `entries` jest zbędne. Dodatkowo możemy zdestrukturyzować wpisy, aby lepiej nazwać wartości.

```kotlin
fun main() {
    val capitals = mapOf(
        "USA" to "Washington",
        "Polski" to "Warszawa",
        "Ukrainy" to "Kijów"
    )

    for ((country, capital) in capitals) {
        println("Stolicą $country jest $capital.")
    }
}
// Stolicą USA jest Washington.
// Stolicą Polski jest Warszawa.
// Stolicą Ukrainy jest Kijów.
```

Możemy również użyć `forEach` dla mapy, co zostanie wyjaśnione w książce *Funkcyjny Kotlin*.

```kotlin
fun main() {
    val capitals = mapOf(
        "USA" to "Washington",
        "Polski" to "Warszawa",
        "Ukrainy" to "Kijów"
    )

    capitals.forEach { (country, capital) ->
        println("Stolicą $country jest $capital.")
    }
}
// Stolicą USA jest Washington.
// Stolicą Polski jest Warszawa.
// Stolicą Ukrainy jest Kijów.
```

### Podsumowanie

W tym rozdziale nauczyliśmy się korzystać z pętli for. W Kotlinie ta pętla jest naprawdę prosta i daje niezwykłe możliwości, więc warto wiedzieć, jak działa.

Teraz porozmawiajmy o jednym z najważniejszych ulepszeń, jakie Kotlin wprowadził do świata JVM: dobrym wsparciu dla obsługi wartości `null`.

[^07_1]: Ma metodę `iterator`.
[^07_2]: Destrukturyzacja będzie wyjaśniona bardziej szczegółowo w rozdziale *Data klasy*.
