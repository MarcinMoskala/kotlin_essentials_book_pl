## Czym jest Kotlin?

Kotlin to otwartoźródłowy (open source), wieloplatformowy, wieloparadygmatyczny, statycznie typowany, ogólnego przeznaczenia język programowania. Ale co to wszystko oznacza?
* Otwartoźródłowy oznacza, że źródła kompilatora Kotlin są swobodnie dostępne do modyfikacji i redystrybucji. Kotlin jest tworzony głównie przez JetBrains, ale istnieje również Kotlin Foundation, która promuje i rozwija ten język. Istnieje również publiczny proces znany jako KEEP, który pozwala każdemu zobaczyć i komentować oficjalne propozycje zmian w projekcie.
* Wieloplatformowy oznacza, że język można stosować na więcej niż jednej platformie. Na przykład Kotlin można używać zarówno na Androidzie, jak i iOS.
* Wieloparadygmatyczny oznacza, że język obsługuje więcej niż jeden paradygmat programowania. Kotlin ma potężne wsparcie zarówno dla programowania obiektowego, jak i funkcjonalnego.
* Statycznie typowany oznacza, że każda zmienna, obiekt i funkcja ma powiązany typ, który jest znany w czasie kompilacji.
* Ogólnego przeznaczenia oznacza, że język jest zaprojektowany do budowania oprogramowania w szerokim zakresie dziedzin aplikacji na różnorodnych konfiguracjach sprzętowych i systemach operacyjnych.

Te opisy mogą być teraz niejasne, ale zobaczysz je wszystkie w praktyce w trakcie czytania książki. Zacznijmy od omówienia wieloplatformowych możliwości Kotlina.

### Platformy Kotlin

Kotlin to kompilowany język programowania. Oznacza to, że możesz napisać jakiś kod w Kotlin, a następnie użyć kompilatora Kotlin, aby wygenerować kod w języku niższego poziomu. Obecnie Kotlin może być kompilowany do bajtkodu JVM (Kotlin/JVM), JavaScript (Kotlin/JS) lub kodu maszynowego (Kotlin/Native).

{width: 100%}
![](kotlin_compile_results.png)

W tej książce chciałbym omówić wszystkie te targety i domyślnie pokazać kod, który działa na wszystkich z nich, ale skupię się na najbardziej popularnym: Kotlin/JVM.

Kotlin/JVM to technologia służąca do kompilowania kodu Kotlin do bajtkodu JVM. Wynik jest prawie identyczny z wynikiem kompilowania kodu Java do bajtkodu JVM. Termin "Kotlin/JVM" używamy również, aby mówić o kodzie, który zostanie skompilowany do bajtkodu JVM.

![](Kotlin_Java_compile.png)

Kotlin/JVM i Java są w pełni interoperacyjne. Każdy kod napisany w Javie może być używany w Kotlin/JVM. Każda biblioteka w Javie, w tym te oparte na przetwarzaniu adnotacji, może być używana w Kotlin/JVM. Kotlin/JVM może korzystać z klas Java, modułów, bibliotek i biblioteki standardowej Java. Każdy kod Kotlin/JVM może być używany w Javie (z wyjątkiem funkcji zawieszających, które są wsparciem dla Kotlin Coroutines).

![](Java_interoperability.png)

Kotlin i Java mogą być łączone w jednym projekcie. Typowym scenariuszem jest taki, że projekt był początkowo napisany w Javie, ale potem jego twórcy zdecydowali się zacząć używać Kotlina. Zamiast migrować cały projekt, ci programiści postanowili dodać do projektu Kotlin. W związku z tym, za każdym razem, gdy dodadzą nowy plik, będzie to plik Kotlin; ponadto, gdy zrefaktoryzują stary kod Java, zmigrują go do Kotlin. Z biegiem czasu kod Kotlin staje się coraz bardziej rozpowszechniony, aż w końcu całkowicie wyklucza Javę.

![](mix_Kotlin_Java.png)

Jednym z przykładów takiego projektu jest sam kompilator Kotlin. Początkowo został napisany w Javie, ale coraz więcej plików było migrowanych do Kotlin, gdy stał się wystarczająco stabilny. Ten proces trwa już od lat; w chwili pisania tej książki, projekt kompilatora Kotlin wciąż zawiera około 10% kodu Java.

Teraz, gdy zrozumieliśmy związek między Kotlinem a Javą, czas obalić pewne nieporozumienia. Wielu widzi Kotlin jako warstwę cukru składniowego na wierzchu Javy, ale to nieprawda. Kotlin to inny język niż Java. Ma swoje własne konwencje, praktyki oraz funkcjonalności, których Java nie posiada, jak możliwości wieloplatformowe i korutyny. Nie musisz znać Javy, aby zrozumieć Kotlin. Moim zdaniem Kotlin to lepszy pierwszy język niż Java. Juniorzy Kotlin nie muszą znać metody `equals` ani wiedzieć, jak ją nadpisywać. Wystarczy, że rozumieją równość klas i klas danych[^01_4]. Nie muszą uczyć się pisać getterów i setterów ani implementować wzorca singletonu czy buildera. Kotlin ma niższy próg wejścia niż Java i nie potrzebuje platformy JVM, gdyż można używać go na platformie JavaScript czy natywnie.

### Środowisko IDE dla Kotlin

Najpopularniejszymi środowiskami IDE (zintegrowane środowisko programistyczne) dla Kotlin są IntelliJ IDEA oraz Android Studio. Można jednak również korzystać z Kotlin w innych edytorach, takich jak VS Code, Eclipse, Vim, Emacs, Sublime Text i wielu innych. Można też pisać kod w Kotlin online, na przykład, korzystając z oficjalnego IDE online, które można znaleźć pod tym linkiem [play.kotlinlang.org](https://play.kotlinlang.org/).

### Gdzie używamy Kotlin?

Kotlin może być stosowany jako alternatywa dla Java, JavaScript, C++, Objective-C, itp. Jest jednak najbardziej dojrzały na JVM, więc obecnie głównie używany jest jako alternatywa dla Java.

Kotlin stał się dość popularny w rozwoju aplikacji backendowych. Najczęściej widzę go stosowanego z frameworkiem Spring, ale niektóre projekty korzystają z Kotlin w połączeniu z innymi frameworkami backendowymi, takimi jak Vert.x, Ktor, Micronaut, http4k czy Javalin.

Kotlin stał się również praktycznie standardowym językiem dla rozwoju aplikacji na Androida. Google oficjalnie zaleca, aby wszystkie aplikacje na Androida były tworzone w Kotlin[^01_1] i ogłosiło, że wszystkie ich API będą projektowane przede wszystkim z myślą o języku Kotlin[^01_2].

Coraz więcej projektów wykorzystuje fakt, że Kotlin może być kompilowany dla kilku różnych platform, ponieważ pozwala to zespołom na tworzenie kodu, który działa zarówno na Androidzie, jak i iOS, czy zarówno na backendzie, jak i frontendzie. Co więcej, ta wieloplatformowość pozwala twórcom bibliotek na tworzenie jednej biblioteki dla wielu platform jednocześnie. Wieloplatformowe możliwości Kotlin są już wykorzystywane w wielu firmach i stają się coraz bardziej popularne.

Warto również wspomnieć o Jetpack Compose, który jest zestawem narzędzi do budowania natywnych interfejsów użytkownika w Kotlin. Został początkowo opracowany dla Androida, ale wykorzystuje możliwości wieloplatformowe Kotlina i może być również używany do tworzenia stron internetowych, aplikacji na komputery stacjonarne, aplikacji na iOS i innych targetów[^01_3].

Coraz więcej programistów używa Kotlina do przetwarzania danych i analityki. Istnieją też osoby używające go do tworzenia stron webowych, głównie z wykorzystaniem Kotlin React.

Jak widać, już teraz można zrobić wiele rzeczy w języku Kotlin, a z każdym rokiem pojawia się coraz więcej możliwości. Jestem pewien, że znajdziesz dobre sposoby na zastosowanie swojej nowej wiedzy po przeczytaniu tej książki.

[^01_1]: Źródło: https://techcrunch.com/2022/08/18/five-years-later-google-is-still-all-in-on-kotlin/
[^01_2]: Źródło: https://developer.android.com/kotlin/first
[^01_3]: W chwili obecnej dojrzałość tych targetów różni się.
[^01_4]: Zostanie to wyjaśnione w rozdziale *Data klasy*.
