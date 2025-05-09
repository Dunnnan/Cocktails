package com.example.koktajle.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.koktajle.R
import com.example.koktajle.models.Cocktail
import com.example.koktajle.components.timerElement

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CocktailDetailScreen(cocktail: Cocktail, navController: NavController, darkTheme: MutableState<Boolean>) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = cocktail.name)
                    AsyncImage(
                        model = cocktail.imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .height(75.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        val icon = if (darkTheme.value) R.drawable.ic_action_back_dark else R.drawable.ic_action_back_light

                        Icon(
                            painter = painterResource(id = icon),
                            contentDescription = "Search",
                            modifier = Modifier.size(45.dp)
                        )
                    }
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
                },
            )
        },

        floatingActionButton = {
            FloatingActionButton(onClick = {
                Toast.makeText(context, "Składniki: \n ${cocktail.ingredients.joinToString()}", Toast.LENGTH_SHORT).show()
            }) {
                val icon = if (darkTheme.value) R.drawable.ic_action_fab_dark else R.drawable.ic_action_fab_light

                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    modifier = Modifier.size(45.dp)
                )
            }
        },

        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                val configuration = LocalConfiguration.current
                val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

                val headerSize = if (isLandscape) 40.sp else 28.sp
                val textSize = if (isLandscape) 32.sp else 20.sp

//                AsyncImage(
//                    model = cocktail.imageUrl,
//                    contentDescription = null,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(240.dp)
//                        .clip(RoundedCornerShape(16.dp))
//                )

                timerElement() // Upewnij się, że masz tę funkcję!

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
