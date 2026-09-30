package com.example.data.model

enum class RecipeCategory(val displayName: String, val iconName: String) {
    ALL("Tout", "restaurant"),
    TAJINES("Tajines", "soup_kitchen"),
    COUSCOUS("Couscous", "dinner_dining"),
    PASTILLAS("Pastillas & Briouates", "bakery_dining"),
    SOUPES_ENTREES("Soupes & Salades", "ramen_dining"),
    DESSERTS_THES("Pâtisseries & Thé", "coffee")
}

enum class RecipeDifficulty(val label: String) {
    EASY("Facile"),
    MEDIUM("Moyen"),
    ADVANCED("Festif & Avancé")
}
