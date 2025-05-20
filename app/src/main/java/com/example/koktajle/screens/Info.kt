package com.example.koktajle.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.koktajle.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Info(
    darkTheme: MutableState<Boolean>,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    val context = LocalContext.current
    val tintColor = if (darkTheme.value) Color.White else Color.Black
    val scrollState = rememberScrollState()


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Informacje")
                },

                navigationIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_action_settings_light),
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
                            contentDescription = "Przełącz motyw",
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
                .padding(45.dp)
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.padding(32.dp))

            // Ikona wyszukiwania
            Row() {
                Icon(
                    painter = painterResource(id = R.drawable.ic_action_search_dark),
                    tint = tintColor,
                    contentDescription = null,
                    modifier = Modifier.size(45.dp)
                )
                Text(
                    textAlign = TextAlign.Justify,
                    text = "Ikonka strony domowej.Informuje też o elemencie służącym do wyszukiwania.",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Ikona hamburgera
            Row() {
                Icon(
                    painter = painterResource(id = R.drawable.ic_action_hamburger_dark),
                    tint = tintColor,
                    contentDescription = null,
                    modifier = Modifier.size(45.dp)
                )
                Text(
                    textAlign = TextAlign.Justify,
                    text = "Akcja służąca do rozwinięcia szuflady nawigującej.",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Ikony zmiany motywu
            Row() {
                Icon(
                    painter = painterResource(id = R.drawable.ic_action_brightness_dark),
                    tint = tintColor,
                    contentDescription = null,
                    modifier = Modifier.size(45.dp)
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_action_brightness_light),
                    tint = tintColor,
                    contentDescription = null,
                    modifier = Modifier.size(45.dp)
                )
                Text(
                    textAlign = TextAlign.Justify,
                    text = "Akcje służące do zmiany motywu aplikacji.",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Ikona wódki(40%)
            Row() {
                Icon(
                    painter = painterResource(id = R.drawable.ic_action_vodka_dark),
                    tint = tintColor,
                    contentDescription = null,
                    modifier = Modifier.size(45.dp),
                )
                Text(
                    textAlign = TextAlign.Justify,
                    text = "Akcja służąca do przejścia do ekranu pokazującego tylko koktajle z zawartością wódki.",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Ikona butelki z mlekiem
            Row() {
                Icon(
                    painter = painterResource(id = R.drawable.ic_action_all_light),
                    tint = tintColor,
                    contentDescription = null,
                    modifier = Modifier.size(45.dp),
                )
                Text(
                    textAlign = TextAlign.Justify,
                    text = "Akcja służąca do przejścia do ekranu pokazującego tylko koktajle bezalkoholowe.",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Ikona koktajlu (w lewo)
            Row() {
                Icon(
                    painter = painterResource(id = R.drawable.ic_action_back_light),
                    tint = tintColor,
                    contentDescription = null,
                    modifier = Modifier.size(45.dp)
                )
                Text(
                    textAlign = TextAlign.Justify,
                    text = "Akcja służąca do przejścia do ekranu poprzedniego.",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Ikona koktajlu (w prawo)
            Row() {
                Icon(
                    painter = painterResource(id = R.drawable.ic_action_fab_dark),
                    tint = tintColor,
                    contentDescription = null,
                    modifier = Modifier.size(45.dp)
                )
                Text(
                    textAlign = TextAlign.Justify,
                    text = "Akcja służąca do wyświetlenia listy składników danego przepisu. (uproszczony sms)",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Ikona zębatki
            Row() {
                Icon(
                    painter = painterResource(id = R.drawable.ic_action_settings_dark),
                    tint = tintColor,
                    contentDescription = null,
                    modifier = Modifier.size(45.dp)
                )
                Text(
                    textAlign = TextAlign.Justify,
                    text = "Akcja służąca do przejścia do ekranu pokazującego informacje na temat aplikacji.",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

        }
    }
}