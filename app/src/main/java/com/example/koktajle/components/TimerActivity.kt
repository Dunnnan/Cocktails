package com.example.koktajle.components

    import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.sharp.Clear
import androidx.compose.material.icons.sharp.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

val buttonHeight = 60.dp;
    val buttonWidth = 80.dp;
    val paddingSize = 12.dp

    @Composable
    fun timerElement() {
        var minutes by rememberSaveable { mutableIntStateOf(0) }
        var seconds by rememberSaveable { mutableIntStateOf(0) }

        var isRunning by rememberSaveable { mutableStateOf(false) };
        var isPicking by rememberSaveable { mutableStateOf(false) }

        var coroutineScope = rememberCoroutineScope();

        LaunchedEffect(isRunning) {
            while (isRunning && (minutes > 0 || seconds > 0)) {
                delay(1000L)
                if (seconds - 1 < 0) {
                    seconds = 59;
                    minutes--;
                } else {
                    seconds--;
                }
            }
            isRunning = false;
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingSize)
        ) {

            if (seconds < 10) Text("$minutes:0$seconds", fontSize = 40.sp)
            else Text("$minutes:$seconds", fontSize = 40.sp)

            if (isPicking and !isRunning) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Minutes: ${minutes}")
                    Slider(
                        value = minutes.toFloat(),
                        onValueChange = { minutes = it.toInt() },
                        valueRange = 0f..59f,
                        steps = 59,
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )

                    Text("Seconds: ${seconds}")
                    Slider(
                        value = seconds.toFloat(),
                        onValueChange = { seconds = it.toInt() },
                        valueRange = 0f..59f,
                        steps = 59,
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier
                    .padding(paddingSize)
            ) {
                Button(
                    onClick = { isRunning = true; isPicking = false },
                    colors = ButtonDefaults.buttonColors(Color(165, 217, 171)),
                    modifier = Modifier
                        .height(buttonHeight)
                ) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = "Start"
                    )
                }

                Button(
                    onClick = { isRunning = false; isPicking = false },
                    colors = ButtonDefaults.buttonColors(Color(180, 49, 35)),
                    modifier = Modifier
                        .height(buttonHeight)
                ) {
                    Icon(
                        imageVector = Icons.Sharp.Clear,
                        contentDescription = "Stop"
                    )
                }

                Button(
                    onClick = { isRunning = false; minutes = 0; seconds = 0; isPicking = false },
                    colors = ButtonDefaults.buttonColors(Color.DarkGray),
                    modifier = Modifier
                        .height(buttonHeight)
                ) {
                    Icon(
                        imageVector = Icons.Sharp.Refresh,
                        contentDescription = "Stop"
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier
                    .padding(paddingSize)
            ) {
                Button(
                    onClick = { isPicking = !isPicking },
                    colors = ButtonDefaults.buttonColors(Color.DarkGray),
                    modifier = Modifier
                        .height(buttonHeight)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = "Start"
                    )
                }
            }
        }
    }