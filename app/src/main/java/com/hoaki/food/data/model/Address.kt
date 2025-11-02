package com.hoaki.food.data.model

data class Address(
    val id: Long = 0,
    val userId: Long,
    val label: String, // Home, Work, Other
    val fullAddress: String,
    val city: String,
    val district: String,
    val ward: String,
    val isDefault: Boolean = false
)
