package com.example.koktajle.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.koktajle.R
import com.example.koktajle.components.loadStartScreen
import com.example.koktajle.components.saveStartScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(darkTheme: MutableState<Boolean>, drawerState: DrawerState, scope: CoroutineScope) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Strona Główna",
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                navigationIcon = {
                        val tintColor = if (darkTheme.value) Color.White else Color.Black
                        Icon(
                            painter = painterResource(id = R.drawable.ic_action_search_dark),
                            tint = tintColor,
                            contentDescription = null,
                            modifier = Modifier.size(45.dp)
                        )
                },
                actions = {
                    IconButton(onClick = { darkTheme.value = !darkTheme.value }) {
                        val icon = if (darkTheme.value) R.drawable.ic_action_brightness_light else R.drawable.ic_action_brightness_dark
                        Icon(
                            painter = painterResource(id = icon),
                            contentDescription = "Przełącz motyw",
                            modifier = Modifier.size(45.dp)
                        )
                    }

                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        val icon = if (darkTheme.value) R.drawable.ic_action_hamburger_dark else R.drawable.ic_action_hamburger_light
                        Icon(
                            painter = painterResource(id = icon),
                            contentDescription = "Otwórz menu",
                            modifier = Modifier.size(45.dp)
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(
                textAlign = TextAlign.Center,
                text = "Witaj anonimowy alkoholiku!\nŻyczymy poszerzenia alkoholowych horyzontów :)",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            ElevatedButton(
                onClick = { scope.launch { drawerState.open() }},
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(R.drawable.home_button_icon)
                            .build(),
                        contentDescription = "Ikona koktajlu",
                        modifier = Modifier
                            .size(225.dp)
                            .padding(bottom = 8.dp),
                        contentScale = ContentScale.Fit
                    )
                    Text(text = "Rozpocznij przygodę z koktajlami")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            val currentStartScreen = remember { mutableStateOf(loadStartScreen(context)) }
            ElevatedButton(
                onClick = {
                    val newStartScreen = if (currentStartScreen.value == "home") "all" else "home"
                    saveStartScreen(context, newStartScreen)
                    currentStartScreen.value = newStartScreen
                },
                colors = if (currentStartScreen.value == "home") {
                    ButtonDefaults.elevatedButtonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                } else {
                    ButtonDefaults.elevatedButtonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                }
            ) {
                Text(
                    text = if (currentStartScreen.value == "home")
                        "Ustaw ekran startowy na katalog główny"
                    else
                        "Ustaw ekran startowy na stronę główną"
                )
            }

        }
    }
}
