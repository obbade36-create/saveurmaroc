package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_items")
data class ShoppingItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val amount: String,
    val category: String, // "Épices & Aromates", "Boucherie & Volailles", "Fruits & Légumes", "Épicerie fine", "Boulangerie"
    val isChecked: Boolean = false,
    val recipeTitle: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)
