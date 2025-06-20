package com.example.koktajle.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.koktajle.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerContent(
    navController: NavController,
    drawerState: DrawerState,
    scope: CoroutineScope,
    darkTheme: MutableState<Boolean>
) {
    val destinations =
        listOf(
            Triple(
                "Strona Główna",
                "home",
                if (darkTheme.value) R.drawable.ic_action_search_light else R.drawable.ic_action_search_dark
            ),
            Triple(
                "Wszystkie koktajle",
                "all",
                if (darkTheme.value) R.drawable.ic_action_all_dark else R.drawable.ic_action_all_light
            ),
            Triple(
                "Bezalkoholowe",
                "nonalcoholic",
                if (darkTheme.value) R.drawable.ic_action_nonalcoholic_light else R.drawable.ic_action_nonalcoholic_light
            ),
            Triple(
                "Z wódką",
                "vodka",
                if (darkTheme.value) R.drawable.ic_action_vodka_dark else R.drawable.ic_action_vodka_light
            ),
            Triple(
                "Informacje",
                "info",
                if (darkTheme.value) R.drawable.ic_action_settings_dark else R.drawable.ic_action_settings_light
            )
        )


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(8.dp)
    ) {
        destinations.forEach { (label, route, icon) ->
            val tintColor = if (darkTheme.value) Color.White else Color.Black
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        scope.launch {
                            drawerState.close()
                        }
                        navController.navigate(route)
                    }
                    .padding(12.dp)
            ) {
                Icon(
                    painter = painterResource(id = icon),
                    tint = tintColor,
                    contentDescription = "Szukaj",
                    modifier = Modifier.size(45.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))
                Text(text = label, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}