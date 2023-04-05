## Twój pierwszy program w Kotlin

Pierwszym krokiem w naszej przygodzie z Kotline jest napisanie minimalnego programu w tym języku. Tak, to słynny program "Hello, World!". Oto jak wygląda w Kotlin:

```kotlin
fun main() {
    println("Hello, World")
}
```

To jest minimalistyczne, prawda? Nie potrzebujemy klas (jak w Javie), żadnych obiektów (jak `console` w JavaScripcie) ani warunków (jak w Pythonie, gdy zaczynamy kod w IDE). Potrzebujemy funkcji `main` oraz wywołania funkcji `println` z jakimś tekstem[^02_0].

To jest najbardziej popularna (choć nie jedyna) wariant funkcji "main". Jeśli potrzebujemy argumentów, możemy dołączyć parametr typu `Array<String>`:

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

Chociaż wszystkie te formy są poprawne, skupmy się na prostym `main`, gdyż będzie on najbardziej użyteczny. Użyję go niemal w każdym przykładzie w tej książce. Takie przykłady są zwykle w pełni wykonywalne, jeśli tylko skopiujesz i wkleisz je do IntelliJ lub do Kotlin Playground[^02_2].

```kotlin
fun main() {
    println("Hello, World")
}
```

Wszystko, co musisz zrobić, aby uruchomić funkcję `main` w IntelliJ, to kliknąć zielony trójkąt, który pojawia się po lewej stronie funkcji `main`; nazywa się to "ikona marginesu" albo przyciskiem "Uruchom".

{width: 100%}
![](main_run.png)

### Szablony dynamiczne (Live templates)

Jeśli zdecydujesz się przetestować lub przećwiczyć materiał z tej książki[^02_3], prawdopodobnie będziesz często pisać funkcję `main`. Na pomoc przychodzą nam *szablony dynamiczne* (live templates). Jest to funkcja IntelliJ, która sugeruje użycie szablonu, gdy zaczynasz wpisywać jego nazwę w odpowiednim kontekście. Czyli jeśli zaczniesz wpisywać "main" lub "maina" (dla main z argumentami) w pliku Kotlin, zostanie Ci pokazana sugestia, która oferuje całą funkcję `main`.

{width: 100%}
![](main_template.png)

W większości moich warsztatów używałem tego szablonu setki razy. Kiedy tylko chcę pokazać coś nowego z kodowaniem na żywo, otwieram plik "Playground", zaznaczam całą jego zawartość (Ctrl/command + A), wpisuję "main", potwierdzam szablon dynamiczny klawiszem Enter, i mam idealną przestrzeń do pokazania, jak działa Kotlin.

Polecam również, abyś przetestował to teraz. Otwórz dowolny projekt Kotlin (najlepiej, jeśli masz dedykowany projekt do zabawy z Kotline), utwórz nowy plik (możesz go nazwać "Test" lub "Playground") i utwórz funkcję `main` z szablonem dynamicznym "maina". Użyj funkcji `print` z jakimś tekstem i uruchom kod przyciskiem "Uruchom".

### Co kryje się pod maską na JVM?

Najważniejszym celem Kotlin jest JVM (Java Virtual Machine). Na JVM każdy element musi znajdować się w klasie. Możesz zatem zastanawiać się, jak możliwe jest, że nasza główna funkcja może być uruchomiona tam, jeśli nie jest w żadnej klasie. Spróbujmy to wyjaśnić. Po drodze nauczymy się, jak nasz kod Kotlin wyglądałby, gdyby był napisany w Java. Jest to najpotężniejsze narzędzie dla programistów Java do nauki, jak działa Kotlin.

Zacznijmy od otwarcia lub uruchomienia projektu Kotlin w IntelliJ lub Android Studio. Stwórz nowy plik Kotlin o nazwie "Playground". W środku tego pliku użyj szablonu dynamicznego "maina", aby utworzyć główną funkcję z argumentami i dodać `println("Hello, World")` w środku.

{width: 100%}
```kotlin
fun main(args: Array<String>) {
    println("Hello, World")
}
```

Teraz wybierz z zakładek Narzędzia (Tools) > Kotlin > Pokaż bajtkod Kotlin (Show Kotlin Bytecode).

{width: 100%}
![](tools_kotlin_show_bytecode.png)

Po prawej stronie powinno otworzyć się nowe narzędzie. "Pokaż bajtkod Kotlin" pokazuje bajtkod JVM generowany z tego pliku.

{width: 100%}
![](show_kotlin_bytecode.png)

To świetne miejsce dla każdego, kto lubi czytać bajtkod JVM. Ponieważ nie wszyscy są Jake Wharton, większość z nas może uznać przycisk "Dekompiluj" (Decompile) za przydatny. To, co robi, jest dość zabawne. Właśnie skompilowaliśmy nasz kod Kotlin do bajtkodu JVM, a ten przycisk dekompiluje ten bajtkod na Javę. W rezultacie możemy zobaczyć, jak wyglądałby nasz kod, gdyby był napisany w Javie[^02_5].

{width: 100%}
![](hello_world_decompiled.png)

Ten kod ujawnia, że nasza funkcja `main` na JVM staje się statyczną funkcją wewnątrz klasy o nazwie `PlaygroundKt`. Skąd pochodzi ta nazwa? Spróbuj zgadnąć. Tak, domyślnie jest to nazwa pliku z sufiksem "Kt". To samo dzieje się ze wszystkimi innymi funkcjami i właściwościami zdefiniowanymi poza klasami na JVM.

Nazwę `PlaygroundKt` można zmienić, dodając adnotację `@file:JvmName("NewName")` na górze pliku[^02_6]. Jednak nie zmienia to sposobu, w jaki używane są elementy zdefiniowane w tym pliku w Kotlin. Wpływa to tylko na to, jak będziemy korzystać z takich funkcji w Javie. Jeśli chcielibyśmy wywołać naszą funkcję `main` z kodu Java, możemy wywołać `PlaygroundKt.main({})`.

Jeśli masz doświadczenie z Java, zapamiętaj to narzędzie, ponieważ może pomóc Ci zrozumieć:
- Jak działa kod Kotlin na niskim poziomie.
- Jak działa pewna funkcja Kotlin "pod maską".
- Jak korzystać z elementu Kotlin w Javie.

Istnieją propozycje stworzenia podobnego narzędzia, aby pokazywać JavaScript generowany z kodu Kotlin, gdy naszym celem jest Kotlin/JS. Jednak w momencie pisania tej książki najlepszym, co możesz zrobić, jest samodzielne otwieranie wygenerowanych plików.

### Podsumowanie

Nauczyliśmy się korzystać z funkcji `main` i tworzyć je łatwo za pomocą szablonów dynamicznych. Dowiedzieliśmy się też, jak sprawdzić, jak nasz kod Kotlin wyglądałby, gdyby był napisany w Javie. Dla mnie wydaje się, że mamy całkiem fajne narzędzia do rozpoczęcia naszej przygody. Więc bez zbędnych ceregieli, zaczynajmy.

[^02_0]: Funkcja `println` jest niejawnie importowana z pakietu biblioteki standardowej `kotlin.io`.
[^02_2]: Można również znaleźć niektóre rozdziały tej książki online na stronie Kt. Academy. Przykłady te można uruchomić i modyfikować dzięki funkcji Kotlin Playground.
[^02_3]: Cieszę się, gdy ludzie próbują podważyć to, czego nauczałem. Bądź sceptyczny i weryfikuj to, czego się nauczyłeś; to świetny sposób na naukę czegoś nowego i pogłębienie zrozumienia.
[^02_5]: To nie zawsze działa, ponieważ dekompilator nie jest doskonały, ale i tak jest bardzo pomocny.
[^02_6]: Więcej na ten temat w książce *Zaawansowany Kotlin*, rozdział *Interoperacyjność Kotlin i Java*.
