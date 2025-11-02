package com.hoaki.food.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "foods")
data class Food(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String? = null,
    val categoryId: Long,
    val ingredients: String? = null,
    val rating: Float = 0f,
    val reviewCount: Int = 0,
    val isPopular: Boolean = false,
    val isFavorite: Boolean = false,
    val preparationTime: Int = 0, // in minutes
    val calories: Int = 0
)
