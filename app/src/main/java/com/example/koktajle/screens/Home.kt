package com.example.koktajle.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.koktajle.R
import com.example.koktajle.data.CocktailsBase
import com.example.koktajle.models.Cocktail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(darkTheme: MutableState<Boolean>, drawerState: DrawerState, scope: CoroutineScope) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Strona Główna")
                        },

                navigationIcon = {
                    val icon = if (darkTheme.value) R.drawable.ic_action_search_light else R.drawable.ic_action_search_dark
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = "Szukaj",
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
        Text(
            text = "Witaj anonimowy alhoholiku!\nŻyczymy poszerzenia alkoholowych horyzontów :)",
            modifier = Modifier.padding(padding).padding(16.dp)
        )
    }
}