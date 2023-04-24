## Twój pierwszy program w Kotlin

Pierwszym krokiem w naszej przygodzie z Kotlinem jest napisanie minimalnego programu w tym języku. Tak, słynny program "Hello, World!". Oto jak wygląda on w Kotlinie:

```kotlin
fun main() {
    println("Hello, World")
}
```

Niewiele potrzeba, prawda? Nie potrzebujemy klas (jak w Javie), żadnych obiektów (jak `console` w JavaScripcie) ani warunków (jak w Pythonie). Potrzebujemy funkcji `main` oraz wywołania funkcji `println` z dowolnym tekstem[^02_0].

To jest najbardziej popularny (choć nie jedyny) wariant funkcji "main". Jeśli potrzebujemy argumentów, możemy dołączyć parametr typu `Array<String>`:

```kotlin
fun main(args: Array<String>) {
    println("Hello, World")
}
```

Istnieją także inne formy funkcji `main`:

```kotlin
fun main(vararg args: String) {
    println("Hello, World")
}
```

```kotlin
class Test {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
        }
    }
}
```

```kotlin
suspend fun main() {
    println("Hello, World")
}
```

Chociaż wszystkie te formy są poprawne, skupmy się na prostym `main`, gdyż będzie on najbardziej użyteczny. Użyję go niemal w każdym przykładzie w tej książce i zazwyczaj będą w pełni wykonywalne, jeśli tylko skopiujesz i wkleisz je do IntelliJ lub do Kotlin Playground[^02_2].

```kotlin
fun main() {
    println("Hello, World")
}
```

Wszystko, co musisz zrobić, aby uruchomić funkcję `main` w IntelliJ, to kliknąć zielony trójkąt, który pojawia się po lewej stronie funkcji `main`, znany jako "gutter icon". 

{width: 100%}
![](main_run.png)

### Szablony dynamiczne (Live templates)

Jeśli zdecydujesz się przetestować lub przećwiczyć materiał z tej książki[^02_3], prawdopodobnie będziesz często pisać funkcję `main`. Na pomoc przychodzą nam *szablony dynamiczne* (live templates). Jest to funkcjonalność IntelliJ, która sugeruje użycie szablonu, gdy zaczynasz wpisywać jego nazwę w odpowiednim kontekście. Czyli jeśli zaczniesz wpisywać "main" lub "maina" (dla main z argumentami) w pliku Kotlin, zostanie Ci pokazana sugestia, która oferuje całą funkcję `main`.

{width: 100%}
![](main_template.png)

W większości moich warsztatów używałem tego szablonu setki razy. Kiedy tylko chcę pokazać coś nowego z kodowaniem na żywo, otwieram plik "Playground.kt", zaznaczam całą jego zawartość (Ctrl/command + A), wpisuję "main", potwierdzam szablon dynamiczny klawiszem Enter i mam idealną przestrzeń do pokazania, jak coś działa.

Polecam, abyś przetestował to teraz. Otwórz dowolny projekt Kotlin (najlepiej, jeśli masz dedykowany projekt do zabawy z Kotlinem), utwórz nowy plik (możesz go nazwać "Test" lub "Playground") i dodaj funkcję `main` posługując się szablonem dynamicznym "maina". Użyj funkcji `print` z dowolnym tekstem i uruchom kod przyciskiem "Uruchom".

### Co kryje się pod maską na JVM?

Najważniejszym targetem Kotlina jest JVM (Java Virtual Machine). W JVM każdy element musi znajdować się w klasie. Możesz zatem zastanawiać się, jak to możliwe, że nasza funkcja "main" może być uruchomiona w JVM, skoro nie jest w żadnej klasie. Spróbujmy to wyjaśnić. Po drodze nauczymy się, jak sprawdzić, jak nasz kod w Kotlinie wyglądałby, gdyby był napisany w Javie. Ta możliwość jest niesamowitą pomocą przy nauce Kotlina.

Zacznijmy od otwarcia lub uruchomienia projektu Kotlin w IntelliJ lub Android Studio. Stwórz nowy plik Kotlin o nazwie "Playground". W środku tego pliku użyj szablonu dynamicznego "maina" aby utworzyć główną funkcję z argumentami i dodać `println("Hello, World")` w środku.

{width: 100%}
```kotlin
fun main(args: Array<String>) {
    println("Hello, World")
}
```

Teraz wybierz z zakładek Tools > Kotlin > Show Kotlin Bytecode.

{width: 100%}
![](tools_kotlin_show_bytecode.png)

Po prawej stronie powinno otworzyć się nowe narzędzie. "Show Kotlin Bytecode" pokazuje bytecode JVM generowany z tego pliku.

{width: 100%}
![](show_kotlin_bytecode.png)

To świetne miejsce dla każdego, kto lubi czytać bytecode JVM. Ponieważ nie wszyscy są jak Jake Wharton, w związku z tym większość z nas może uznać przycisk "Decompile" za przydatny. To, co robi, jest dość zabawne. Skompilowaliśmy nasz kod w Kotlinie do bytecode'u JVM, a ten przycisk pozwoli nam go wyświetlić po zdekompilowaniu do Javy. W rezultacie możemy zobaczyć, jak wyglądałby nasz kod, gdyby był napisany w Javie[^02_5].

{width: 100%}
![](hello_world_decompiled.png)

Ten kod ujawnia, że nasza funkcja `main` na JVM staje się statyczną funkcją wewnątrz klasy o nazwie `PlaygroundKt`. Skąd pochodzi ta nazwa? Spróbuj zgadnąć. Tak, domyślnie jest to nazwa pliku z sufiksem "Kt". To samo dzieje się ze wszystkimi innymi funkcjami i właściwościami zdefiniowanymi poza klasami na JVM. Jeśli chcielibyśmy wywołać naszą funkcję `main` w Javie, możemy wywołać `PlaygroundKt.main({})`.

Nazwę `PlaygroundKt` można zmienić, dodając adnotację `@file:JvmName("NewName")` w pierwszej linii pliku źródłowego[^02_6]. Nie wpłynie to na użycie tych elementów z języka Kotlin, ale zmienia nazwę klasy, gdy używana jest w innych językach JVM. Po tej zmianie moglibyśmy wywołać funkcję `main` w Javie jako `NewName.main({})`.

Jeśli masz doświadczenie z Javy, zapamiętaj to narzędzie, ponieważ może pomóc Ci zrozumieć:
- Jak działa kod Kotlin na niskim poziomie.
- Jak działa konkretna funkcja Kotlina "pod maską".
- Jak korzystać w Javie z elementu napisanego w języku Kotlin.

Istnieją propozycje stworzenia podobnego narzędzia, aby pokazywać kod JavaScript generowany z kodu Kotlin, gdy naszym targetem jest Kotlin/JS. Jednak w momencie pisania tej książki najlepszym, co możesz zrobić, jest samodzielne otwieranie wygenerowanych plików.

### Podsumowanie

Nauczyliśmy się korzystać z funkcji `main` i łatwo je tworzyć przy pomocy szablonów dynamicznych. Dowiedzieliśmy się też jak sprawdzić, jak nasz kod Kotlin wyglądałby, gdyby był napisany w Javie. To chyba niezły początek naszej przygody, więc bez zbędnych ceregieli chodźmy dalej.

[^02_0]: Funkcja `println` jest niejawnie importowana z pakietu biblioteki standardowej `kotlin.io`.
[^02_2]: Niektóre rozdziały tej książki można również znaleźć online na stronie Kt. Academy. Przykłady w nich zawarte można uruchomić i modyfikować dzięki funkcji Kotlin Playground.
[^02_3]: Cieszę się, gdy ludzie próbują podważyć to, czego nauczałem. Bądź sceptyczny i weryfikuj to, czego się nauczyłeś; to świetny sposób na naukę czegoś nowego i pogłębienie zrozumienia.
[^02_5]: To nie zawsze działa, ponieważ dekompilator nie jest doskonały, ale i tak jest bardzo pomocny.
[^02_6]: Więcej na ten temat w książce *Zaawansowany Kotlin*, rozdział *Interoperacyjność Kotlin i Java*.
