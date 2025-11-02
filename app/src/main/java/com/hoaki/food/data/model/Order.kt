package com.hoaki.food.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val orderNumber: String,
    val items: String, // JSON string of order items
    val subtotal: Double,
    val deliveryFee: Double,
    val total: Double,
    val status: OrderStatus = OrderStatus.PENDING,
    val deliveryAddress: String,
    val phoneNumber: String,
    val note: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

enum class OrderStatus {
    PENDING,
    CONFIRMED,
    PREPARING,
    DELIVERING,
    COMPLETED,
    CANCELLED
}
