## Zmienne

Aby zadeklarować zmienną w Kotlinie, zaczynamy od słowa kluczowego `val` lub `var`, następnie podajemy nazwę zmiennej, znak równości i wartość początkową.
* Słowo kluczowe `var` (od słowa "variable" czyli "zmienna") definiuje *zmienną do odczytu i zapisu* i jest używane do definiowania zmiennych, do których zawsze możesz przypisać inną wartość. 
* Słowo kluczowe `val` (od słowa "value" czyli "wartość") definiuje *zmienne tylko do odczytu* i jest używane do definiowania wartości, któych wartości określa się tylko raz (choć jeśli wartość ta jest mutowalna, to może się ona wciąż zmienić). 

```kotlin
fun main() {
   val a = 10
   var b = "ABC"
   println(a) // 10
   println(b) // ABC
   // a = 12 - ponowne przypisanie nie jest możliwe, 
   // ponieważ a jest tylko do odczytu!
   b = "CDE"
   println(b) // CDE
}
```

Zmienne możemy nazywać za pomocą wielkich i małych liter, podkreślenia `_` i cyfr (ale cyfry nie są dozwolone na pierwszej pozycji). Zmienne nazywamy zgodnie z konwencją camelCase; oznacza to, że nazwa zmiennej zaczyna się od małej litery, a następnie zamiast używać spacji, każde kolejne słowo zaczyna się wielką literą.

{width: 50%}
![W Kotlin nazywamy zmienne używając camelCase.](camelCase.png)

Zmienne nie muszą jawnie określać swojego typu, ale to nie oznacza, że zmienne nie mają typów. Kotlin to język statycznie typowany, więc każda zmienna musi mieć określony typ. Chodzi o to, że Kotlin jest na tyle inteligentny, żeby wywnioskować typ na podstawie przypisanej wartości. `10` jest typu `Int`, więc typ zmiennej `a` w powyższym przykładzie to `Int`. `"ABC"` jest typu `String`, więc typ zmiennej `b` to `String`.

{width: 60%}
![](104_inference.png)

Możemy również jawnie określić typ zmiennej, używając dwukropka i nazwy typu **po** nazwie zmiennej.

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

Podczas inicjalizacji zmiennej powinniśmy przypisać jej wartość. Jak w poniższym przykładzie, definicję i inicjalizację zmiennej można oddzielić, jeśli Kotlin może być pewien, że zmienna nie zostanie użyta przed przypisaniem wartości. Jednak gdy nie jest to konieczne, sugeruję unikać tej praktyki.

```kotlin
fun main() {
    val a: Int
    a = 10
    println(a) // 10
}
```

Możemy założyć, że zmienna powinna być zwykle zainicjalizowana przy użyciu znaku równości po jej deklaracji (jak w `val a = 10`). Co więc może znajdować się po prawej stronie znaku równości? Może to być dowolne wyrażenie, tj. fragment kodu zwracający wartość. Oto najczęstsze rodzaje wyrażeń w języku Kotlin:
* wartość dosłowna, jak `1` czy `"ABC"`[^03_2],
* instrukcja warunkowa użyta jako wyrażenie, jak wyrażenie `if`, wyrażenie `when` czy wyrażenie `try-catch`[^03_3].
* wywołanie konstruktora[^03_4],
* wywołanie funkcji[^03_5],
* wyrażenie obiektu lub użycie deklaracji obiektu[^03_6],
* literał funkcyjny, jak wyrażenie lambda, funkcja anonimowa lub referencja do funkcji[^03_7],
* referencja do elementu[^03_8].

Mamy wiele do omówienia, więc zacznijmy od wartości dosłownych.

[^03_2]: Wartości dosłowne oraz typy podstawowe omówione są w kolejnym rozdziale.
[^03_3]: Instrukcje warunkowe omówione są w rozdziale *Instrukcje warunkowe*.
[^03_4]: Konstruktory omówione są w rozdziale *Klasy*.
[^03_5]: Wszystkie funkcje w Kotlinie zwracają jakąś wartość, więc wszystkie mogą być użyte po prawej stronie przypisania zmiennej. Funkcje omówione są w rozdziale *Funkcje*.
[^03_6]: Wyrażenia obiektu i deklaracja obiektu omówione są w rozdziale *Obiekty*.
[^03_7]: Literały funkcyjne omówione są w książce *Funkcyjny Kotlin* w rozdziałach *Funkcje anonimowe*, *Wyrażenia lambda* i *Referencje do funkcji*.
[^03_8]: Rodzaje referencji do elementów omówione są w książce *Zaawansowany Kotlin* w rozdziale *Refleksja*.