## Słowniczek

W książce użyłem następujących tłumaczeń pojęć z języka angielskiego:

* Akcesor — ang. *accessor*, czyli getter lub setter. Zobacz rozdział *Klasy*.
* Argument typu — ang. *type argument*, czyli na przykład `Int` w `List<Int>`. Zobacz rozdział *Typy generyczne*.
* Assersja not—null — ang. *not-null assertion*, czyli `!!`. Zobacz rozdział *Nullowalność*. 
* Bajtkod — ang. *bytecode*, czyli efekt kompilacji kodu Kotlin na przykład do JVM.
* companion obiekt — ang. *companion object*, czyli specjalny type deklaracji obiektu, do którego można się odnosić poprzez nazwę klasy, w której się znajduje. Zobacz rozdział *Obiekty*. 
* Context receiver — ang. *context receiver*, czyli specjalna funkcjonalność języka, którą opisałem w książce *Funkcyjny Kotlin*. 
* Czas wykonania — ang. *runtime*, czyli czas, kiedy program jest wykonywany. 
* Deklaracja obiektu — ang. *object declaration*, czyli specjalna funkcjonalność Kotlina tworząca obiekt z pojedynczą instancją, implementacja wzorca singleton, na przykład `object Empty`. Zobacz rozdział *Obiekty*.
* Destrukturyzacja — ang. *destructuring*, czyli na przykład `val (e1, e2) = list`. Zobacz rozdział *Data klasy*.
* Enum — ang. *enum class*, czyli na przykład `enum class Letter { A, B, C }`. Zobacz rozdział *Enumy*.
* Element — ang. *element*, czyli klasa, funckcja, interfejs, deklaracja obiektu.
* Funkcja anonimowa — ang. *anonymous function*, czyli na przykład `val f = fun() { println("A") }`. Funkcje anonimowe są opisane w książce *Funkcyjny Kotlin*.
* Funkcja infixowa — ang. *infix function*, czyli funkcje z modyfikatorem `infix` pozwalające na umieszczenie wywołania takiej funkcji pomiędzy argumentem i receiverem bez żadnych nawiasów czy kropek. Przykładem jest `1 to "one"` albo `0x01 or 0.10`. Zobacz rozdział *Funkcje*.
* Funkcja klasy — ang. *member function*, czyli funkcja zdefiniowana w ciele klasy, a więc będącej metodą. Przyjąłem takie tłumaczenie ze względu ze względu na brak lepszego tłumaczenia. Na przykład `a` w `class A { fun a() {} }`. Zobacz rozdział *Klasy*.
* Funkcje rozszerzające — ang. *extension functions*, czyli specjalny typ funkcji w Kotlinie pozwalający definiować funkcje, wywoływane na obiektach określonego typu, podobnie jak funkcje klasy. Przykładem jest `fun String.a() {}`. Zobacz rozdział *Rozszerzenia*.
* Getter — ang. *getter*, czyli funkcja zwracająca wartość właściwości. Zobacz rozdział *Klasy*.
* Instrukcja — ang. *statement*, czyli najmniejszy samodzielny element imperatywnego języka programowania. Różne instrukcje znajdują się typowo w różnych liniach albo są oddzielone średnikami. Na przykład `val a = 1` albo `print("A")`.
* Instrukcja warunkowa — ang. *conditional statement. Instrukcja decydująca*, które działanie wykonać na podstawie wyniku działania warunków (wyrażeń zwracających wartość logiczną). 
* Interpolacja stringa — ang. *string interpolation*, czyli specjalny zapis pozwalający na wstawienie wartości wyrażenia do stringa. Na przykład `"A ${1 + 1}"`. Zobacz rozdział *Podstawowe typy, ich literały i operacje*.
* Klasa data — ang. *data class*, czyli klasy z modyfikatorem `data` reprezentujące zbiory danych. Zobacz rozdział *Data klasy*.
* Klasa sealed — ang. *sealed class*, czyli klasy z modyfikatorem `sealed` reprezentujące zamkniętą hierarchię typów. Zobacz rozdział *Sealed klasy i interfejsy*.
* Konstruktor główny — ang. *primary constructor*, czyli specjalny rodzaj konstruktora definiowany za nazwą klasy, na przykład `class Person(val name: String)`. Zobacz rozdział *Klasy*.
* Lateinit — ang. *lateinit*, czyli mogyfikator sprawiający, że nie musimy inicjalizować właściwości przy jej definicji. Zobacz rozdział *Nullowalność*.
* Literał funkcyjny — ang. *function literal*, specjalny zapis służący do zdefiniowania funkcji jako wartości, czyli w języku Kotlin albo funkcja lambda albo funkcja anonimowa. Więcej na ten temat w książce *Funkcyjny Kotlin*.
* Literał stringa — ang. *string literal*, czyli specjalny zapis służący do definiowania stringa, na przykład `"Hello"`. Zobacz rozdział *Podstawowe typy, ich literały i operacje*.
* Lista — ang. *list*, czyli konkretny typ kolekcji o typie `List`. Zobacz rozdział *Kolekcje*.
* Lista modyfikowalna — ang. *mutable list*, czyli konkretny typ kolekcji o typie `MutableList`.
* Łapanie wyjątku — ang. *catch exception*, czyli proces zatrzymywania propagacji wyjątku przy pomocy bloku `try-catch`. Zobacz rozdział *Wyjątki*.
* Parametr typu — ang. type parameter*, czyli struktura określająca, że spodziewany jest typ generyczny, do którego odnosić się będziemy poprzez nazwę parametry typu. Na przykład `T` w definicji `class A<T>` albo `T` w definicji `fun <T> f()`. Zobacz rozdział *Typy generyczne*.
* Przeciążanie — ang. *overloading*, czyli możliwość definiowania wielu funkcji o tej samej nazwie, ale różnych parametrach. Zobacz rozdział *Funkcje*.
* Przypisanie z operatorem arytmetycznym — ang. *augmented assignment*, czyli specjalny zapis służący do przypisania do zmiennej wartości będącej wynikiem operacji arytmetycznej uwzględniającej jej poprzednią wartość, na przykład `a += 1`. Zobacz rozdział *Operatory*.
* Programowanie funkcyjne — ang. *functional programming*, czyli paradygmat programowania w dużym stopniu wykorzystujący funkcjonalności opisane w książce *Funkcyjny Kotlin*.
* Projekcja gwiazdkowa — ang. *star projection*, czyli specjalny zapis służący do definiowania typu generycznego, który jest nieznany. Zobacz rozdział *Typy generyczne*.
* Referencja — ang. *reference*, odniesienie do elementu programu, na przykład zmiennej, funkcji, klasy, obiektu. Przykładem może być referencja do klasy `User::class` albo referencja do funkcji `::println`. Więcej na ten temat w książce *Funkcyjny Kotlin*.
* Rozszerzenia — ang. *extension*, czyli specjalna funkcjonalność języka Kotlin, która pozwala na definiowanie funkcji i właściwości, które są wykonywane na obiektach określonego typu, podobnie jak funkcje i właściwości klasy. Zobacz rozdział *Rozszerzenia*.
* Rzucanie wyjątkiem — ang. *throw exception*, czyli proces rozpoczynany przy użyciu słowa kluczowego `throw`, kończący obecnie wykonywaną funkcję i rzucający wyjątek z miejsca jej użycia aż wyjątek zostanie złapany lub aż program się zakończy. Zobacz rozdział *Wyjątki*.
* Setter — ang. *setter*, czyli funkcja ustawiająca wartość właściwości. Zobacz rozdział *Klasy*.
* Smart cast — ang. *smart cast*, czyli specjalna funkcjonalność języka Kotlin, która po sprawdzeniu typu zmiennej pozwala na użycie jej jako zmiennej tego typu. Zobacz rozdział *Instrukcje warunkowe*.
* Stacktrace — ang. *stacktrace*, czyli lista funkcji, które były wykonywane, w czasie wystąpienia błędu. Pomaga odnaleźć źródło problemu. Zobacz rozdział *Wyjątki*.
* String — ang. *string*, czyli typ wartości o typie `String`, nazywany też łańcuchem znaków. Zobacz rozdział *Podstawowe typy, ich literały i operacje*.
* Szablon stringa — ang. *string template*, czyli specjalny zapis definiujący stringa, ale również zawierający wstawione wartości zmiennych i wyrażeń. Na przykład `"Value: $i and the next one ${i+1}`. Zobacz rozdział *Podstawowe typy, ich literały i operacje*.
* Target — ang. *target*, czyli platforma na którą kompilowany jest Kotlin, na przykład JVM (Kotlin/JVM) czy JavaScript (Kotlin/JS). Zobacz rozdział *Czym jest Kotlin?*.
* Typ — ang. *type*, czyli określenie jakiego rodzaju wartości może przyjmować zmienna lub może zwrócić funkcja. Na przykład `Int` albo `String`. Koncepcja typu jest szczegółowo opisana w rozdziale *Piękno systemu typów w Kotlinie*.
* Typ funkcyjny — ang. *function type*, czyli typ opisujący funkcję, na przykład `(Int) -> String`. Więcej na ten temat w książce *Funkcyjny Kotlin*.
* Typ nienullowalny — ang. *not-nullable type*, czyli taki, który nie może przyjmować wartości `null`, na przykład `Int` albo `String`. Zobacz rozdział *Nullowalność*.
* Typ nullowalny — ang. *nullable type*, czyli taki, który może przyjmować wartość `null`, na przykład `Int?` albo `String?`. Zobacz rozdział *Nullowalność*.
* Typ wnioskowany — ang. *inferred type*, czyli określony przez kompilator na podstawie kontekstu, na przykład `val x = 1` ma typ wnioskowany `Int`.
* Type erasure — ang. *type erasure*, czyli proces, który występuje na wielu platformach, na przykład JVM, który powoduje, że typy generyczne nie są dostępne w czasie wykonania. Zobacz rozdział *Typy generyczne*.
* Właściwość — ang. *property*, czyli zmienna zdefiniowana w ciele klasy, a więc zawierająca getter i/lub setter. Zobacz rozdział *Klasy*.
* Właściwości rozszerzające — ang. *extension property*, czyli specjalna struktura definiująca właściwość dla danego typu, ale nie w ciele klasy, a w zewnętrznym miejscu. Na przykład `age` w `val User.age get() = yearsFrom(birthday)`. Zobacz rozdział *Rozszerzenia*.
* Wyrażenie — ang. *expression*, czyli fragment kodu, który zwraca wartość, na przykład `1 + 1`. Wiele wyrażeń składa się z innych wyrażeń, na przykład wyrażenie `user.name + user.surname` składa się z wyrażeń `user.name` i `user.surname`. 
* Wyrażenie if — ang. *if-expression*, czyli typ wyrażenia warunkowego używający słowa kluczowego `if`, na przykład `if (x > 0) 1 else -1`. Zobacz rozdział *Instrukcje warunkowe*.
* Wyrażenie lambda — ang. *lambda expression*, czyli specjalny zapis do tworzenia wartości reprezentujących funkcje, na przykład `{ x -> x + 1 }`. Więcej na ten temat w książce *Funkcyjny Kotlin*.
* Wyrażenie try-catch - ang. try-catch expression, czyli wyrażenie służące do łapania wyjątków, używające słów kluczowych `try` i `catch`, na przykład `try { ... } catch (e: Exception) { ... }`. Zobacz rozdział *Wyjątki*.
* Wyrażenie when — ang. *when-expression*, czyli wyrażenie warunkowe służące do wyboru jednej z wielu możliwych wartości, używające słowa kluczowego `when`. Zobacz rozdział *Instrukcje warunkowe*.
* Wywołanie bezpieczne — ang. *safe call*, czyli specjalny sposób wywołania funkcji lub właściwości na obiekcie nullowalnym, który zwraca `null` jeśli obiekt jest `null`, na przykład `x?.foo()`. Zobacz rozdział *Nullowalność*.
* Wyrażenie tworzące obiekt — ang. *object expression*, czyli specjalny zapis do tworzenia obiektów, alternatywa dla klas anonimowych w Javie, na przykład `object : A() {}`. Zobacz rozdział *Obiekty*.
* Zakres — ang. *range*, czyli obiekt reprezentujący zakres wartości, na przykład `1..10`, `10 downTo 1` czy `0 until 5`. Zobacz rozdział *Supermoce pętli for*.
* Zbiór — ang. *set*, czyli konkretny typ kolekcji o typie `Set`. Zobacz rozdział *Kolekcje*.


