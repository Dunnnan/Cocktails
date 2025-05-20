package com.example.koktajle.screens

import android.util.Log
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.koktajle.R
import com.example.koktajle.components.CocktailCardItem
import com.example.koktajle.data.CocktailsBase
import com.example.koktajle.models.Cocktail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CocktailList(onCocktailClick: (Cocktail) -> Unit, filter: (Cocktail) -> Boolean, darkTheme: MutableState<Boolean>, drawerState: DrawerState, scope: CoroutineScope, navController: NavController) {

    val searchQuery = remember { mutableStateOf("") }
    val screenRoutes = listOf("all", "nonalcoholic", "vodka")

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val currentIndex = screenRoutes.indexOf(currentRoute).takeIf { it >= 0 } ?: 0

    val tintColor = if (darkTheme.value) Color.White else Color.Black
    val icons = listOf(
        R.drawable.ic_action_all_light,
        R.drawable.ic_action_nonalcoholic_light,
        R.drawable.ic_action_vodka_light
    )


    LaunchedEffect(currentRoute, currentIndex) {
        Log.d("NavigationDebug", "Current route: $currentRoute")
        Log.d("NavigationDebug", "Current index: $currentIndex")
    }

    val cocktails = CocktailsBase.cocktails
        .filter(filter)
        .filter { it.name.contains(searchQuery.value, ignoreCase = true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    TextField(
                        value = searchQuery.value,
                        onValueChange = { searchQuery.value = it },
                        placeholder = {
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Wpisz koktajl");
                        },
                        singleLine = true,
                        modifier = Modifier
                            .width(280.dp)
                            .height(56.dp)
                    )

                },

                navigationIcon = {
                    val icon = icons.getOrNull(currentIndex) ?: R.drawable.ic_action_all_light
                    Icon(
                        painter = painterResource(id = icon),
                        tint = tintColor,
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
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 250.dp),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(padding)
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
        ) {
            items(cocktails) { cocktail ->
                CocktailCardItem(cocktail = cocktail, onCocktailClick = {
                    onCocktailClick(cocktail)
                })
            }
        }
    }
}