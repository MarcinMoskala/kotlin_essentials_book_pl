## Kolekcje

Kolekcje to jeden z najważniejszych pojęć w programowaniu. Są to typy reprezentujące grupy elementów. W Kotlinie najważniejszymi typami kolekcji są:

* `List`, który reprezentuje uporządkowaną kolekcję elementów. Te same elementy mogą wystąpić wiele razy. Elementy listy można uzyskać, używając indeksów (liczb całkowitych z zerem na początku, które odzwierciedlają pozycje elementów). Przykładem może być lista utworów w kolejce: ważna jest kolejność utworów, a każdy utwór może wystąpić w wielu miejscach.

* `Set`, który reprezentuje kolekcję unikalnych elementów. Odzwierciedla matematyczną abstrakcję zbioru: grupę obiektów bez powtórzeń. Zbiory mogą nie szanować kolejności elementów (jednak domyślny zbiór używany przez Kotlin szanuje kolejność elementów). Przykładem może być zbiór wygrywających numerów w loterii: muszą być unikalne, ale ich kolejność nie ma znaczenia.

* `Map` (znany jako słownik w niektórych innych językach), który reprezentuje zbiór par klucz-wartość. Klucze muszą być unikalne i każdy z nich wskazuje dokładnie jedną wartość. Wiele kluczy może być powiązanych z tymi samymi wartościami. Mapy są przydatne do wyrażania logicznych połączeń między elementami.

Istnieją również tablice, które zwykle uważane są za niskopoziomowe prymitywy używane przez inne kolekcje "pod spodem".

W tym rozdziale omówimy najważniejsze zagadnienia dotyczące kolekcji, zaczynając od tego, jak są one organizowane, jak są tworzone, specjalnych rodzajów kolekcji i jak wszystkie te rodzaje kolekcji są wykorzystywane w praktyce. To długi rozdział, więc zaczynajmy.

### Hierarchia interfejsów

W Kotlinie cała hierarchia interfejsów służy do reprezentowania różnych rodzajów kolekcji. Spójrz na poniższy diagram.

![Relacje między interfejsami reprezentującymi kolekcje. Niebieskie elementy są tylko do odczytu. Pomarańczowe elementy są modyfikowalne. Klasy takie jak `ArrayList` czy `HashSet` implementują warianty modyfikowalne, ale można je rzutować na wersje tylko do odczytu.](collections_interfaces.png)

Na szczycie hierarchii znajduje się `Iterable`, który reprezentuje sekwencję elementów, po której można iterować. Możemy iterować po obiektach `Iterable` używając pętli for, dzięki jej metodzie `iterator`.

```kotlin
interface Iterable<out T> {
    operator fun iterator(): Iterator<T>
}
```

Następnym typem jest `Collection`, który reprezentuje kolekcję elementów. Jego metody są tylko do odczytu (nie ma metod do manipulowania elementami), więc ten interfejs nie pozwala na żadne modyfikacje.

```kotlin
interface Collection<out E> : Iterable<E> {
    val size: Int
    fun isEmpty(): Boolean
    operator fun contains(element: E): Boolean
    override fun iterator(): Iterator<E>
    fun containsAll(elements: Collection<E>): Boolean
}
```

Zauważ, że tylko `List` i `Set` są podtypami `Collection` i `Iterable`. `Map` i tablice nie są częścią tej hierarchii; jednak możemy również iterować po nich używając pętli for.

Wszystkie opisane dotąd interfejsy są tylko do odczytu, więc mają metody, które pozwalają odczytać to, co jest w środku (takie jak `get`, `contains`, `size`), ale nie modyfikować. `MutableIterable`, `MutableCollection` i `MutableList` to podinterfejsy odpowiednio `Iterable`, `Collection` i `List`, które dodają metody do modyfikowania elementów (takie jak `remove`, `clear`, `add`).

Rzeczywiste klasy używane podczas pracy z kolekcjami są zależne od platformy. Na przykład, jeśli utworzysz listę za pomocą funkcji `listOf("A", "B")` w wersji Kotlin/JVM 1.7.20, rzeczywistą klasą jest `Arrays.ArrayList` z biblioteki standardowej Java; jednak jeśli użyjesz Kotlin/JS w wersji 1.7.20, rzeczywistą klasą jest tablica JavaScript. Chodzi o to, aby oczekiwać nie konkretnej klasy, ale obiektu reprezentującego listę (a więc implementującego interfejs `List`). Jest to zastosowanie ogólnego pomysłu: używaj abstrakcji, aby ułatwić zmianę reprezentacji (np. ze względu na wydajność) bez wprowadzania zmian powodujących błędy[^17_0].

### Typy modyfikowalne i tylko do odczytu

Różnica między modyfikowalnymi i tylko do odczytu interfejsami jest bardzo ważna. Na przykład funkcja `listOf` zwraca `List`, który reprezentuje kolekcję tylko do odczytu. `List` nie ma żadnych funkcji, które pozwalałyby na jego modyfikację (funkcje takie jak `add` czy `remove`). Oznacza to, że obiekt kolekcji nie może ulec zmianie, ale to nie oznacza, że nie można zaktualizować zmiennej zawierającej kolekcję.

Podobnie jest z `Int` czy `String`. Oba są niezmienne, więc nie mogą się zmieniać wewnętrznie; jednak możemy zaktualizować ich wartości za pomocą operatorów, takich jak plus.

  ```kotlin
fun main() {
    var a = 100
    a = a + 10
    a += 1
    println(a) // 111

    var str = "A"
    str = str + "B"
    str += "C"
    println(str) // ABC
}
```

To samo dotyczy kolekcji tylko do odczytu: możemy użyć operatorów, aby utworzyć nową kolekcję z zaktualizowaną wartością.

```kotlin
fun main() {
    var list = listOf("A", "B")
    list = list + "C"
    println(list) // [A, B, C]
    list = list + listOf("D", "E")
    println(list) // [A, B, C, D, E]
    list = listOf("Z") + list
    println(list)  // [Z, A, B, C, D, E]
}
```

W przeciwieństwie do list tylko do odczytu, listy modyfikowalne mogą być modyfikowane wewnętrznie. Więc jeśli utworzysz kolekcję za pomocą funkcji `mutableListOf`, wynikowy obiekt to `MutableList`, który obsługuje operacje takie jak `add`, `clear` czy `remove`.

```kotlin
fun main() {
    val mutable = mutableListOf("A", "B")
    mutable.add("C")
    mutable.remove("A")
    println(mutable) // [B, C]
}
```

Można łatwo przekształcić między listą modyfikowalną a listą tylko do odczytu za pomocą `toList` i `toMutableList`. Jednak często nie musisz jawnie przekształcać z `MutableList` na `List`. `MutableList` jest podtypem `List`, więc `MutableList` może być używana jako `List`.

W nadchodzących sekcjach zobaczymy najważniejsze operatory do modyfikacji kolekcji tylko do odczytu oraz metody, które można użyć do modyfikacji modyfikowalnych kolekcji.

### Tworzenie kolekcji

Większość języków programowania obsługuje funkcję o nazwie "dosłowny zapis kolekcji" (ang. "collection literal"), który jest specjalną składnią umożliwiającą tworzenie określonego typu kolekcji na podstawie dostarczonej listy elementów.

```
// JavaScript
const arr = ["A", "B"] // tablica stringów
// Python
numbers = [1, 2, 3] // lista liczb
names = {"Alex", "Barbara"} // zbiór stringów
```

W Kotlinie rolę tę pełnią funkcje na najwyższym poziomie. Zgodnie z konwencją, ich nazwy zaczynają się od nazwy typu, który produkują (zaczynając od małej litery) i kończą się na przyrostku `Of`. Oto kilka przykładów.

```kotlin
fun main() {
    // Tworzymy `List` używając funkcji `listOf`.
    val list: List<String> = listOf("A", "B", "C")
    // Tworzymy `MutableList` używając funkcji `mutableListOf`.
    val mutableList: MutableList<Int> = mutableListOf(1, 2, 3)

    // Tworzymy `Set` używając funkcji `setOf`.
    val set: Set<Double> = setOf(3.14, 7.11)
    // Tworzymy `MutableSet` używając funkcji `mutableSetOf`.
    val mutableSet: MutableSet<Char> = mutableSetOf('A', 'B')

    // Tworzymy `Map` używając funkcji `mapOf`.
    val map: Map<Char, String> =
        mapOf('A' to "Alex", 'B' to "Ben")
    // Tworzymy `MutableMap` używając funkcji `mutableMapOf`.
    val mutableMap: MutableMap<Int, Char> =
        mutableMapOf(1 to 'A', 2 to 'B')

    // Tworzymy `Array` używając funkcji `arrayOf`.
    val array: Array<String> = arrayOf("Dukaj", "Sapkowski")
    // Tworzymy `IntArray` używając funkcji `intArrayOf`.
    val intArray: IntArray = intArrayOf(9, 8, 7)

    // Tworzymy `ArrayList` używając funkcji `arrayListOf`.
    val arrayList: ArrayList<String> = arrayListOf("M", "N")
}
```

Do wszystkich tych klas przekazujemy początkowe elementy jako argumenty. Jedynym wyjątkiem jest mapa, która jest zbiorem par klucz-wartość, więc początkowe pary określamy za pomocą `Pair`, który zwykle tworzymy za pomocą funkcji `to` (jak wyjaśniono w rozdziale *Data classes*).

Możemy również przekształcić jedną kolekcję w inną. Często można to zrobić za pomocą metody, której nazwa jest typem, którego chcemy osiągnąć, poprzedzonej prefiksem `to`.

```kotlin
fun main() {
    val list: List<Char> = listOf('A', 'B', 'C')
    val mutableList: MutableList<Char> = list.toMutableList()
    val set: Set<Char> = mutableList.toSet()
    val mutableSet: MutableList<Char> = set.toMutableList()
    val array: Array<Char> = mutableSet.toTypedArray()
    val charArray: CharArray = array.toCharArray()
    val list2: List<Char> = charArray.toList()
}
```

### Listy

**List** to najbardziej podstawowy typ kolekcji. Można go traktować jako domyślny typ kolekcji. Reprezentuje uporządkowaną listę elementów.

```kotlin
fun main() {
    val list = listOf("A", "B", "C")
    println(list) // [A, B, C]
}
```

`List` to klasa generyczna. Typem wynikowym `listOf` jest `List<T>`, gdzie `T` to typ elementów na tej liście. Ponieważ mamy listę ze stringami w powyższym kodzie, typem jest `List<String>`. Więcej o klasach generycznych w rozdziale *Generics*.

```kotlin
fun main() {
    val list: List<String> = listOf("A", "B", "C")
    println(list) // [A, B, C]
    val ints: List<Int> = listOf(1, 2, 3)
    println(ints) // [1, 2, 3]
}
```

#### Modyfikowanie list

Gdy potrzebujesz modyfikować elementy listy, masz dwie opcje:
1. Użyj listy tylko do odczytu w zmiennej `var` i modyfikuj ją za pomocą operatorów takich jak plus lub minus.
2. Użyj modyfikowalnej listy w zmiennej `val` i modyfikuj ją za pomocą metod `MutableList` takich jak `add`, `addAll` czy `remove`.

```kotlin
fun main() {
    var list = listOf("A", "B")
    list = list + "C"
    println(list) // [A, B, C]
    list = list + listOf("D", "E")
    println(list) // [A, B, C, D, E]
    list = listOf("Z") + list
    println(list) // [Z, A, B, C, D, E]
    list = list - "A"
    println(list) // [Z, B, C, D, E]

    val mutable = mutableListOf("A", "B")
    mutable.add("C")
    println(mutable) // [A, B, C]
    mutable.addAll(listOf("D", "E"))
    println(mutable) // [A, B, C, D, E]
    mutable.add(0, "Z") // Pierwsza liczba to indeks
    println(mutable) // [Z, A, B, C, D, E]
    mutable.remove("A")
    println(mutable) // [Z, B, C, D, E]
}
```

Od początku Kotlinu trwają dyskusje, która z tych dwóch podejść powinna być preferowana. Pierwsze daje większą swobodę[^17_2], ale drugie jest uważane za bardziej wydajne[^17_3].

Możesz również użyć operatora `+=` do dodawania elementu lub kolekcji do zmiennej `var`, która wskazuje na listę tylko do odczytu, lub do zmiennej `var`, która wskazuje na modyfikowalną listę.

```kotlin
fun main() {
    var list = listOf("A", "B")
    list += "C"
    println(list) // [A, B, C]

    val mutable = mutableListOf("A", "B")
    mutable += "C"
    println(mutable) // [A, B, C]
}
```

Jednak używanie `+=` dla list tylko do odczytu powoduje ostrzeżenie, że w tle została utworzona nowa kolekcja, co może prowadzić do problemów z wydajnością, gdy mamy do czynienia z dużymi listami.

![](plusAssign.png)

#### Sprawdzanie rozmiaru listy lub czy jest pusta

Liczba elementów na liście można uzyskać za pomocą właściwości `size`.

```kotlin
fun main() {
    val list = listOf("A", "B", "C")
    println(list.size) // 3
}
```

Lista jest uważana za pustą, gdy jej rozmiar wynosi `0`. Można to również sprawdzić za pomocą metody `isEmpty`.

```kotlin
fun main() {
    val list = listOf("A", "B", "C")
    println(list.size == 0) // false
    println(list.isEmpty()) // false

    val empty: Set<Int> = setOf()
    println(empty.size == 0) // true
    println(empty.isEmpty()) // true
}
```

#### Listy i indeksy

Listy pozwalają na pobieranie elementów według ich indeksu, który jest liczbą reprezentującą pozycję elementu. Indeks pierwszego elementu zawsze wynosi 0, a każdy kolejny element na liście ma kolejny indeks. Można sobie wyobrazić listę jako nieskończoną półkę na przedmioty, gdzie pod każdym przedmiotem znajduje się etykieta z numerem.

![](shelf.png)

Aby uzyskać element według indeksu, używamy nawiasów kwadratowych. Jest to synonim metody `get`. Obie te metody rzucają wyjątek `IndexOutOfBoundsException`, gdy próbujesz uzyskać element o indeksie, który nie istnieje.

```kotlin
fun main() {
    val list = listOf("A", "B")
    println(list[1]) // B
    println(list.get(1)) // B
    println(list[3]) // Błąd wykonania
}
```

Jeśli nie jesteś pewien, czy indeks jest poprawny, bezpieczniej jest użyć `getOrNull`, który zwraca `null` w przypadku niepoprawnego indeksu, lub `getOrElse`, który określa wartość domyślną[^17_1].

```kotlin
fun main() {
    val list = listOf("A", "B")
    println(list.getOrNull(1)) // B
    println(list.getOrElse(1) { "X" }) // B

    println(list.getOrNull(3)) // null
    println(list.getOrElse(3) { "X" }) // X
}
```

Możesz znaleźć indeks elementu, używając metody `indexOf`. Zwraca ona `-1`, gdy na liście nie ma pasującego elementu.

```kotlin
fun main() {
    val list = listOf("A", "B")
    println(list.indexOf("A")) // 0
    println(list.indexOf("B")) // 1
    println(list.indexOf("Z")) // -1
}
```

W przypadku listy mutowalnej możesz zmodyfikować element o określonym indeksie, używając nawiasów kwadratowych w przypisaniu lub metody `set`.

```kotlin
fun main() {
    val mutable = mutableListOf("A", "B", "C")
    mutable[1] = "X"
    println(mutable) // [A, X, C]
    mutable.set(1, "Y")
    println(mutable) // [A, Y, C]
}
```

#### Sprawdzanie, czy lista zawiera element

Można sprawdzić, czy lista zawiera element, używając metody `contains` lub operatora `in`.

```kotlin
fun main() {
    val letters = listOf("A", "B", "C")
    println(letters.contains("A")) // true
    println(letters.contains("Z")) // false
    println("A" in letters) // true
    println("Z" in letters) // false
}
```

Można także sprawdzić, czy kolekcja nie zawiera elementu, używając operatora `!in`.

```kotlin
fun main() {
    val letters = listOf("A", "B", "C")
    println("A" !in letters) // false
    println("Z" !in letters) // true
}
```

#### Iterowanie po liście

Możesz iterować po liście używając pętli for. Wystarczy umieścić listę po prawej stronie `in`.

```kotlin
fun main() {
    val letters = listOf("A", "B", "C")
    for (letter in letters) {
        print(letter)
    }
}
```

Ponieważ `MutableList` implementuje `List`, wszystkie te operacje mogą być również używane na listach mutowalnych.

To są najbardziej podstawowe operacje na listach. Omówimy więcej z nich w kolejnej książce: *Funkcyjny Kotlin*.

### Zbiory

Używamy **zbiorów** zamiast list, gdy:
1. chcemy zapewnić, że elementy w naszej kolekcji są unikalne (zbiory przechowują tylko unikalne elementy),
2. często szukamy elementu w kolekcji (znalezienie elementów w zbiorze jest znacznie bardziej efektywne niż w liście[^17_6]).

Zbiory są dość podobne do list, dlatego używa się podobnych metod do operacji na nich. Jednak zbiory nie traktują porządku tak poważnie jak listy, a niektóre rodzaje zbiorów w ogóle nie szanują porządku. Dlatego nie możemy uzyskać elementów według indeksu.

Tworzymy zbiór, używając funkcji `setOf`; następnie określamy jego wartości za pomocą argumentów.

```kotlin
fun main() {
    val set = setOf('A', 'B', 'C')
    println(set) // [A, B, C]
}
```

`Set` jest klasą generyczną. Typem wynikowym `setOf` jest `Set<T>`, gdzie `T` to typ elementów w tym zbiorze. Ponieważ mamy zbiór z wartościami typu char w powyższym kodzie, typem jest `Set<Char>`.

```kotlin
fun main() {
    val set: Set<Char> = setOf('A', 'B', 'C')
    println(set) // [A, B, C]
    val ints: Set<Long> = setOf(1L, 2L, 3L)
    println(ints) // [1, 2, 3]
}
```

#### Modyfikowanie zbiorów

Możesz dodawać elementy do zbioru tylko do odczytu w ten sam sposób, jak do listy tylko do odczytu: używając plusa lub minusa.

```kotlin
fun main() {
    var set = setOf("A", "B")
    set = set + "C"
    println(set) // [A, B, C]
    set = set + setOf("D", "E")
    println(set) // [A, B, C, D, E]
    set = setOf("Z") + set
    println(set) // [Z, A, B, C, D, E]
    set = set - "A"
    println(set) // [Z, B, C, D, E]
}
```

Możesz również użyć mutowalnego zbioru i jego metod `add`, `addAll` lub `remove`.

```kotlin
fun main() {
    val mutable = mutableSetOf("A", "B")
    mutable.add("C")
    println(mutable) // [A, B, C]
    mutable.addAll(listOf("D", "E"))
    println(mutable) // [A, B, C, D, E]
    mutable.remove("B")
    println(mutable) // [A, C, D, E]
}
```

#### Elementy w zbiorze są unikalne

Zbiory akceptują tylko unikalne elementy. Jeśli elementy się powtarzają podczas tworzenia zbioru, tylko pierwsze wystąpienie będzie obecne w zbiorze.

```kotlin
fun main() {
    val set = setOf("A", "B", "C", "B")
    println(set) // [A, B, C]
}
```

Dodanie elementu, który jest równy elementowi już obecnemu w zbiorze, jest ignorowane.

```kotlin
fun main() {
    val set = setOf("A", "B", "C")
    println(set + "D") // [A, B, C, D]
    println(set + "B") // [A, B, C]

    val mutable = mutableSetOf("A", "B", "C")
    mutable.add("D")
    mutable.add("B")
    println(mutable) // [A, B, C, D]
}
```

Dwa elementy są uważane za różne, gdy porównując je za pomocą podwójnego znaku równości zwróci `false`.

```kotlin
// domyślnie każdy obiekt z regularnej klasy jest unikalny
class Cat(val name: String)

// jeśli użyty jest modyfikator data,
// dwie instancje o tych samych właściwościach są równe
data class Dog(val name: String)

fun main() {
    val cat1 = Cat("Garfield")
    val cat2 = Cat("Garfield")
    println(cat1 == cat2) // false
    println(setOf(cat1, cat2)) // [Cat@4eec7777, Cat@3b07d329]

    val dog1 = Dog("Rex")
    val dog2 = Dog("Rex")
    println(dog1 == dog2) // true
    println(setOf(dog1, dog2)) // [Dog(name=Rex)]
}
```

Najbardziej efektywnym sposobem usunięcia duplikatów z listy jest przekształcenie jej w zbiór.

```kotlin
fun main() {
    val names = listOf("Jake", "John", "Jake", "James", "Jan")
    println(names) // [Jake, John, Jake, James, Jan]
    val unique = names.toSet()
    println(unique) // [Jake, John, James, Jan]
}
```

#### Sprawdzanie rozmiaru zbioru lub czy jest pusty

Liczba elementów w zbiorze można sprawdzić za pomocą właściwości `size`.

```kotlin
fun main() {
    val set = setOf('A', 'B', 'C')
    println(set.size) // 3
}
```

Aby sprawdzić, czy zbiór jest pusty, można porównać jego rozmiar z `0` lub użyć metody `isEmpty`.

```kotlin
fun main() {
    val set = setOf('A', 'B', 'C')
    println(set.size == 0) // false
    println(set.isEmpty()) // false

    val empty: Set<Int> = setOf()
    println(empty.size == 0) // true
    println(empty.isEmpty()) // true
}
```

#### Sprawdzanie, czy zbiór zawiera element

Można sprawdzić, czy zbiór zawiera określony element, używając metody `contains` lub operatora `in`. Obydwie te opcje zwracają `true`, jeśli w zbiorze znajduje się element równy szukanemu elementowi; w przeciwnym razie zwracają `false`.

```kotlin
fun main() {
    val letters = setOf('A', 'B', 'C')
    println(letters.contains('A')) // true
    println(letters.contains('Z')) // false
    println('A' in letters) // true
    println('Z' in letters) // false
}
```

Można również sprawdzić, czy zbiór nie zawiera elementu, używając operatora `!in`.

```kotlin
fun main() {
    val letters = setOf("A", "B", "C")
    println("A" !in letters) // false
    println("Z" !in letters) // true
}
```

#### Iterowanie po zbiorach

Można iterować po zbiorze za pomocą pętli for. Wystarczy umieścić zbiór po prawej stronie `in`.

```kotlin
fun main() {
    val letters = setOf('A', 'B', 'C')
    for (letter in letters) {
        print(letter)
    }
}
```

### Mapy

Mapy służą do przechowywania powiązań między kluczami a ich wartościami. Na przykład:
* Od identyfikatora użytkownika do obiektu reprezentującego tego użytkownika.
* Od adresu strony internetowej do jej adresu IP.
* Od nazwy konfiguracji do danych przechowywanych w tej konfiguracji.

```kotlin
class CachedApiArticleRepository(
    val articleApi: ArticleApi
) {
    val articleCache: MutableMap<String, String> =
        mutableMapOf()

    fun getContent(key: String) =
        articleCache.getOrPut(key) {
            articleApi.fetchContent(key)
        }
}

class DeliveryMethodsConfiguration(
    val deliveryMethods: Map<String, DeliveryMethod>
)

class TokenRepository {
    private var tokenToUser: Map<String, User> = mapOf()

    fun getUser(token: String) = tokenToUser[token]

    fun addToken(token: String, user: User) {
        tokenToUser[token] = user
    }
}
```

Możesz utworzyć mapę za pomocą funkcji `mapOf`, a następnie użyć par klucz-wartość jako argumentów, aby określić powiązania klucz-wartość. Na przykład mogę zdefiniować mapę, która łączy kraje ze swoimi stolicami. Pary można zdefiniować za pomocą konstruktora lub funkcji `to`.

```kotlin
fun main() {
    val capitals = mapOf(
        "USA" to "Washington DC",
        "Poland" to "Warsaw",
    )
//    val capitals = mapOf(
//        Pair("USA", "Washington DC"),
//        Pair("Poland", "Warsaw"),
//    )
    println(capitals) // {USA=Washington DC, Poland=Warsaw}
}
```

`Map` to klasa generyczna. Typem wynikowym jest `Map<K, V>`, gdzie `K` to typ klucza, a `V` to typ wartości. W przypadku mapy z powyższej zmiennej `capitals` zarówno klucze, jak i wartości są typu `String`, więc typem mapy jest `Map<String, String>`. Jednak klucz nie musi być tego samego typu co jego wartość. Rozważ mapę z powiązaniami między literami a ich pozycjami w alfabecie angielskim, jak w poniższym przykładzie. Jej typem jest `Map<Char, Int>`, ponieważ kluczami są znaki, a wartościami są liczby całkowite.

```kotlin
fun main() {
    val capitals: Map<String, String> = mapOf(
        "USA" to "Washington DC",
        "Poland" to "Warsaw",
    )
    println(capitals) // {USA=Washington DC, Poland=Warsaw}

    val alphabet: Map<Char, Int> =
        mapOf('A' to 1, 'B' to 2, 'C' to 3)
    println(alphabet) // {A=1, B=2, C=3}
}
```

#### Wyszukiwanie wartości według klucza

Aby znaleźć wartość według klucza, można użyć funkcji `get` lub nawiasów kwadratowych z kluczem. Na przykład, aby znaleźć wartość dla klucza `'A'` w mapie `alphabet`, użyj `alphabet.get('A')` lub `alphabet['A']`. Wynik ma nullable typ wartości, który w tym przypadku to `Int?`. Dlaczego nullable? Jeśli klucza, o który pytasz, nie ma w mapie, zwrócona zostanie wartość `null`.

```kotlin
fun main() {
    val alphabet: Map<Char, Int> =
        mapOf('A' to 1, 'B' to 2, 'C' to 3)
    val number: Int? = alphabet['A']
    println(number) // 1
    println(alphabet['B']) // 2
    println(alphabet['&']) // null
}
```

Wszystkie podstawowe mapy są zoptymalizowane, aby wyszukiwanie wartości według klucza było bardzo szybką operacją[^17_7].

#### Dodawanie elementów do mapy

Podobnie jak w przypadku zwykłej listy czy zwykłego zbioru, zwykła mapa jest tylko do odczytu, więc nie ma metod, które pozwalałyby na dodawanie lub usuwanie elementów. Można jednak użyć znaku plus, aby utworzyć nową mapę z nowymi wpisami. Jeśli dodasz parę do mapy, wynikiem jest mapa z nowym wpisem. Jeśli dodasz dwie mapy razem, wynikiem jest ich połączenie.

```kotlin
fun main() {
    val map1 = mapOf('A' to "Alex", 'B' to "Bob")
    val map2 = map1 + ('C' to "Celina")
    println(map1) // {A=Alex, B=Bob}
    println(map2) // {A=Alex, B=Bob, C=Celina}
    val map3 = mapOf('D' to "Daniel", 'E' to "Ellen")
    val map4 = map2 + map3
    println(map3) // {D=Daniel, E=Ellen}
    println(map4)
    // {A=Alex, B=Bob, C=Celina, D=Daniel, E=Ellen}
}
```

Uważaj, że nie są dozwolone zduplikowane klucze; więc, gdy dodasz nową wartość z istniejącym kluczem, zastępuje ona starą wartość.

```kotlin
fun main() {
    val map1 = mapOf('A' to "Alex", 'B' to "Bob")
    val map2 = map1 + ('B' to "Barbara")
    println(map1) // {A=Alex, B=Bob}
    println(map2) // {A=Alex, B=Barbara}
}
```

Można również usunąć klucz z mapy za pomocą znaku minus.

```kotlin
fun main() {
    val map1 = mapOf('A' to "Alex", 'B' to "Bob")
    val map2 = map1 - 'B'
    println(map1) // {A=Alex, B=Bob}
    println(map2) // {A=Alex}
}
```

#### Sprawdzanie, czy mapa zawiera klucz

Można sprawdzić, czy mapa zawiera klucz, używając słowa kluczowego `in` lub metody `containsKey`.

```kotlin
fun main() {
    val map = mapOf('A' to "Alex", 'B' to "Bob")
    println('A' in map) // true
    println(map.containsKey('A')) // true
    println('Z' in map) // false
    println(map.containsKey('Z')) // false
}
```

#### Sprawdzanie rozmiaru mapy

Możesz sprawdzić, ile wpisów masz w mapie, używając właściwości `size`.

```kotlin
fun main() {
    val map = mapOf('A' to "Alex", 'B' to "Bob")
    println(map.size) // 2
}
```

#### Iterowanie po mapach

Możesz iterować po mapie używając pętli for. Iterujesz po wpisach, które zawierają właściwości `key` i `value`.

```kotlin
fun main() {
    val map = mapOf('A' to "Alex", 'B' to "Bob")
    for (entry in map) {
        println("${entry.key} is for ${entry.value}")
    }
}
// A is for Alex
// B is for Bob
```

Możesz również zdestrukturyzować każdy wpis na dwie zmienne. Kotlin obsługuje destrukturyzację w pętli for. Spójrz na poniższy przykład.

```kotlin
fun main() {
    val map = mapOf('A' to "Alex", 'B' to "Bob")
    for ((letter, name) in map) {
        println("$letter is for $name")
    }
}
// A is for Alex
// B is for Bob
```

#### Zmienne mapy

Możesz utworzyć zmienną mapę za pomocą `mutableMapOf`. Typem wyniku jest `MutableMap`, który obsługuje metody modyfikujące ten obiekt. Używając go możemy:
* dodać nowe wpisy do mapy za pomocą metody `put` lub nawiasów kwadratowych i przypisania,
* usunąć wpis po kluczu za pomocą metody `remove`.

```kotlin
fun main() {
    val map: MutableMap<Char, String> =
        mutableMapOf('A' to "Alex", 'B' to "Bob")
    map.put('C', "Celina")
    map['D'] = "Daria"
    println(map) // {A=Alex, B=Bob, D=Daria, C=Celina}
    map.remove('B')
    println(map) // {A=Alex, D=Daria, C=Celina}
}
```

### Korzystanie z tablic w praktyce

**Tablica** to bardzo podstawowa struktura danych, która ściśle wiąże się z organizacją pamięci. Pamięć naszego komputera przypomina wielki parking, gdzie każde miejsce ma kolejny numer. Tablica to jak rezerwacja na kilka przyległych miejsc. Dzięki takiej rezerwacji łatwo iterować się po samochodach, które posiadamy. Również łatwo znaleźć samochód o konkretnym indeksie.

Załóżmy, że tablica zaczyna się na pozycji 1024 w pamięci, a my chcemy znaleźć element o indeksie 100 w tablicy. Wiemy również, że każdy element zajmuje 4 pozycje (tablica rezerwuje stałą ilość miejsca dla swoich elementów, która w większości przypadków to rozmiar referencji pamięci). To proste zadanie: nasz element zaczyna się na pozycji 1024 + 100 * 4 = 1424. Dostęp do elementu na określonej pozycji to bardzo prosta i wydajna operacja, co jest dużą zaletą korzystania z tablic.

Korzystanie z tablic bezpośrednio jest trudniejsze niż z innych rodzajów kolekcji. Mają stały rozmiar, ograniczoną liczbę operacji, nie implementują żadnego interfejsu i nie nadpisują metod `toString`, `equals` ani `hashCode`. Jednak tablice są używane przez wiele innych struktur danych "pod maską". Na przykład, gdy używasz `mutableListOf` w Kotlin/JVM, wynikowy obiekt to `ArrayList`, który przechowuje elementy w tablicy. Dlatego wyszukiwanie elementu o indeksie w domyślnej liście jest tak wydajne. `ArrayList` ma więc zalety tablic, ale oferuje znacznie więcej. Tablice mają stały rozmiar, więc nie można dodać więcej elementów niż ich rozmiar pozwala. Gdy dodajesz element do `ArrayList`, a jego wewnętrzna tablica jest już pełna, tworzy większą i wypełnia ją poprzednimi wartościami. Uważamy listy za preferowaną opcję względem tablic, a korzystanie z tablic ograniczamy do wydajnościowo kluczowych części naszych aplikacji[^17_4].

Tablice są również używane przez domyślne `Set` i `Map`, które stosujemy w Kotlin. Oba opierają się na algorytmie tablicy mieszającej, który musi używać tablicy, aby działać wydajnie.

Ni mniej jednak, zobaczmy jak można używać tablic bezpośrednio. Tworzymy tablicę za pomocą funkcji `arrayOf`. Tworzy to instancję klasy `Array` oraz typ `Array<T>`, gdzie `T` to typ elementów. Aby uzyskać element o określonym indeksie, możemy użyć nawiasów kwadratowych lub metody `get`. Aby zmodyfikować element w określonej pozycji, można użyć nawiasów kwadratowych lub metody `set`. Można również uzyskać rozmiar tablicy, używając właściwości `size` lub iterując się po tablicy za pomocą pętli for.

```kotlin
fun main() {
    val arr: Array<String> = arrayOf("A", "B", "C")
    println(arr[0]) // A
    println(arr.get(0)) // A
    println(arr[1]) // B
    arr[1] = "D"
    println(arr[1]) // D
    arr.set(2, "E")
    println(arr[2]) // E
    println(arr.size) // 3
    for (e in arr) {
        print(e)
    }
    // ADE
}
```

Wszystkie powyższe operacje są takie same jak dla `MutableList`, ale to koniec listy podstawowych operacji na tablicach. Tablice nie obsługują równości, więc dwie tablice o takich samych elementach nie są uważane za równe. Innym problemem tablic jest to, że ich metoda `toString`, która służy do przekształcania obiektu w `String`, nie drukuje elementów. Drukuje tylko typ tablicy i hash jej referencji pamięci.

```kotlin
fun main() {
    val arr1 = arrayOf("A", "B", "C")
    val arr2 = arrayOf("A", "B", "C")
    println(arr1 == arr2) // false
    println(arr1) // [Ljava.lang.String;@4f023edb
    println(arr2) // [Ljava.lang.String;@3a71f4dd
}
```

Dla pocieszenia tych, którzy lubią korzystać z tablic, biblioteka standardowa Kotlin oferuje wiele funkcji rozszerzających, które pozwalają na wiele rodzajów transformacji tablic.

![](array_operations.png)

Zauważ, że istnieje metoda `plus`, która pozwala dodać nowy element do tablicy. Tak jak metoda `plus` na liście, nie modyfikuje ona tablicy, ale tworzy nową o większym rozmiarze.

```kotlin
// Implementacja JVM
operator fun <T> Array<T>.plus(element: T): Array<T> {
    val index = size
    val result = java.util.Arrays.copyOf(this, index + 1)
    result[index] = element
    return result
}

fun main() {
    val arr = arrayOf("A", "B", "C")
    println(arr.size) // 3
    val arr2 = arr + "D"
    println(arr.size) // 3
    println(arr2.size) // 4
}
```

Można przekształcić tablicę na listę lub zbiór, używając metod `toList` i `toSet`. Aby przenieść się w drugą stronę, użyj `toTypedArray`.

```kotlin
fun main() {
    val arr1: Array<String> = arrayOf("A", "B", "C")
    val list: List<String> = arr1.toList()
    val arr2: Array<String> = list.toTypedArray()
    val set: Set<String> = arr2.toSet()
    val arr3: Array<String> = set.toTypedArray()
}
```

#### Tablice typów prostych

Niektóre rodzaje wartości w Kotlinie, takie jak `Int` czy `Char`, mogą być reprezentowane w prostszy sposób niż zwykły obiekt. Ta forma jest znana jako typ prosty (primitive) i jest optymalizacją Kotlin, która nie wpływa na używanie wartości; jednak sprawia, że wartości prymitywne zajmują mniej pamięci, a ich użycie jest bardziej wydajne. Problem polega na tym, że typów prostych nie można przechowywać w zwykłych kolekcjach, ale można je przechowywać w specjalnych tablicach. Dla każdej wartości, która ma formę prymitywną, istnieje dedykowany typ tablicy. Oto one:
* `IntArray`, która reprezentuje tablicę prymitywnych wartości `Int`.
* `LongArray`, która reprezentuje tablicę prymitywnych wartości `Long`.
* `DoubleArray`, która reprezentuje tablicę prymitywnych wartości `Double`.
* `FloatArray`, która reprezentuje tablicę prymitywnych wartości `Float`.
* `CharArray`, która reprezentuje tablicę prymitywnych wartości `Char`.
* `BooleanArray`, która reprezentuje tablicę prymitywnych wartości `Boolean`.
* `ShortArray`, która reprezentuje tablicę prymitywnych wartości `Short`.
* `ByteArray`, która reprezentuje tablicę prymitywnych wartości `Byte`.

Każda z tych tablic może być utworzona na dwa sposoby:
* Używając funkcji `xxxOf` i początkowych elementów jako argumentów, gdzie `xxx` to małymi literami napisana nazwa tablicy. Na przykład, aby utworzyć `DoubleArray`, możesz użyć funkcji `doubleArrayOf` z argumentami typu `Double`.
* Przekształcając inny rodzaj kolekcji w tablicę prymitywów, używając metody `toXXX`, gdzie `XXX` to nazwa tablicy. Na przykład, możesz przekształcić `List<Boolean>` na `BooleanArray`, używając metody `toBooleanArray`.

```kotlin
fun main() {
    val doubles: DoubleArray = doubleArrayOf(2.71, 3.14, 9.8)
    val chars: CharArray = charArrayOf('X', 'Y', 'Z')

    val accepts: List<Boolean> = listOf(true, false, true)
    val acceptsArr: BooleanArray = accepts.toBooleanArray()

    val ints: Set<Int> = setOf(2, 4, 8, 10)
    val intsArr: IntArray = ints.toIntArray()
}
```

Tablice prymitywów nie są często używane w większości rzeczywistych projektów. Są one generalnie traktowane jako niskopoziomowe optymalizacje wydajności lub użycia pamięci[^17_5].

#### Parametry Vararg i funkcje tablicowe

Jak wspomniano w rozdziale *Funkcje*, możemy użyć modyfikatora `vararg` dla parametru, aby zaakceptować dowolną liczbę argumentów. Ten modyfikator zamienia parametr na tablicę. Weź pod uwagę funkcję `markdownList` z poniższego przykładu. Jej parametr `lines` ma określony typ `String`, ale ponieważ ma modyfikator `vararg`, rzeczywistym typem `lines` jest `Array<String>`. Dlatego możemy iterować po nim, używając pętli for.

```kotlin
fun markdownList(vararg lines: String): String {
    // typem lines jest Array<String>
    var str = ""
    for ((i, line) in lines.withIndex()) {
        str += " * $line"
        if (i != lines.size) {
            str += "\n"
        }
    }
    return str
}

fun main() {
    print(markdownList("A", "B", "C"))
    // * A
    // * B
    // * C
}
```

Podstawowe funkcje używane do tworzenia kolekcji, takie jak `listOf` czy `setOf`, mogą mieć dowolną liczbę argumentów, ponieważ używają modyfikatora `vararg`.

```kotlin
fun <T> listOf(vararg elements: T): List<T> =
    if (elements.size > 0) elements.asList() else emptyList()

fun <T> setOf(vararg elements: T): Set<T> =
    if (elements.size > 0) elements.toSet() else emptySet()
```

Można również rozłożyć tablicę na argumenty vararg, używając symbolu `*`.

```kotlin
fun main() {
    val arr = arrayOf("B", "C")
    print(markdownList("A", *arr, "D"))
    // * A
    // * B
    // * C
    // * D
}
```

### Podsumowanie

W tym rozdziale omówiliśmy najważniejsze rodzaje kolekcji Kotlin i ich typowe przypadki użycia:

* `List` reprezentuje uporządkowaną kolekcję elementów. Jest to najbardziej podstawowy sposób przechowywania kolekcji elementów.
* `Set` reprezentuje kolekcję unikalnych elementów. Używamy go, gdy chcemy się upewnić, że elementy w naszej kolekcji są unikalne, lub gdy często szukamy określonego elementu.
* `Map` to zestaw par klucz-wartość. Używamy go do przechowywania powiązań między kluczami a wartościami.

Tablice są rzadko używane bezpośrednio w Kotlinie, ponieważ preferujemy korzystanie z innych rodzajów kolekcji.

[^17_0]: Ten temat jest omówiony w *Effective Kotlin*, szczególnie w *Item 27: Use abstraction to protect code against changes*.
[^17_1]: Domyślna wartość jest obliczana przez funkcjonalny argument, taki jak wyrażenie lambda; opiszemy to w kolejnej książce *Functional Kotlin*.
[^17_2]: Jak przedstawiono w *Effective Kotlin*, *Item 1: Limit mutability*.
[^17_3]: Jak przedstawiono w *Effective Kotlin*, *Item 56: Consider using mutable collections*.
[^17_4]: Zobacz *Effective Kotlin*, *Item 55: Consider Arrays with primitives for performance-critical processing*.
[^17_5]: Zobacz *Effective Kotlin*, *Item 55: Consider Arrays with primitives for performance-critical processing*.
[^17_6]: Domyślny zestaw opiera się na algorytmie tablicy mieszającej, który sprawia, że znalezienie elementu o prawidłowo zaimplementowanej funkcji `hashCode` jest naprawdę szybkie. Czas tej operacji nie zależy od liczby elementów w zestawie (ma więc złożoność O(1)). Szczegóły dotyczące działania tego algorytmu tablic mieszających znajdują się w Effective Kotlin *Item 43: Respect the contract of hashCode*.
[^17_7]: Domyślna mapa oparta jest na algorytmie tablicy mieszającej, co sprawia, że znalezienie elementu po kluczu jest naprawdę szybkie (gdy klucz ten ma właściwie zaimplementowaną metodę `hashCode`). Czas tej operacji nie zależy od liczby wpisów w mapie (czyli ma złożoność O(1)). Aby dowiedzieć się więcej o tym, jak działa algorytm tablicy mieszającej, zobacz Efektywny Kotlin *Pozycja 43: Przestrzegaj kontraktu metody hashCode*.
