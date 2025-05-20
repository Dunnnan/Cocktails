package com.example.koktajle.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.koktajle.models.Cocktail

@Composable
fun CocktailCardItem(cocktail: Cocktail, onCocktailClick: (String) -> Unit) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

    val imageSize = if (isLandscape) 200.dp else 300.dp
    val paddingSize = if (isLandscape) 16.dp else 8.dp

    Box(modifier = Modifier
        .fillMaxWidth()
        .height(imageSize+125.dp)
        .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(12.dp))
        .shadow(4.dp, ambientColor = MaterialTheme.colorScheme.primary , spotColor = MaterialTheme.colorScheme.primary)
        .clickable { onCocktailClick(cocktail.name) }
        .animateContentSize()
        .padding(paddingSize),
    ) {
        AsyncImage(
            model = cocktail.imageUrl,
            contentDescription = "Obrazek koktajlu",
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingSize)
                .size(imageSize)
                .clip(RoundedCornerShape(26.dp))
                .align(Alignment.TopCenter),
            contentScale = ContentScale.Crop
            )
        Text(
            textAlign = TextAlign.Center,
            text = cocktail.name,
            style = MaterialTheme.typography.headlineLarge.copy(),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        )
    }
}