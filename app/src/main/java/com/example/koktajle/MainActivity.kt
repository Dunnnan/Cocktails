package com.example.koktajle

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.koktajle.components.DrawerContent
import com.example.koktajle.components.loadStartScreen
import com.example.koktajle.models.Cocktail
import com.example.koktajle.screens.CocktailDetailScreen
import com.example.koktajle.screens.CocktailList
import com.example.koktajle.screens.Home
import com.example.koktajle.screens.Info
import com.example.koktajle.ui.theme.KoktajleTheme
import com.google.gson.Gson
import java.net.URLDecoder
import java.net.URLEncoder

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        val startScreen = loadStartScreen(this)
        setContent {
            val isDarktheme = isSystemInDarkTheme()
            var darkTheme = rememberSaveable { mutableStateOf(isDarktheme) }
            KoktajleTheme(darkTheme = darkTheme.value) {
                CocktailApp(darkTheme, startScreen)
            }
        }
    }
}

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
            composable(
                "home",
                enterTransition = { return@composable fadeIn() + slideInVertically(initialOffsetY  = { 1000 }) },
                exitTransition = { return@composable fadeOut() },
                popExitTransition = { return@composable fadeOut() },
                popEnterTransition = { return@composable fadeIn() + slideInVertically(initialOffsetY  = { 1000 }) },
            )
            {
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
                    drawerState = drawerState,
                    navController = navController
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
                    drawerState = drawerState,
                    navController = navController
                )
            }

            composable("nonalcoholic",
            ) {
                CocktailList(
                    onCocktailClick = { cocktail ->
                        val encoded = URLEncoder.encode(gson.toJson(cocktail), "UTF-8")
                        navController.navigate("details/$encoded")
                    },
                    filter = { it.qualities.any { ing -> ing.contains("bezalkoholowy", ignoreCase = true) } },
                    darkTheme = darkTheme,
                    scope = scope,
                    drawerState = drawerState,
                    navController = navController
                )
            }

            composable(
                "info",
                enterTransition = { return@composable fadeIn() + slideInVertically(initialOffsetY  = { 1000 }) },
                exitTransition = { return@composable fadeOut() },
                popExitTransition = { return@composable fadeOut() },
                popEnterTransition = { return@composable fadeIn() + slideInVertically(initialOffsetY  = { 1000 }) },
            )
            {
                Info(
                    drawerState = drawerState,
                    scope = scope,
                    darkTheme = darkTheme
                )
            }

            composable(
                enterTransition = { return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(700)
                ) },
                popExitTransition = { return@composable slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End, tween(700)
                ) },

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
