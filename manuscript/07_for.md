## Moc pętli for

W Java i innych starszych językach pętla for zwykle ma trzy części: pierwsza inicjuje zmienną przed rozpoczęciem pętli; druga zawiera warunek wykonania bloku kodu; trzecia jest wykonywana po bloku kodu.

__THREE_BACKTICKS__
// Java
for(int i=0; i < 5; i++){
   System.out.println(i);
}
__THREE_BACKTICKS__

Jednak uważa się, że jest to skomplikowane i podatne na błędy. Weźmy pod uwagę sytuację, w której ktoś używa `>` lub `<=` zamiast `<`. Taka mała różnica nie jest łatwa do zauważenia, ale wpływa na zachowanie tej pętli for.

Jako alternatywę dla tej klasycznej pętli for wiele języków wprowadziło nowoczesną alternatywę do iterowania po kolekcjach. Dlatego w językach takich jak Java czy JavaScript mamy dwie zupełnie różne rodzaje pętli for, obie zdefiniowane tym samym słowem kluczowym `for`. Kotlin uprościł to. W Kotlin mamy jedną uniwersalną pętlę for, która może być wyrazisto używana do iterowania po kolekcji, mapie, zakresie liczb i wielu innych.

Ogólnie rzecz biorąc, pętla for jest używana w Kotlin do iterowania po czymś, co jest iterowalne[^07_1].

![](for_described.jpg)

Możemy iterować po listach lub zbiorach.

__THREE_BACKTICKS__kotlin
fun main() {
    val list = listOf("A", "B", "C")
    for (letter in list) {
        print(letter)
    }

    // Typ zmiennej może być jawnie określony
    for (str: String in setOf("D", "E", "F")) {
        print(str)
    }
}
// ABCDEF
__THREE_BACKTICKS__

Możemy również iterować po dowolnym innym obiekcie, o ile zawiera metodę `iterator` bez parametrów, oraz typ wyniku `Iterator` i modyfikator `operator`. Najłatwiejszym sposobem zdefiniowania tej metody jest zaimplementowanie interfejsu `Iterable` przez naszą klasę.

__THREE_BACKTICKS__kotlin
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
__THREE_BACKTICKS__

Wnioskowany typ zmiennej zdefiniowanej wewnątrz pętli for pochodzi z argumentu typu `Iterable`. Gdy iterujemy po `Iterable<User>`, wnioskowany typ elementu będzie `User`. Gdy iterujemy po `Iterable<Long?>`, wnioskowany typ elementu będzie `Long?`. To samo dotyczy wszystkich innych typów.

Ten mechanizm, oparty na `Iterable`, jest naprawdę potężny i pozwala nam pokryć liczne przypadki użycia, z których jednym z najbardziej znanych jest wykorzystanie *zakresów* do wyrażania progresji.

### Zakresy

W Kotlin, jeśli umieścisz dwie kropki pomiędzy dwoma liczbami, jak `1..5`, utworzysz `IntRange`. Ta klasa implementuje `Iterable<Int>`, więc możemy jej użyć w pętli for:

__THREE_BACKTICKS__kotlin
fun main() {
    for (i in 1..5) {
        print(i)
    }
}
// 12345
__THREE_BACKTICKS__

To rozwiązanie jest wydajne, jak również wygodne, ponieważ kompilator Kotlinu optymalizuje jego wydajność "pod maską".

Zakresy utworzone za pomocą `..` obejmują ostatnią wartość (co oznacza, że są to **zamknięte zakresy**). Jeśli chcesz zakresu, który kończy się przed ostatnią wartością, użyj zamiast tego funkcji infiksowej `until`.

__THREE_BACKTICKS__kotlin
fun main() {
    for (i in 1 until 5) {
        print(i)
    }
}
// 1234
__THREE_BACKTICKS__

Zarówno `..`, jak i `until` zaczynają się od wartości po lewej stronie i zmierzają w kierunku prawej liczby z przyrostem o jeden. Jeśli użyjesz większej liczby po lewej stronie, wynikiem będzie pusty zakres.

__THREE_BACKTICKS__kotlin
fun main() {
    for (i in 5..1) {
        print(i)
    }
    for (i in 5 until 1) {
        print(i)
    }
}
// (nic nie jest drukowane)
__THREE_BACKTICKS__

Jeśli chcesz iterować w przeciwnym kierunku, od większych do mniejszych liczb, użyj funkcji `downTo`.

__THREE_BACKTICKS__kotlin
fun main() {
    for (i in 5 downTo 1) {
        print(i)
    }
}
// 54321
__THREE_BACKTICKS__

Domyślnym krokiem we wszystkich tych przypadkach jest `1`. Jeśli chcesz użyć innego kroku, powinieneś użyć funkcji infiksowej `step`.

__THREE_BACKTICKS__kotlin
fun main() {
    for (i in 1..10 step 3) {
        print("$i ")
    }
    println()
    for (i in 1 until 10 step 3) {
        print("$i ")
    }
    println()
    for (i in 10 downTo 1 step 3) {
        print("$i ")
    }
}
// 1 4 7 10 
// 1 4 7 
// 10 7 4 1 
__THREE_BACKTICKS__

### Break i continue

Wewnątrz pętli możemy użyć słów kluczowych `break` i `continue`:
* `break` - kończy najbliższą otaczającą pętlę.
* `continue` - przechodzi do następnego kroku najbliższej otaczającej pętli.

__THREE_BACKTICKS__kotlin
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
__THREE_BACKTICKS__

Oba są stosowane raczej rzadko, a nawet miałem trudności ze znalezieniem choćby jednego realnego przykładu w komercyjnych projektach, które współtworzyłem. Zakładam również, że są dobrze znane programistom, którzy przyszli do Kotlina ze starszych języków. Dlatego tak krótko przedstawiam te słowa kluczowe.

### Przypadki użycia

Programiści z doświadczeniem w starszych językach często używają pętli for tam, gdzie zamiast tego powinny być używane nieco bardziej nowoczesne alternatywy. Na przykład, w niektórych projektach można znaleźć pętlę for, która służy do iterowania po elementach z indeksami.

__THREE_BACKTICKS__kotlin
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
__THREE_BACKTICKS__

To nie jest dobre rozwiązanie. W Kotlin istnieje wiele sposobów, aby zrobić to lepiej.

Po pierwsze, zamiast jawnie iterować po zakresie `0 until names.size`, moglibyśmy użyć właściwości `indices`, która zwraca zakres dostępnych indeksów.

__THREE_BACKTICKS__kotlin
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
__THREE_BACKTICKS__

Po drugie, zamiast iterować po indeksach i znajdować element dla każdego z nich, moglibyśmy zamiast tego iterować po indeksowanych wartościach. Możemy tworzyć indeksowane wartości za pomocą `withIndex` na obiektach iterowalnych. Każda indeksowana wartość zawiera zarówno indeks, jak i wartość. Takie obiekty można destrukturyzować w pętli for[^07_2].

__THREE_BACKTICKS__kotlin
fun main() {
    val names = listOf("Alex", "Bob", "Celina")

    for ((i, name) in names.withIndex()) {
        println("[$i] $name")
    }
}
// [0] Alex
// [1] Bob
// [2] Celina
__THREE_BACKTICKS__

Po trzecie, jeszcze lepszym rozwiązaniem jest użycie `forEachIndexed`, które jest wyjaśnione w kolejnej książce: *Funkcjonalny Kotlin*.

__THREE_BACKTICKS__kotlin
fun main() {
    val names = listOf("Alex", "Bob", "Celina")

    names.forEachIndexed { i, name ->
        println("[$i] $name")
    }
}
// [0] Alex
// [1] Bob
// [2] Celina
__THREE_BACKTICKS__

Innym popularnym przypadkiem użycia jest iterowanie po mapie. Programiści z doświadczeniem w Java często robią to w ten sposób:

__THREE_BACKTICKS__kotlin
fun main() {
    val capitals = mapOf(
        "USA" to "Washington DC",
        "Polska" to "Warszawa",
        "Ukraina" to "Kijów"
    )

    for (entry in capitals.entries) {
        println(
            "Stolica ${entry.key} to ${entry.value}")
    }
}
// Stolica USA to Washington DC
// Stolica Polska to Warszawa
// Stolica Ukraina to Kijów
__THREE_BACKTICKS__

Można to ulepszyć, iterując bezpośrednio po mapie, więc wywołanie entries jest zbędne. Dodatkowo możemy zdestrukturyzować wpisy, aby lepiej nazwać wartości.

__THREE_BACKTICKS__kotlin
fun main() {
    val capitals = mapOf(
        "USA" to "Washington DC",
        "Polska" to "Warszawa",
        "Ukraina" to "Kijów"
    )

    for ((country, capital) in capitals) {
        println("Stolica $country to $capital")
    }
}
// Stolica USA to Washington DC
// Stolica Polska to Warszawa
// Stolica Ukraina to Kijów
__THREE_BACKTICKS__

Możemy użyć forEach dla mapy.

__THREE_BACKTICKS__kotlin
fun main() {
    val capitals = mapOf(
        "USA" to "Washington DC",
        "Poland" to "Warsaw",
        "Ukraine" to "Kiev"
    )

    capitals.forEach { (country, capital) ->
        println("Stolica $country to $capital")
    }
}
// Stolica USA to Washington DC
// Stolica Polska to Warszawa
// Stolica Ukraina to Kijów
__THREE_BACKTICKS__

### Podsumowanie

W tym rozdziale nauczyliśmy się korzystać z pętli for. W Kotlin jest naprawdę prosta i potężna, więc warto wiedzieć, jak działa, mimo że nie jest używana zbyt często (ze względu na niesamowite funkcje funkcyjne Kotlin, które często są używane zamiast niej).

Teraz porozmawiajmy o jednym z najważniejszych ulepszeń Kotlin względem Javy: dobrym wsparciu dla obsługi wartości null.

[^07_1]: Ma metodę operatora `iterator`.
[^07_2]: Destrukturyzacja będzie wyjaśniona bardziej szczegółowo w rozdziale *Data klasy*.

