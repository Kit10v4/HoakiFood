package com.hoaki.food.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "addresses")
data class Address(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val label: String, // Home, Work, Other
    val fullAddress: String,
    val city: String,
    val district: String,
    val ward: String,
    val isDefault: Boolean = false
)
