package com.example.koktajle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = cocktail.name,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .clickable { onCocktailClick(cocktail.name) }
                .padding(16.dp)
        )

        // Show the details if the cocktail is expanded
        if (expandedCocktail == cocktail.name) {
            CocktailDetail(cocktail)
        }
    }
}

@Composable
fun CocktailDetail(cocktail: Cocktail) {
    Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Składniki:", style = MaterialTheme.typography.bodyMedium)
        cocktail.ingredients.forEach { ingredient ->
            Text(text = "• $ingredient", style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Sposób przygotowania:", style = MaterialTheme.typography.bodyMedium)
        Text(text = cocktail.recipe, style = MaterialTheme.typography.bodyMedium)
    }
}
