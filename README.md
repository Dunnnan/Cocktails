# Aplikacje Mobilne – Cocktails

Autorzy:  
**Maciej Wrzesień**  
**Kamil Bryła**

## Spis treści
- [Struktura plików](#struktura-plików)
- [Funkcjonalności](#funkcjonalności)
  - [Główna nawigacja](#główna-nawigacja)
  - [Szuflada nawigacyjna](#szuflada-nawigacyjna)
  - [Strona główna](#strona-główna)
  - [Ekran katalogu koktajli](#ekran-katalogu-koktajli)
  - [Szczegóły](#szczegóły)
  - [Informacje](#informacje)
  - [Minutnik](#minutnik)
  - [Przycisk FAB](#przycisk-fab)
  - [Gesty](#gesty)
  - [Motywy](#motywy)
  - [Pasek narzędzi](#pasek-narzedzi)
  - [Ikony](#ikony)
  - [Animacje](#animacje)
- [Wymagania](#wymagania)
  - [Wersja na tablety](#wersja-na-tablety)
  - [Material design](#material-design)
  - [Minutnik](#minutnik)
  - [Elementy biblioteki wsparcia wzornictwa](#wzornictwo)
  - [Animacje](#animacje2)
  

---

<a id="struktura-plików"></a>
## Struktura plików

Projekt aplikacji mobilnej został zorganizowany w logiczne pakiety:

- `components/` – komponenty UI:
  - `CocktailCardItem.kt` – pojedyncza karta koktajlu.
  - `DrawerContent.kt` – zawartość szuflady nawigacyjnej.
  - `StartScreenFunctions.kt` – obsługa ekranu startowego.
  - `TimerActivity.kt` – komponent czasomierza.

- `data/` – dane aplikacji:
  - `CocktailsBase.kt` – statyczna baza danych koktajli.

- `models/` – modele danych:
  - `Cocktail.kt` – klasa reprezentująca koktajl.

- `screens/` – widoki aplikacji:
  - `CocktailDetailScreen.kt`
  - `CocktailList.kt`
  - `Home.kt`
  - `Info.kt`

- `ui.theme/` – motyw i styl aplikacji:
  - `Color.kt`
  - `Theme.kt`
  - `Type.kt`

- `MainActivity.kt` – główna aktywność aplikacji.

---

<a id="funkcjonalności"></a>
## Funkcjonalności

Cały interfejs użytkownika został zaimplementowany z wykorzystaniem biblioteki **Jetpack Compose**. Aplikacja dynamicznie reaguje na zmianę orientacji urządzenia, zachowując poprawne ułożenie i działanie interfejsu, co zostało przedstawione na załączonym materiale wideo.

Dodatkowo, większość ekranów w aplikacji wykorzystuje bardzo podobną strukturę, opartą na komponencie `Scaffold` z paskiem narzędzi (`topBar`) oraz podstawowymi elementami interfejsu, takimi jak `Column`, `Row`, `Box`, `Icon`, `IconButton`, `AsyncImage`, `ElevatedButton`, `Text` czy `Spacer`.

Aby uniknąć powtarzania tego samego kodu w wielu miejscach, poniżej przedstawiono uogólniony przykład struktury ekranu. W dalszej części sprawozdania prezentowane będą jedynie istotne fragmenty kodu specyficzne dla danego widoku.

```kotlin
@Composable
fun Home(darkTheme: MutableState<Boolean>, drawerState: DrawerState, scope: CoroutineScope) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Strona Glowna") },
                navigationIcon = {
                    Icon(painterResource(R.drawable.ic_action_search_dark), contentDescription = null)
                },
                actions = {
                    IconButton(onClick = { darkTheme.value = !darkTheme.value }) {
                        Icon(painterResource(if (darkTheme.value) R.drawable.ic_action_brightness_light else R.drawable.ic_action_brightness_dark),
                             contentDescription = "Przelacz motyw")
                    }
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(painterResource(if (darkTheme.value) R.drawable.ic_action_hamburger_dark else R.drawable.ic_action_hamburger_light),
                             contentDescription = "Otworz menu")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Witaj anonimowy alkoholiku!\nZyczymy poszerzenia alkoholowych horyzontów :)",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            ElevatedButton(
                onClick = { scope.launch { drawerState.open() } },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Rozpocznij przygodę z koktajlami")
            }

            // Przycisk zmiany ekranu startowego i inne elementy...
        }
    }
}
```
<a id="główna-nawigacja"></a>
### Główna nawigacja

Aplikacja oparta jest na architekturze jednowątkowej (*Single-Activity Architecture*), gdzie `MainActivity` hostuje cały interfejs użytkownika w postaci funkcji typu `@Composable`. Główna funkcja odpowiedzialna za strukturę i nawigację aplikacji to `CocktailApp()`.

W aplikacji zastosowano **Navigation Compose** do zarządzania trasami między ekranami. Nawigacja odbywa się z wykorzystaniem `NavHost`, gdzie każdy ekran to osobna ścieżka (`composable`) i funkcja `composable`. Dzięki parametrowaniu tras (np. ekran szczegółów z zakodowanym obiektem JSON), możliwe jest dynamiczne przekazywanie danych między ekranami.

Obsługiwane są różne przejścia animacyjne (np. `fadeIn()`, `slideInVertically()`), a także płynne nawigowanie do ekranów szczegółowych na podstawie danych w formacie JSON. Ekrany `Home`, `Info`, `CocktailList` (z różnymi filtrami) oraz `CocktailDetailScreen` są zdefiniowane w `NavHost` i dostępne z poziomu szuflady nawigacyjnej.

Całość osadzona jest wewnątrz `ModalNavigationDrawer`, umożliwiającego prezentację szuflady z dostępem do najważniejszych funkcji aplikacji.

```kotlin
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CocktailApp(darkTheme: MutableState<Boolean>, startScreen: String) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val gson = Gson()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                navController = navController,
                drawerState = drawerState,
                scope = scope,
                darkTheme = darkTheme
            )
        }
    ) {
        NavHost(navController = navController, startDestination = startScreen) {
            // Komponenty: home, katalogi, info, szczegoly
        }
    }
}
```
<a id="szuflada-nawigacyjna"></a>
### Szuflada nawigacyjna

Szuflada nawigacyjna została zaimplementowana w postaci funkcji `DrawerContent()`, która odpowiada za prezentację i obsługę menu aplikacji w komponencie `ModalNavigationDrawer`. Menu umożliwia szybkie przełączanie się pomiędzy ekranami aplikacji (`Home`, `CocktailList`, `Info` itp.) oraz automatycznie dostosowuje ikony do aktualnie wybranego motywu (ciemny/jasny).

Każda pozycja menu to wiersz z ikoną i etykietą, reagujący na kliknięcia. Po kliknięciu:
- Zamyka się szuflada przy użyciu korutyny,
- Następuje przejście do odpowiedniego ekranu dzięki `NavController`.

Całość wykorzystuje standardowe komponenty Jetpack Compose: `Column`, `Row`, `Icon`, `Text`, a wygląd dostosowano do aktualnego motywu poprzez dynamiczne wybieranie ikon i kolorów.

```kotlin
@Composable
fun DrawerContent(
    navController: NavController,
    drawerState: DrawerState,
    scope: CoroutineScope,
    darkTheme: MutableState<Boolean>
) {
    val destinations = listOf(
        Triple("Strona Glowna", "home", R.drawable.ic_action_search_dark),
        Triple("Wszystkie koktajle", "all", R.drawable.ic_action_all_dark),
        Triple("Bezalkoholowe", "nonalcoholic", R.drawable.ic_action_nonalcoholic_light),
        Triple("Z wodka", "vodka", R.drawable.ic_action_vodka_dark),
        Triple("Informacje", "info", R.drawable.ic_action_settings_dark)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(8.dp)
    ) {
        destinations.forEach { (label, route, icon) ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        scope.launch { drawerState.close() }
                        navController.navigate(route)
                    }
                    .padding(12.dp)
            ) {
                // ...
            }
        }
    }
}
```
<a id="strona-główna"></a>
### Strona główna

Strona główna aplikacji jest implementowana jako funkcja `Home` w Jetpack Compose. Zawiera pasek narzędzi z tytułem, ikonę typu katalogu, przycisk przełączający motyw oraz otwierający menu nawigacyjne. Główna część ekranu to powitalny tekst oraz przyciski umożliwiające rozpoczęcie korzystania z aplikacji i zmianę ekranu startowego.

---

<a id="ekran-katalogu-koktajli"></a>
### Ekran katalogu koktajli

Ekran katalogu (`CocktailList`) odpowiedzialny jest za wyświetlanie wszystkich dostępnych koktajli w formie siatki kart. Zrealizowano to przy pomocy komponentu `LazyVerticalGrid`, który zapewnia efektywne renderowanie dużej liczby elementów oraz automatyczne zarządzanie pamięcią.

W górnej części ekranu umieszczono pasek narzędzi (`TopAppBar`) z dynamicznym polem wyszukiwania (`TextField`), pozwalającym filtrować koktajle według nazwy. Ponadto, użytkownik może przełączać motyw (jasny/ciemny) oraz otwierać nawigacyjne menu boczne za pomocą ikony hamburgera.

Filtrowanie listy koktajli odbywa się dwuetapowo:
- najpierw według przekazanego warunku logicznego `filter`,
- następnie według wpisanego tekstu (`searchQuery`).

Zastosowanie parametru `GridCells.Adaptive(minSize = 250.dp)` sprawia, że siatka układu jest responsywna — automatycznie dostosowuje liczbę kolumn do rozmiaru ekranu. Dzięki temu interfejs jest wygodny zarówno na telefonach, jak i tabletach.

```kotlin
LazyVerticalGrid(
    columns = GridCells.Adaptive(minSize = 250.dp),
    contentPadding = PaddingValues(8.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp),
    modifier = Modifier.padding(padding)
    ...
) {
    items(cocktails) { cocktail ->
        CocktailCardItem(cocktail = cocktail, onCocktailClick = {
            onCocktailClick(cocktail)
        })
    }
}
```

<a id="szczegóły"></a>
### Szczegóły

Ekran szczegółów prezentuje pełne informacje o wybranym koktajlu – składniki, sposób przygotowania oraz możliwość uruchomienia minutnika.

---
<a id="informacje"></a>
### Informacje

Ekran informacyjny wyjaśnia znaczenie ikon użytych w aplikacji.

---

<a id="minutnik"></a>
### Minutnik

Wbudowany minutnik, uruchamiany z poziomu ekranu szczegółów koktajlu, umożliwia odmierzanie czasu przygotowania drinka. Minutnik działa w tle i zachowuje swój stan nawet po zmianie orientacji ekranu.

```kotlin
LaunchedEffect(isRunning) {
    while (isRunning && (minutes > 0 || seconds > 0)) {
        delay(1000L)
        if (seconds - 1 < 0) {
            seconds = 59
            minutes--
        } else {
            seconds--
        }
    }
    isRunning = false
}
```


```kotlin
if (isPicking && !isRunning) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Minutes: ${minutes}")
        Slider(
            value = minutes.toFloat(),
            onValueChange = { minutes = it.toInt() },
            valueRange = 0f..59f,
            steps = 59,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
        Text("Seconds: ${seconds}")
        Slider(
            value = seconds.toFloat(),
            onValueChange = { seconds = it.toInt() },
            valueRange = 0f..59f,
            steps = 59,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
    }
}
```

<a id="przycisk-fab"></a>
### Przycisk FAB

W aplikacji zastosowano pływający przycisk akcji (Floating Action Button), służący do wyświetlenia składników koktajlu. Jest on zawsze widoczny na ekranie szczegółów.

```kotlin
floatingActionButton = {
    FloatingActionButton(onClick = {
        Toast.makeText(context, "Skladniki: \n ${cocktail.ingredients.joinToString()}", Toast.LENGTH_SHORT).show()
    }) {
        val icon = if (darkTheme.value) R.drawable.ic_action_fab_dark else R.drawable.ic_action_fab_light
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(45.dp)
        )
    }
},
```

<a id="gesty"></a>
### Gesty

Aplikacja wspiera nawigację gestami — użytkownik może zmieniać ekrany katalogowe za pomocą przeciągnięcia - lewo/prawo. Dodatkowo przeciągnięcie w prawo na pasku akcji powoduje rozwinięcie szuflady nawigacyjnej.

```kotlin
.pointerInput(Unit) {
    detectHorizontalDragGestures { change, dragAmount ->
        if (dragAmount > 100f) {
            // Swipe w prawo
            val prevIndex = (currentIndex - 1 + screenRoutes.size) % screenRoutes.size
            navController.navigate(screenRoutes[prevIndex]) {
                restoreState = true
            }
        } else if (dragAmount < -100f) {
            // Swipe w lewo
            val nextIndex = (currentIndex + 1) % screenRoutes.size
            navController.navigate(screenRoutes[nextIndex]) {
                restoreState = true
            }
        }
    }
}
```

<a id="motywy"></a>
### Motywy

Aplikacja pozwala użytkownikowi zmianę motywu aplikacji na - jasny/ciemny poprzez akcję zamieszczoną na pasku akcji. Dodatkowo w momencie uruchomienia aplikacji motyw dostosowywany jest do motywu systemu.

```kotlin
actions = {
    IconButton(onClick = { darkTheme.value = !darkTheme.value }) {
        val icon = if (darkTheme.value) R.drawable.ic_action_brightness_light else R.drawable.ic_action_brightness_dark
        Icon(
            painter = painterResource(id = icon),
            contentDescription = "Przełącz motyw",
            modifier = Modifier.size(45.dp)
        )
    }
}
```

<a id="pasek-narzedzi"></a>
### Pasek narzędzi

Każdy ekran zawiera pasek narzędzi (TopAppBar), dostosowany kontekstowo do widocznego widoku. Zawiera on tytuł, ikony akcji oraz przycisk rozwijający szufladę nawigacyjną (jeśli dotyczy).

<a id="ikony"></a>
### Ikony

Aplikacja wykorzystuje system ikon do wizualnej komunikacji funkcji oraz atrybutów koktajli. Ikony własne. Ikona aplikacji własna.

![Ikona aplikacji](app%20icon.png)

<div style="display: flex; justify-content: space-between; margin-top: 1em; margin-bottom: 1em;">
  <img src="vodka icon.png" alt="vodka" style="width: 19%;" />
  <img src="all icon.png" alt="all" style="width: 19%;" />
  <img src="dark icon.png" alt="lighttheme" style="width: 19%;" />
  <img src="nonalcohol.png" alt="nonalcohol" style="width: 19%;" />
  <img src="FAB icon.png" alt="fab" style="width: 19%;" />
</div>

<div style="display: flex; justify-content: space-between;">
  <img src="search icon.png" alt="search" style="width: 19%;" />
  <img src="hamburger icon.png" alt="hamburger" style="width: 19%;" />
  <img src="back icon2.png" alt="back" style="width: 19%;" />
  <img src="settings icon.png" alt="settings" style="width: 19%;" />
  <img src="brightness icon.png" alt="darktheme" style="width: 19%;" />
</div>

*Rysunek: Ikony aplikacji*

<a id="animacje"></a>
### Animacje

Aplikacja zawiera animowany ekran startowy, który wyświetlany jest podczas ładowania aplikacji oraz unikalne przejścia ekranów - boczny slide dla szczegółów, pionowy slide dla home oraz informacje oraz fade dla katalogów.

```xml
<!-- Zoom In/Out Animation -->
<objectAnimator
    android:propertyName="scaleX"
    android:duration="650"
    android:valueFrom="0.7"
    android:valueTo="1.85"
    android:repeatCount="1"
    android:repeatMode="reverse"
    android:interpolator="@android:interpolator/linear_out_slow_in"
    android:valueType="floatType"/>

<objectAnimator
    android:propertyName="scaleY"
    android:duration="650"
    android:valueFrom="0.75"
    android:valueTo="1.85"
    android:repeatCount="1"
    android:repeatMode="reverse"
    android:interpolator="@android:interpolator/linear_out_slow_in"
    android:valueType="floatType"/>

<!-- Rotation Animation -->
<objectAnimator
    android:propertyName="rotation"
    android:duration="2500"
    android:valueFrom="360"
    android:valueTo="0"
    android:repeatCount="0"
    android:repeatMode="reverse"
    android:interpolator="@android:interpolator/linear_out_slow_in"
    android:valueType="floatType"/>
```

```kotlin
composable(
    "home",
    enterTransition = { return@composable fadeIn() + slideInVertically(initialOffsetY  = { 1000 }) },
    exitTransition = { return@composable fadeOut() },
    popExitTransition = { return@composable fadeOut() },
    popEnterTransition = { return@composable fadeIn() + slideInVertically(initialOffsetY  = { 1000 }) },
)
```
<a id="wymagania"></a>
# Wymagania

<a id="wersja-na-tablety"></a>
## Wersja na tablety

W celu osiągnięcia responsywności zastosowano komponent `LazyVerticalGrid` z parametrem `GridCells.Adaptive(minSize = 250.dp)`. Dzięki temu liczba kolumn w siatce koktajli dostosowuje się dynamicznie: na mniejszych ekranach (np. telefonach) wyświetlane są zazwyczaj 1–2 kolumny, natomiast na większych ekranach (np. tabletach) nawet 3 lub więcej, zależnie od dostępnej szerokości.  
Używanie jednostki `dp` pozwala na zachowanie odpowiednich proporcji i rozmiarów elementów na różnych urządzeniach, niezależnie od ich rozdzielczości czy gęstości pikseli.

<a id="material-design"></a>
## Material design

Zastosowano komponenty, takie jak `TopAppBar`, `FloatingActionButton`, `Scaffold` czy `MaterialTheme`, zapewniają spójny wygląd. Dodatkowo aplikacja wspiera automatyczne przełączanie motywu jasnego i ciemnego.

<a id="minutnik"></a>
## Minutnik

### Wymagania minimalne

1. ✅ We fragmencie szczegółów należy zagnieździć fragment dynamiczny minutnika  
2. ✅ Minutnik ma wyświetlać czas z dokładnością do sekundy  
3. ✅ Minutnik powinien działać poprawnie na smartfonach i tabletach  
4. ✅ Minutnik powinien działać poprawnie po zmianie orientacji urządzenia  
5. ✅ Minutnik powinien mieć przyciski:  
   1. Start – uruchamiający odliczanie  
   2. Stop – wyłączający odliczanie  
   3. Przerwij – wyłączający odliczanie w dowolnym momencie  

### Wymagania dodatkowe

1. ✅ Kod w **Kotlinie**  
2. ✅ Interfejs użytkownika w **Jetpack Compose**  
3. ✅ Umieszczenie na przyciskach **ikon zamiast napisów**  
4. ❌ **Dodatkowe funkcje** lub dodatkowe przyciski  
5. ✅ Możliwość **ustawienia odliczanego czasu**


<a id="wzornictwo"></a>
## Elementy biblioteki wsparcia wzornictwa

### Wymagania minimalne

1. ✅ Karty kategorii zamiast listy nazw koktajlów mają używać widoku RecyclerView z układem siatki (grid), w którym poszczególne pozycje (koktajle) będą prezentowane w postaci obrazka i nazwy, dla których użyto widoku CardView. Kliknięcie wybranej pozycji (szlaku) powoduje wyświetlenie szczegółów, czyli nazwy koktajlu, większego obrazka, listy składników i sposobu przygotowania.
2. ✅ Na ekranie szczegółów ma się pojawić przycisk FAB (floating action button), który będzie odpowiedzialny za wysłanie SMSa ze składnikami (w uproszczonej wersji działanie przycisku może prowadzić jedynie do wyświetlenia odpowiedniego komunikatu).
3. ✅ W aplikacji należy zastosować motywy.
4. ✅ W aplikacji należy korzystać z fragmentów (w przypadku stosowania Jetpack Compose nie wymagane).
5. ✅ Aplikacja ma działać poprawnie przy zmianie orientacji urządzenia.
6. ✅ Każda aktywność ma mieć pasek aplikacji w postaci paska narzędzi.
7. ✅ Ekran szczegółów ma być przewijany w pionie razem z paskiem aplikacji.
8. ✅ Na ekranie szczegółów obrazek ma się pojawić na pasku aplikacji, ale ma się razem z nim zwijać.
9. ✅ Przechodzenie pomiędzy kartami ma się odbywać także za pomocą gestu przeciągnięcia.
10. ✅ Do aplikacji należy dodać szufladę nawigacyjną.

### Wymagania dodatkowe

1. ✅ Kod w **Kotlinie**  
2. ✅ UI w **Jetpack Compose**  
3. ❌ Dane umieszczone **poza urządzeniem**  
4. ✅ Wykorzystanie motywów z biblioteki **Material Design**  
5. ✅ Dodanie do paska aplikacji **akcji**  
6. ✅ Dodanie do paska aplikacji opcji wyszukiwania przepisu zawierającego w nazwie i/lub opisie podany tekst.  
7. ✅ Opracowanie własnych ikon związanych z akcją.  
8. ✅ Opracowanie własnej ikony dla aplikacji  

<a id="animacje2"></a>
## Animacje

### Wymagania minimalne

1. ✅ Animacja ma się opierać na systemie animacji właściwości, czyli korzystać z obiektu ObjectAnimator

### Wymagania dodatkowe

1. ✅ Kod w **Kotlinie**  
2. ✅ Uruchomienie kilku animatorów równocześnie  
3. ❌ Animacja reagująca na działania wybranego sensora lub sensorów  
4. ✅ Ruchy obiektów zgodne z regułami biblioteki wzornictwa **Material Design**  
5. ❌ Ruchy obiektów zgodne z prawami fizyki  
6. ✅ Inne wg uznania - animacje przejść ekranów.
