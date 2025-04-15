package com.example.koktajle

import android.os.Bundle
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
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
import com.google.gson.Gson
import java.net.URLDecoder
import java.net.URLEncoder

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CocktailApp()
        }
    }
}

@Composable
fun CocktailApp() {
    val navController = rememberNavController()
    val gson = Gson()

    NavHost(navController = navController, startDestination = "list") {
        composable("list") {
            CocktailList(onCocktailClick = { cocktail ->
                val encoded = URLEncoder.encode(gson.toJson(cocktail), "UTF-8")
                navController.navigate("details/$encoded")
            })
        }

        composable(
            route = "details/{cocktail}",
            arguments = listOf(navArgument("cocktail") { type = NavType.StringType })
        ) { backStackEntry ->
            val json = backStackEntry.arguments?.getString("cocktail")?.let { URLDecoder.decode(it, "UTF-8") }
            val cocktail = gson.fromJson(json, Cocktail::class.java)
            CocktailDetailScreen(cocktail, navController)
        }
    }
}

@Composable
fun CocktailList(onCocktailClick: (Cocktail) -> Unit) {
    val cocktails = CocktailsBase.cocktails

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 160.dp),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(cocktails) { cocktail ->
            CocktailCardItem(cocktail = cocktail, onCocktailClick = {
                onCocktailClick(cocktail)
            })
        }
    }
}

@Composable
fun CocktailCardItem(cocktail: Cocktail, onCocktailClick: (String) -> Unit) {
    Spacer(modifier = Modifier.height(8.dp))
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

    val imageSize = if (isLandscape) 450.dp else 300.dp
    val paddingSize = if (isLandscape) 16.dp else 8.dp
    val textSize = if (isLandscape) 48.sp else 30.sp

    Column(modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(12.dp))
        .shadow(4.dp)
        .clickable { onCocktailClick(cocktail.name) }
        .animateContentSize()
        .padding(paddingSize),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = cocktail.imageUrl,
            contentDescription = "Obrazek koktajlu",
            modifier = Modifier
                .padding(paddingSize)
                .size(imageSize)
                .clip(RoundedCornerShape(26.dp))
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            textAlign = TextAlign.Center,
            text = cocktail.name,
            style = MaterialTheme.typography.headlineLarge.copy(fontSize = textSize),
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingSize*2)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CocktailDetailScreen(cocktail: Cocktail, navController: NavController) {

    val context = LocalContext.current

    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text(text = cocktail.name)},
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },

        floatingActionButton = {
            FloatingActionButton(onClick = {
                Toast.makeText(context, "Składniki: \n ${cocktail.ingredients.joinToString()}", Toast.LENGTH_SHORT).show()
            }) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send SMS")
            }
        },

        content = { paddingValues ->

            Column(modifier = Modifier
                .padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(8.dp))


                val configuration = LocalConfiguration.current
                val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

                val headerSize = if (isLandscape) 40.sp else 28.sp
                val textSize = if (isLandscape) 32.sp else 20.sp

                AsyncImage(
                    model = cocktail.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .clip(RoundedCornerShape(16.dp))
                )

                timerElement();

                Text(text = "Składniki:", style = MaterialTheme.typography.headlineSmall.copy(fontSize = headerSize))
                cocktail.ingredients.forEach { ingredient ->
                    Text(text = "• $ingredient", style = MaterialTheme.typography.bodyMedium.copy(fontSize = textSize))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Sposób przygotowania:", style = MaterialTheme.typography.headlineSmall.copy(fontSize = headerSize))
                Text(text = cocktail.recipe, style = MaterialTheme.typography.bodyMedium.copy(fontSize = textSize))
            }
        }
    )
}
