package com.example.koktajle.components

import android.content.Context

// Funkcja do zapisu
fun saveStartScreen(context: Context, screen: String) {
    val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    prefs.edit().putString("start_screen", screen).apply()
}

// Funkcja do odczytu
fun loadStartScreen(context: Context): String {
    val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    return prefs.getString("start_screen", "home") ?: "home"
}
