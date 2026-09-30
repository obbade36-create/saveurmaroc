package com.example.data.model

data class RecipeIngredient(
    val name: String,
    val baseAmount: Double, // amount for defaultServings
    val unit: String,       // "g", "ml", "c. à soupe", "c. à café", "pincée", "pièce(s)"
    val category: String    // "Épices & Aromates", "Boucherie & Volailles", "Fruits & Légumes", "Épicerie fine", "Boulangerie"
) {
    fun formattedAmount(servingsMultiplier: Double): String {
        val total = baseAmount * servingsMultiplier
        return if (total <= 0.0) {
            ""
        } else if (total % 1.0 == 0.0) {
            "${total.toInt()} $unit".trim()
        } else {
            "%.1f %s".format(total, unit).trim()
        }
    }
}

data class RecipeStep(
    val stepNumber: Int,
    val title: String,
    val instruction: String,
    val timerDurationMinutes: Int? = null,
    val chefTip: String? = null
)

data class Recipe(
    val id: String,
    val title: String,
    val arabicTitle: String,
    val category: RecipeCategory,
    val description: String,
    val culturalOrigin: String,
    val prepTimeMinutes: Int,
    val cookTimeMinutes: Int,
    val difficulty: RecipeDifficulty,
    val defaultServings: Int = 4,
    val calories: Int,
    val keySpices: List<String>,
    val ingredients: List<RecipeIngredient>,
    val steps: List<RecipeStep>,
    val videoTutorial: VideoTutorial
)
