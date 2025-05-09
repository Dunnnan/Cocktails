package com.example.koktajle

import android.os.Bundle
import android.widget.GridLayout
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.koktajle.data.CocktailsBase
import com.example.koktajle.models.Cocktail
import com.example.koktajle.ui.theme.KoktajleTheme
import com.google.gson.Gson
import java.net.URLDecoder
import java.net.URLEncoder
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDrawerState
import androidx.compose.ui.input.key.Key.Companion.Home
import androidx.compose.ui.res.painterResource
import com.example.koktajle.components.DrawerContent
import com.example.koktajle.screens.CocktailDetailScreen
import com.example.koktajle.screens.CocktailList
import com.example.koktajle.screens.Home


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val darkTheme = remember { mutableStateOf(false) }

            KoktajleTheme(darkTheme = darkTheme.value) {
                CocktailApp(darkTheme)
            }
        }
    }
}

@Composable
fun CocktailApp(darkTheme: MutableState<Boolean>) {
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
                darkTheme = darkTheme.value
            )
        }
    ) {
        NavHost(navController = navController, startDestination = "home") {
            composable("home") {
                Home(
                    drawerState = drawerState,
                    scope = scope,
                    darkTheme = darkTheme
                )
            }

            composable("all") {
                CocktailList(
                    onCocktailClick = { cocktail ->
                        val encoded = URLEncoder.encode(gson.toJson(cocktail), "UTF-8")
                        navController.navigate("details/$encoded")
                    },
                    filter = { true },
                    darkTheme = darkTheme,
                    scope = scope,
                    drawerState = drawerState
                )
            }

            composable("vodka") {
                CocktailList(
                    onCocktailClick = { cocktail ->
                        val encoded = URLEncoder.encode(gson.toJson(cocktail), "UTF-8")
                        navController.navigate("details/$encoded")
                    },
                    filter = { it.ingredients.any { ing -> ing.contains("wódka", ignoreCase = true) } },
                    darkTheme = darkTheme,
                    scope = scope,
                    drawerState = drawerState
                )
            }

            composable(
                route = "details/{cocktail}",
                arguments = listOf(navArgument("cocktail") { type = NavType.StringType })
            ) { backStackEntry ->
                val json = backStackEntry.arguments?.getString("cocktail")?.let {
                    URLDecoder.decode(it, "UTF-8")
                }
                val cocktail = gson.fromJson(json, Cocktail::class.java)
                CocktailDetailScreen(cocktail, navController, darkTheme)
            }
        }
    }
}
