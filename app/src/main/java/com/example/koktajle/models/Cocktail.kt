package com.example.koktajle.models

data class Cocktail (
    val name: String,
    val ingredients: List<String>,
    val recipe: String,
    val imageUrl: String,
    val qualities: List<String>
)