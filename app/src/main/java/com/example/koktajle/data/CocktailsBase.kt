package com.example.koktajle.data

import com.example.koktajle.models.Cocktail

object CocktailsBase {
    val cocktails = listOf(
        Cocktail(
            name = "Mojito",
            ingredients = listOf("Rum", "Cukier", "Limonka", "Mięta", "Woda gazowana"),
            recipe = "Połącz rum, cukier, miętę, limonkę, dodaj wodę gazowaną i lód.",
            imageUrl = "https://ms-fox.pl/wp-content/uploads/2022/02/mojito-1300x1294.jpg"
        ),
        Cocktail(
            name = "Martini",
            ingredients = listOf("Gin", "Wermut", "Lód"),
            recipe = "Wymieszaj gin z wermutem w szklance z lodem, odcedź do kieliszka.",
            imageUrl ="https://krosno.com.pl/media/catalog/product/cache/e93b1ff2537986ecbd15357d4354a611/5/7/57_c735_0170_0e0_0204_00a.jpg"
        ),
        Cocktail(
            name = "Piña Colada",
            ingredients = listOf("Rum", "Sok ananasowy", "Mleczko kokosowe", "Lód"),
            recipe = "Zblenduj rum, sok ananasowy, mleczko kokosowe i lód.",
            imageUrl = "https://www.quoregelato.com/wp-content/uploads/2024/04/Quore_WebsitePic_PinaColada_Cocktail2_1200.jpg"
        ),
        Cocktail(
            name = "Cosmopolitan",
            ingredients = listOf("Wódka", "Cointreau", "Sok żurawinowy", "Limonka"),
            recipe = "Wymieszaj składniki w shakerze z lodem, odcedź do kieliszka.",
            imageUrl = "https://cdn.aniagotuje.com/pictures/articles/2023/07/45574135-v-1500x1500.jpg"
        ),
        Cocktail(
            name = "Tequila Sunrise",
            ingredients = listOf("Tequila", "Sok pomarańczowy", "Grenadyna"),
            recipe = "Wlej tequilę, następnie sok pomarańczowy. Na koniec powoli wlej grenadynę.",
            imageUrl = "https://images.food52.com/eugI6wPobQHqEMP9vQZG3AvQAIk=/1200x1200/8ef2cc65-724c-433c-b374-297e8eb1a4be--2020-0707_tequila-sunrise-cocktail_3x2_ty-mecham.jpg"
        ),
        Cocktail(
            name = "Old Fashioned",
            ingredients = listOf("Bourbon", "Cukier", "Angostura Bitters", "Woda", "Skórka pomarańczy"),
            recipe = "Rozpuść cukier z angosturą i wodą, dodaj bourbon, wymieszaj i udekoruj skórką pomarańczy.",
            imageUrl = "https://www.vindulge.com/wp-content/uploads/2024/02/Smoked-Old-Fashioned-FI.jpg"
        ),
        Cocktail(
            name = "Negroni",
            ingredients = listOf("Gin", "Wermut czerwony", "Campari"),
            recipe = "Wymieszaj wszystkie składniki w szklance z lodem i udekoruj skórką pomarańczy.",
            imageUrl = "https://www.liquor.com/thmb/cv0NfK7-V2iGs1gdHbnOTMqOwT0=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/neighborhood-negroni-720x720-primary-727f7dc3a5d04a298d63977679efe856.jpg"
        )
    )

    fun getCocktailByName(name: String) : Cocktail? {
        return cocktails.find { it.name == name }
    }
}

