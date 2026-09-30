package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_recipes")
data class FavoriteRecipeEntity(
    @PrimaryKey
    val recipeId: String,
    val addedAt: Long = System.currentTimeMillis()
)
