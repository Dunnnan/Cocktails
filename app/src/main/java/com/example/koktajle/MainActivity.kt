package com.example.koktajle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.koktajle.data.CocktailsBase
import com.example.koktajle.models.Cocktail

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
    CocktailList()
}

@Composable
fun CocktailList() {
    val cocktails = CocktailsBase.cocktails
    var expandedCocktail by remember { mutableStateOf<String?>(null) }

    LazyColumn {
        items(cocktails) { cocktail ->
            CocktailListItem(cocktail, expandedCocktail, onCocktailClick = {
                expandedCocktail = if (expandedCocktail == cocktail.name) null else cocktail.name
            })
        }
    }
}

@Composable
fun CocktailListItem(cocktail: Cocktail, expandedCocktail: String?, onCocktailClick: (String) -> Unit) {
    Spacer(modifier = Modifier.height(8.dp))
    Column(modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(12.dp))
        .shadow(4.dp)
        .clickable { onCocktailClick(cocktail.name) }
        .animateContentSize()
        .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = cocktail.imageUrl,
            contentDescription = "Obrazek koktajlu",
            modifier = Modifier
                .padding(8.dp)
                .size(300.dp)
                .clip(RoundedCornerShape(26.dp))
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            textAlign = TextAlign.Center,
            text = cocktail.name,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        if (expandedCocktail == cocktail.name) {
            CocktailDetail(cocktail)
        }
    }
}

@Composable
fun CocktailDetail(cocktail: Cocktail) {
    Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Składniki:", style = MaterialTheme.typography.headlineSmall)
        cocktail.ingredients.forEach { ingredient ->
            Text(text = "• $ingredient", style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Sposób przygotowania:", style = MaterialTheme.typography.headlineSmall)
        Text(text = cocktail.recipe, style = MaterialTheme.typography.bodyMedium)
    }
}
