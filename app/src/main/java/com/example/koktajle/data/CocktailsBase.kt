package com.example.koktajle.data

import com.example.koktajle.models.Cocktail

object CocktailsBase {
    val cocktails = listOf(
        Cocktail(
            name = "Mojito",
            ingredients = listOf("Rum", "Cukier", "Limonka", "Mięta", "Woda gazowana"),
            recipe = "Połącz rum, cukier, miętę, limonkę, dodaj wodę gazowaną i lód."
        ),
        Cocktail(
            name = "Martini",
            ingredients = listOf("Gin", "Wermut", "Lód"),
            recipe = "Wymieszaj gin z wermutem w szklance z lodem, odcedź do kieliszka."
        )
    )

    fun getCocktailByName(name: String) : Cocktail? {
        return cocktails.find { it.name == name }
    }
}

