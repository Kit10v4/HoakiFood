package com.hoaki.food.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val foodId: Long,
    val foodName: String,
    val foodPrice: Double,
    val foodImageUrl: String? = null,
    val quantity: Int = 1,
    val note: String? = null,
    val userId: Long
)
