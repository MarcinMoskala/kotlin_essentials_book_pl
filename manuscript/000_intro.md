## Wprowadzenie

Kotlin to niesamowity język wieloplatformowy, głównie dzięki swojej czytelnej składni, intuicyjnemu i bezpiecznemu dla wartości null systemowi typów oraz świetnemu wsparciu narzędzi. Nic dziwnego, że stał się najpopularniejszym językiem do tworzenia aplikacji na Androida i, w przypadku aplikacji backendowych, popularną alternatywą dla Javy. Jest również używany przy przetwarzaniu i analizie danych oraz w programowaniu wieloplatformowych aplikacji na iOS, komputery stacjonarne i strony internetowe. W tej książce nauczysz się najważniejszych funkcji języka Kotlin, które pozwolą Ci właściwie rozpocząć twoją przygodę z Kotlinem.

### Dla kogo jest ta książka?

Ta książka jest dedykowana programistom. Zakładam, że wszyscy programiści wiedzą, czym są funkcje, instrukcje warunkowe if czy ciągi znaków. Niemniej jednak staram się wyjaśnić (przynajmniej w skrócie) wszystkie rzeczy, które mogą nie być jasne dla wszystkich programistów, takie jak klasy, enumy czy listy. Na pewno będzie to dobra ksiżka dla osób używających C, JavaScript czy Matlaba.

Ponieważ większość programistów Kotlin ma doświadczenie w Javie, więc czasami odnoszę się do Javy i jej platformy i czasami przedstawiam elementy specyficzne dla JVM. Za każdym razem, gdy to robię, staram się wyraźnie to zaznaczyć. Zakładam, że niektórzy czytelnicy mogą być głównie zainteresowani używaniem Kotlin/JS lub Kotlin/Native, więc wszystko, co nie jest opisane jako specyficzne dla Javy, powinno być dla nich użyteczne. 
 
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

Ta książka nie omawia funkcyjnych cech Kotlin, takich jak wyrażenia lambda czy typy funkcyjne. Te tematy są omówione w kontynuacji tej książki: *Functional Kotlin*.


### Seria Kotlin dla programistów

Ta książka jest pierwszą z serii książek o nazwie *Kotlin dla programistów*, która obejmuje następujące pozycje:
* Kotlin Podstawy, omawiający wszystkie podstawowe funkcje Kotlin.
* Funkcjonalny Kotlin, poświęcony funkcjonalnym cechom Kotlin, w tym typom funkcji, wyrażeniom lambda, przetwarzaniu kolekcji, DSL-om i funkcjom zakresu.
* Zaawansowany Kotlin, poświęcony zaawansowanym funkcjom Kotlin, w tym modyfikatorom wariancji generyków, delegacji, dokumentowaniu kodu, przetwarzaniu ciągów znaków, refleksji i programowaniu wieloplatformowemu.

Ta książka oraz moje dwie inne książki, *Kotlin Korutyny* i *Efektywny Kotlin*, tworzą dużą serię obejmującą wszystko, co moim zdaniem jest potrzebne, aby stać się niesamowitym programistą Kotlin.


### Moja historia

Moja historia z Kotline zaczęła się w 2015 roku, gdy pracowałem jako programista Android w Javie. Byłem dość sfrustrowany całym tym powtarzalnym kodem, takim jak gettery i settery dla każdego pola, oraz niemal identycznymi metodami `equals`, `toString` i `hashCode`, które często powtarzają się w wielu klasach. Wtedy znalazłem Kotlin na stronie JetBrains i tak mnie zafascynował ten język, że każdą wolną chwilę poświęcałem na jego eksplorację. Wkrótce potem dostałem pracę jako programista Kotlin i zatopiłem się w jego społeczności. Teraz, już od ponad siedmiu lat, zawodowo używam Kotlin. Wciągu tych siedmiu lat opublikowałem setki artykułów o tym języku i na powiązane tematy. Opublikowałem też kilka książek oraz przeprowadziłem ponad sto warsztatów. Stałem się oficjalnym partnerem JetBrains w nauczaniu Kotlin oraz Google Developer Expert w Kotlin. Podczas tych wszystkich doświadczeń zgromadziłem dużo wiedzy, więc postanowiłem podzielić się nią w postaci serii książek, którą nazywam *Kotlin dla programistów*.


### Konwencje kodu

Większość prezentowanych fragmentów, to wykonywalny kod bez instrukcji importowania. W wersji online tej książki na stronie Kt. Academy większość fragmentów można wykonać, dzięki czemu czytelnicy mogą eksperymentować z kodem.

Wyniki fragmentów są prezentowane za pomocą funkcji `println` i często będzie umieszczony po instrukcji. Oto przykład:

```kotlin
fun main() {
    println("Hello") // Hello
    println(10 + 20) // 30
}
```

{pagebreak}


### Podziękowania

{width: 25%, float: left, }
![](owen.jpg)

**Owen Griffiths** tworzy oprogramowanie od połowy lat 90. i pamięta produktywność języków takich jak Clipper czy Borland Delphi. Od 2001 roku obecny w świecie internetu, serwerów opartych na Javie i rewolucji open-source. Po wielu latach doświadczenia komercyjnego w Javie, od początku 2015 roku zaczął przyswajać Kotlin. Po objazdach przez Clojure i Scalę, niczym Złotowłosa, uważa, że Kotlin jest właśnie w sam raz i ma najlepszy smak. Owen z entuzjazmem pomaga deweloperom Kotlin rozwijać się i odnosić sukcesy.

{width: 25%, float: left, }
![](nicola_corti.jpeg)

**Nicola Corti** jest Google Developer Expertem w dziedzinie Kotlin. Pracuje z tym językiem od czasów przed wersją 1.0 i jest opiekunem kilku bibliotek open-source i narzędzi dla deweloperów mobilnych (Detekt, Chucker, AppIntro). Obecnie pracuje w zespole React Native w Meta, tworząc jeden z najpopularniejszych frameworków mobilnych na różne platformy. Ponadto jest aktywnym członkiem społeczności deweloperskiej. Jego zaangażowanie obejmuje wystąpienia na międzynarodowych konferencjach. Jest członkiem komitetów CFP oraz wspiera społeczności deweloperów w całej Europie. W wolnym czasie uwielbia piec, tworzyć podcasty i biegać.

{width: 25%, float: left, }
![](Matthias.jpg)

**Matthias Schenk** rozpoczął swoją karierę z Javą ponad dziesięć lat temu, głównie w ekosystemie Spring/Spring Boot. Osiemnaście miesięcy temu przeszedł na Kotlin i od tego czasu stał się wielkim fanem pracy z natywnymi frameworkami Kotlin, takimi jak Koin, Ktor i Exposed.

{pagebreak}

{width: 25%, float: left, }
![](deak.jpeg)

**Endre Deak** jest architektem oprogramowania. Tworzy infrastrukturę AI w Disco, wiodącej firmie zajmującej się technologią prawniczą. Ma 15 lat doświadczenia w budowaniu złożonych, skalowalnych systemów i uważa, że Kotlin to jeden z najlepszych języków programowania, jakie kiedykolwiek powstały.

{width: 25%, float: left, }
![](Emanuele_Papa.png)

**Emanuele Papa** jest pasjonatem Androida, który fascynuje go od 2010 roku: im więcej się uczy, tym bardziej chce dzielić się swoją wiedzą z innymi, dlatego założył własnego bloga. W swojej obecnej roli, jako Senior Android Developer w Zest One, skupia się na Kotlin Multiplatform Mobile. Wygłosił kilka prelekcji na ten temat.

**Roman Kamyshnikov**, doktor inżynierii, jest programistą Androida, który rozpoczął swoją karierę z Javą, ale przeszedł na Kotlin na początku 2020 roku. Jego zainteresowania zawodowe obejmują wzorce architektury, TDD, programowanie funkcyjne oraz Jetpack Compose. Autor kilku artykułów na temat Androida i Kotlin Coroutines.

**Grigory Pletnev** jest inżynierem oprogramowania od 2000 roku, głównie w dziedzinie systemów wbudowanych. Dołączył do społeczności programistów Androida w 2010 roku. Zetknąwszy się z Kotlina w 2017 roku, zaczął używać go w projektach hobbystycznych, stopniowo migrując projekty Harman Connected Services i ich klientów na Kotlin. Pasjonuje się również językami, żeglarstwem i produkcją miodu pitnego.
