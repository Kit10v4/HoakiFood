package com.hoaki.food.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.hoaki.food.data.local.HoakiFoodDatabase
import com.hoaki.food.data.local.dao.*
import com.hoaki.food.data.preferences.UserPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): HoakiFoodDatabase {
        return Room.databaseBuilder(
            context,
            HoakiFoodDatabase::class.java,
            "hoakifood_database"
        ).fallbackToDestructiveMigration()
            .build()
    }
    
    @Provides
    @Singleton
    fun provideUserDao(database: HoakiFoodDatabase): UserDao {
        return database.userDao()
    }
    
    @Provides
    @Singleton
    fun provideCategoryDao(database: HoakiFoodDatabase): CategoryDao {
        return database.categoryDao()
    }
    
    @Provides
    @Singleton
    fun provideFoodDao(database: HoakiFoodDatabase): FoodDao {
        return database.foodDao()
    }
    
    @Provides
    @Singleton
    fun provideCartDao(database: HoakiFoodDatabase): CartDao {
        return database.cartDao()
    }
    
    @Provides
    @Singleton
    fun provideOrderDao(database: HoakiFoodDatabase): OrderDao {
        return database.orderDao()
    }

    @Provides
    @Singleton
    fun provideAddressDao(database: HoakiFoodDatabase): AddressDao {
        return database.addressDao()
    }
    
    @Provides
    @Singleton
    fun provideUserPreferences(@ApplicationContext context: Context): UserPreferences {
        return UserPreferences(context)
    }
    
    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }
}
