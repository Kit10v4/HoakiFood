package com.hoaki.food.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hoaki.food.data.local.dao.*
import com.hoaki.food.data.model.*

@Database(
    entities = [
        User::class,
        Category::class,
        Food::class,
        CartItem::class,
        Order::class,
        Address::class
    ],
    version = 3, // Added Address entity
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class HoakiFoodDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun categoryDao(): CategoryDao
    abstract fun foodDao(): FoodDao
    abstract fun cartDao(): CartDao
    abstract fun orderDao(): OrderDao
    abstract fun addressDao(): AddressDao
}
