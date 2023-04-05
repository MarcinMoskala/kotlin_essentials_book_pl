## Zmienne

Aby zadeklarować zmienną w Kotlin, zaczynamy od słowa kluczowego `val` lub `var`, następnie nazwa zmiennej, znak równości i wartość początkowa.
* Słowo kluczowe `var` (które oznacza "zmienna") reprezentuje *zmienne czytelno-zapisowe* i jest używane do definiowania zmiennych, których wartości można przypisać ponownie po zainicjowaniu. Oznacza to, że jeśli używasz `var`, zawsze możesz przypisać nową wartość tej zmiennej.
* Słowo kluczowe `val` (które oznacza "wartość") reprezentuje *zmienne tylko do odczytu* i jest używane do definiowania wartości, których nie można przypisać ponownie. Oznacza to, że jeśli używasz `val`, nie możesz przypisać nowej wartości tej zmiennej po jej zainicjowaniu.

```kotlin
fun main() {
   val a = 10
   var b = "ABC"
   println(a) // 10
   println(b) // ABC
   // a = 12 nie jest możliwe, ponieważ a jest tylko do odczytu!
   b = "CDE"
   println(b) // CDE
}
```

Zmienne możemy nazywać za pomocą znaków, podkreślenia `_` i cyfr (ale cyfry nie są dozwolone na pierwszej pozycji). Zgodnie z konwencją, zmienne nazywamy według konwencji camelCase; oznacza to, że nazwa zmiennej zaczyna się od małej litery, a następnie (zamiast używać spacji) każde kolejne słowo zaczyna się od wielkiej litery.

{width: 50%}
![W Kotlin nazywamy zmienne używając camelCase.](camelCase.png)

Zmienne nie muszą jawnie określać swojego typu, ale to nie oznacza, że zmienne nie mają typów. Kotlin to język statycznie typowany, więc każda zmienna musi mieć określony typ. Chodzi o to, że Kotlin jest na tyle inteligentny, żeby wywnioskować typ na podstawie przypisanej wartości. `10` jest typu `Int`, więc typ zmiennej `a` w powyższym przykładzie to `Int`. `"ABC"` jest typu `String`, więc typ zmiennej `b` to `String`.

{width: 60%}
![](104_inference.png)

Możemy również jawnie określić typ zmiennej, używając dwukropka i typu **po** nazwie zmiennej.

```kotlin
fun main() {
   val a: Int = 10
   var b: String = "ABC"
   println(a) // 10
   println(b) // ABC
   b = "CDE"
   println(b) // CDE
}
```

Podczas inicjalizacji zmiennej powinniśmy przypisać jej wartość. Jak w poniższym przykładzie, definicję i inicjalizację zmiennej można oddzielić, jeśli Kotlin może być pewien, że zmienna nie zostanie użyta przed przypisaniem wartości. Sugeruję unikanie tej praktyki, gdy nie jest to konieczne.

```kotlin
fun main() {
    val a: Int
    a = 10
    println(a) // 10
}
```

Możemy założyć, że zmienna powinna być zwykle inicjalizowana, używając znaku równości po jej deklaracji (jak w `val a = 10`). Więc co może znajdować się po prawej stronie przypisania? Może to być dowolne wyrażenie, tj. fragment kodu zwracający wartość. Oto najczęstsze rodzaje wyrażeń w Kotlin:
* dosłowny typ podstawowy, jak `1` czy `"ABC"`[^03_2],
* instrukcja warunkowa użyta jako wyrażenie, jak if-expression, when-expression czy try-catch expression[^03_3].
* wywołanie konstruktora[^03_4],
* wywołanie funkcji[^03_5],
* wyrażenie obiektu lub deklaracja obiektu[^03_6],
* dosłowna funkcja, jak wyrażenie lambda, funkcja anonimowa lub odwołanie do funkcji[^03_7],
* odwołanie do elementu[^03_8].

Mamy wiele do omówienia, więc zacznijmy od dosłownych typów podstawowych.

[^03_2]: Typy podstawowe omówione są w kolejnym rozdziale.
[^03_3]: Instrukcje warunkowe omówione są w rozdziale *Instrukcje warunkowe*.
[^03_4]: Konstruktory omówione są w rozdziale *Klasy*
[^03_5]: Wszystkie funkcje w Kotlin deklarują typ wyniku, więc wszystkie mogą być użyte po prawej stronie przypisania zmiennej. Funkcje omówione są w rozdziale *Funkcje*.
[^03_6]: Wyrażenia obiektów i deklaracje obiektów omówione są w rozdziale *Obiekty*
[^03_7]: Wszystkie typy dosłownych funkcji omówione są w książce *Functional Kotlin* w rozdziałach *Funkcje anonimowe*, *Wyrażenia lambda* i *Odwołania do funkcji*.
[^03_8]: Wszystkie różne rodzaje odwołań do elementów omówione są w książce *Advanced Kotlin* w rozdziale *Refleksja*.