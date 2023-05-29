## Słowniczek

W książce użyłem następujących tłumaczeń pojęć z języka angielskiego:

* akcesor - ang. accessor, czyli getter lub setter.
* argument typu - ang. type argument, czyli na przykład `Int` w `List<Int>`.
* assersja not-null - ang. not-null assertion, czyli `!!`.
* bajtkod - ang. bytecode, czyli efekt kompilacji kodu Kotlin na przykład do JVM.
* companion obiekt - ang. companion object.
* context receiver - ang. context receiver, czyli specjalna funkcjonalność języka, którą opisałem w książce *Funkcyjny Kotlin*. 
* czas wykonania - ang. runtime, czyli czas, w którym program jest uruchomiony.
* deklaracja obiektu - ang. object declaration, czyli specjalna funkcjonalność Kotlina tworząca obiekt z pojedynczą instancją, implementacja wzorca singleton, na przykład `object Empty`.
* destrukturyzacja - ang. destructuring, czyli na przykład `val (e1, e2) = list`.
* enum - ang. enum class, czyli na przykład `enum class Letter { A, B, C }`.
* funkcja anonimowa - ang. anonymous function, czyli na przykład `val f = fun() { println("A") }`. Funkcje anonimowe są opisane w książce *Funkcyjny Kotlin*.
* funkcja infixowa - ang. infix function, czyli funkcje z modyfikatorem `infix` pozwalające na umieszczenie wywołania takiej funkcji pomiędzy argumentem i receiverem bez żadnych nawiasów czy kropek. Przykładem jest `1 to "one"` albo `0x01 or 0.10`.
* funkcja klasy - ang. member function, czyli funkcja zdefiniowana w ciele klasy, a więc będącej metodą. Uwaga, przyjąłem takie tłumaczenie ze względu na brak lepszego tłumaczenia, ale nie powinno być ono mylone z pojęciem "
* funkcje rozszerzające - ang. extension functions.
* getter - ang. getter, czyli funkcja zwracająca wartość właściwości. 
* instrukcja - ang. statement. 
* instrukcja warunkowa - ang. conditional statement.
* interpolacja stringa - ang. string interpolation.
* klasa data - ang. data class.
* klasa sealed - ang. sealed class.
* konstruktor główny - ang. primary constructor.
* lateinit - ang. lateinit.
* literał funkcyjny - ang. function literal.
* literał stringa - ang. string literal.
* lista - ang. list, czyli konkretny typ kolekcji o typie `List`.
* lista modyfikowalna - ang. mutable list, czyli konkretny typ kolekcji o typie `MutableList`.
* łapanie wyjątku - ang. catch exception.
* parametr typu - ang. type parameter.
* przeciążanie - ang. overloading.
* przypisanie z operatorem arytmetycznym - ang. augmented assignment.
* programowanie funkcyjne - ang. functional programming.
* projekcja gwiazdkowa - ang. star projection.
* referencja - ang. reference.
* rozszerzenia - ang. extension.
* rzucanie wyjątkiem - ang. throw exception.
* setter - ang. setter, czyli funkcja ustawiająca wartość właściwości.
* smart cast - ang. smart cast.
* stacktrace - ang. stacktrace.
* string - ang. string, czyli typ wartości o typie `String`, nazywany też łańcuchem znaków.
* szablon stringa - ang. string template, czyli specjalny zapis definiujący stringa, ale również zawierający wstawione wartości zmiennych i wyrażeń. Na przykład `"Value: $i and the next one ${i+1}`.
* target - ang. target, czyli platforma na którą kompilowany jest Kotlin, na przykład JVM (Kotlin/JVM) czy JavaScript (Kotlin/JS).
* typ - ang. type.
* typ funkcyjny - ang. function type, czyli typ opisujący funkcję, na przykład `(Int) -> String`.
* typ nienullowalny - ang. not-nullable type, czyli taki, który nie może przyjmować wartości `null`, na przykład `Int` albo `String`.
* typ nullowalny - ang. nullable type, czyli taki, który może przyjmować wartość `null`, na przykład `Int?` albo `String?`.
* typ wnioskowany - ang. inferred type, czyli określony przez kompilator na podstawie kontekstu, na przykład `val x = 1` ma typ wnioskowany `Int`.
* type erasure - ang. type erasure, czyli proces, który występuje na wielu platformach, na przykład JVM, który powoduje, że typy generyczne nie są dostępne w czasie wykonania.
* właściwość - ang. property, czyli zmienna zdefiniowana w ciele klasy, a więc zawierająca getter i/lub setter.
* właściwości rozszerzające - ang. extension property.
* wyrażenie - ang. expression, czyli fragment kodu, który zwraca wartość, na przykład `1 + 1`.
* wyrażenie if - ang. if-expression.
* wyrażenie lambda - ang. lambda expression.
* wyrażenie try-catch - ang. try-catch expression.
* wyrażenie when - ang. when-expression.
* wywołanie - ang. call.
* wywołanie bezpieczne - ang. safe call.
* zakres - ang. range, czyli obiekt reprezentujący zakres wartości, na przykład `1..10`, `10 downTo 1` czy `0 until 5`.
* zbiór - ang. set, czyli konkretny typ kolekcji o typie `Set`. 
* wyrażenie tworzące obiekt - ang. object expression, czyli specjalny zapis do tworzenia obiektów, alternatywa dla klas anonimowych w Javie, na przykład `object : A() {}`.
