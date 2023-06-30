## Wprowadzenie

Kotlin to niesamowity język wieloplatformowy, głównie dzięki swojej czytelnej składni, intuicyjnemu i bezpiecznemu dla wartości `null` systemowi typów oraz świetnemu wsparciu narzędzi. Nic dziwnego, że stał się najpopularniejszym językiem do tworzenia aplikacji na Androida i, w przypadku aplikacji backendowych, popularną alternatywą dla Javy. Jest również używany przy przetwarzaniu i analizie danych oraz w programowaniu wieloplatformowych aplikacji na iOS, komputery stacjonarne i strony internetowe. W tej książce nauczysz się najważniejszych funkcji języka Kotlin, które pozwolą Ci właściwie rozpocząć Twoją przygodę z Kotlinem.

### Dla kogo jest ta książka?

Ta książka jest dedykowana programistom. Zakładam, że wszyscy programiści wiedzą, czym są funkcje, instrukcje warunkowe if czy stringi. Niemniej jednak staram się wyjaśnić (przynajmniej w skrócie) wszystkie rzeczy, które mogą nie być jasne dla wszystkich programistów, takie jak klasy, enumy czy listy. Powinna to być dobra książka dla osób używających C, JavaScript czy Matlaba, choć w zrozumieniu jej może być przydatne choćby podstawowe zrozumienie konceptów obiektowych, takich jak klasy i obiekty. 

Ponieważ większość programistów piszących w Kotlinie ma doświadczenie w Javie, czasami odnoszę się do niej i jej platformy JVM, oraz od czasu do czasu przedstawiam elementy specyficzne dla tej platformy. Za każdym razem, gdy to robię, staram się wyraźnie to zaznaczyć. Zakładam, że niektórzy czytelnicy mogą być głównie zainteresowani używaniem Kotlin/JS lub Kotlin/Native, więc wszystko, co nie jest opisane jako specyficzne dla Javy, powinno być dla nich użyteczne. 
 
### Co zostanie omówione?

W tej książce omawiam te tematy, które uważam za niezbędne do programowania w języku Kotlin, w tym:

* zmienne, wartości i typy,
* instrukcje warunkowe i pętle,
* wsparcie dla wartości nullowalnych,
* klasy, interfejsy i dziedziczenie,
* wyrażenia i deklaracje obiektów,
* klasy typu data, enum i sealed,
* anotacje i wyjątki,
* funkcje rozszerzające,
* kolekcje,
* przeciążanie operatorów,
* system typów,
* generyki.

Ta książka nie omawia funkcyjnych cech Kotlina, takich jak wyrażenia lambda czy typy funkcyjne. Te tematy są omówione w kontynuacji tej książki: *Funkcyjny Kotlin*.

### Język użyty w książce

Kiedy zacząłem czytać "poprawne" tłumaczenie innej swojej książki, nieraz musiałem sięgać do oryginału, by zrozumieć przetłumaczony fragment. W języku polskim przyjęło się wiele tłumaczeń pojęć technicznych, które są formalnie poprawne, ale nie są używane na co dzień (albo nawet wcale) przez programistów. Efekt jest taki, że tekst "poprawnie przetłumaczonych" książek jest bardzo trudny do zrozumienia, w wyniku czego większość programistów preferuje czytać w języku oryginału. Jest to jednak wylanie dziecka z kąpielą. Jest wartość w tym, że książka jest przetłumaczona na język ojczysty, po prostu powinno się to robić z głową.

W tej książce chciałbym złamać to błędne koło. Przy jej tłumaczeniu postawiłem **wyłącznie** na pragmatyzm. Starałem się używać języka, który jest używany przez programistów, a nie formalnie poprawnych tłumaczeń. Bardzo często oznacza to używanie anglicyzmów. Będę na przykład mówił o stringach, data klasach, enumach, sealed klasach itd. Tam jednak, gdzie znaczenie polskiego słowa wydaje mi się dostatecznie zrozumiałe, będę używał polskiego słowa. Na przykład, będę mówił o klasach, interfejsach, dziedziczeniu itd. W wielu miejscach miałem niemały dylemat, jakiego pojęcia użyć. I tak mówię o "rozszerzeniach" a nie "extensionach" głównie ze względu na to, że zdecydowałem się używać pojęcia "funkcje" a nie "functions", a "funkcje rozszerzające" brzmią znacznie lepiej niż "funkcje typu extension". Być może nie zgodzisz się ze wszystkimi moimi decyzjami, zapewne niejeden skrytykuje kalanie języka ojczystego licznymi anglicyzmami, ale mam nadzieję, że reszta czytelników doceni pragmatyzm, którym kierowałem się przy pisaniu tej książki.

Lista tłumaczeń pojęć, jakich zdecydowałem się używać w tej książce, znajduje się w readme repozytorium tej książki, pod adresem [github.com/MarcinMoskala/kotlin_essentials_book_pl](github.com/MarcinMoskala/kotlin_essentials_book_pl). 

### Seria Kotlin dla programistów

Ta książka jest pierwszą z serii książek o nazwie *Kotlin dla programistów*, która obejmuje następujące pozycje:
* *Kotlin dla programistów*, omawiający wszystkie podstawowe funkcjonalności Kotlina.
* *Funkcyjny Kotlin*, poświęcony funkcyjnym funkcjonalnościom Kotlina, takim jak typy funkcyjne, wyrażenia lambda, przetwarzanie kolekcji, DSL i funkcje zakresu.
* *Kotlinowe Korutyny*, poświęcony korutynom, służącym do efektywnego programowania asynchronicznego w Kotlinie.
* *Zaawansowany Kotlin*, poświęcony zaawansowanym funkcjonalnościom Kotlina, takim jak modyfikatorom wariancji generyków, delegacji, refleksji, programowaniu wieloplatformowemu, przetwarzaniu adnotacji, KSP, pluginom kompilatora itp.
* *Efektywny Kotlin*, poświęcony dobrym praktykom programowania w Kotlinie.

### Moja historia

Moja historia z Kotlinem zaczęła się w 2015 roku, gdy pracowałem jako programista Android w Javie. Byłem dość sfrustrowany całym tym powtarzalnym kodem, takim jak gettery i settery dla każdego pola, oraz niemal identycznymi metodami `equals`, `toString` i `hashCode`, które często powtarzają się w wielu klasach. Wtedy znalazłem Kotlina na stronie JetBrains i tak mnie ten język zafascynował, że każdą wolną chwilę poświęcałem na jego eksplorację. Wkrótce potem dostałem pracę jako programista Kotlina i zatraciłem się w tym świecie. Teraz już od ponad ośmiu lat zawodowo używam Kotlina, w trakcie których opublikowałem setki artykułów o tym języku i na powiązane tematy. Opublikowałem też kilka książek oraz przeprowadziłem ponad sto warsztatów. Stałem się oficjalnym partnerem JetBrains w nauczaniu Kotlina oraz Google Developer Expert w Kotlinie. Podczas tych wszystkich doświadczeń zgromadziłem dużo wiedzy, więc postanowiłem wyrazić ją w postaci serii książek, którą nazywam *Kotlin dla programistów*.

### Konwencje kodu

Prezentowane fragmenty to najczęściej wykonywalny kod bez instrukcji importowania. W wersji online tej książki na stronie Kt. Academy większość fragmentów można uruchomić, dzięki czemu czytelnicy mogą eksperymentować z kodem.

Wyniki fragmentów są prezentowane za pomocą funkcji `println`. Wynik często będzie umieszczony po instrukcji. Oto przykład:

```kotlin
fun main() {
    println("Hello") // Hello
    println(10 + 20) // 30
}
```

{pagebreak}

### Podziękowania do wersji polskiej

To tłumaczenie wiele zawdzięcza osobom, które zgodziły się poświęcić swój wolny czas by pomóc mi w poprawieniu języka tłumaczenia. Najważniejsze osoby to:



Książka również przeszła przez profesjonalną korektę językową przeprowadzoną przez XXX. 

{pagebreak}

### Podziękowania

{width: 25%, float: left, }
![](owen.jpg)

**Owen Griffiths** tworzy oprogramowanie od połowy lat 90. i pamięta produktywność języków takich jak Clipper czy Borland Delphi. Od 2001 roku przeniósł się do świata internetu, serwerów opartych na Javie i rewolucji open-source. Posiadając wiele lat doświadczenia komercyjnego w Javie, poznał Kotlina na początku 2015 roku. Po objazdach przez Clojure i Scalę, niczym Złotowłosa, uważa, że Kotlin jest właśnie w sam raz i ma najlepszy smak. Owen z entuzjazmem pomaga deweloperom Kotlina odnosić dalsze sukcesy.

{width: 25%, float: left, }
![](nicola_corti.jpeg)

**Nicola Corti** jest Google Developer Expertem w dziedzinie Kotlina. Pracuje z tym językiem od czasów przed wersją 1.0 i jest opiekunem kilku bibliotek open-source i narzędzi dla deweloperów mobilnych (Detekt, Chucker, AppIntro). Obecnie pracuje w zespole React Native w Meta, tworząc jeden z najpopularniejszych frameworków mobilnych na różne platformy. Ponadto jest aktywnym członkiem społeczności deweloperskiej. Jego zaangażowanie obejmuje wystąpienia na międzynarodowych konferencjach, bycie członkiem komitetów CFP oraz wspieranie społeczności deweloperów w całej Europie. W wolnym czasie uwielbia piec, tworzyć podcasty i biegać.

{width: 25%, float: left, }
![](Matthias.jpg)

**Matthias Schenk** rozpoczął swoją karierę z Javą ponad dziesięć lat temu, głównie w ekosystemie Spring/Spring Boot. Osiemnaście miesięcy temu przeszedł na Kotlina i od tego czasu stał się wielkim fanem pracy z natywnymi frameworkami Kotlina, takimi jak Koin, Ktor i Exposed.

{pagebreak}

{width: 25%, float: left, }
![](deak.jpeg)

**Endre Deak** jest architektem oprogramowania, który tworzy infrastrukturę AI w Disco, wiodącej firmie zajmującej się technologią prawniczą. Ma 15 lat doświadczenia w budowaniu złożonych, skalowalnych systemów i uważa, że Kotlin to jeden z najlepszych języków programowania, jakie kiedykolwiek powstały.

{width: 25%, float: left, }
![](Emanuele_Papa.png)

**Emanuele Papa** jest pasjonatem Androida i fascynuje go od 2010 roku: im więcej się uczy, tym bardziej chce dzielić się swoją wiedzą z innymi, dlatego założył własnego bloga. W swojej obecnej roli jako Senior Android Developer w Zest One skupia się na Kotlin Multiplatform Mobile: już wygłosił kilka prelekcji na ten temat.

**Roman Kamyshnikov**, doktor inżynierii, jest programistą Androida, który rozpoczął swoją karierę z Javą, ale przeszedł na Kotlina na początku 2020 roku. Jego zainteresowania zawodowe obejmują wzorce architektury, TDD, programowanie funkcyjne oraz Jetpack Compose. Autor kilku artykułów na temat Androida i Kotlin Coroutines.

**Grigory Pletnev** jest inżynierem oprogramowania od 2000 roku, głównie w dziedzinie systemów wbudowanych. Dołączył do społeczności programistów Androida w 2010 roku. Zetknąwszy się z Kotlinem w 2017 roku, zaczął używać go w projektach hobbystycznych, stopniowo migrując projekty Harman Connected Services i ich klientów na Kotlina. Pasjonuje się również językami, żeglarstwem i produkcją miodu pitnego.
