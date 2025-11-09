package com.hoaki.food.data.repository

import com.hoaki.food.data.local.dao.FoodDao
import com.hoaki.food.data.model.Food
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoodRepository @Inject constructor(
    private val foodDao: FoodDao
) {
    fun getAllFoods(): Flow<List<Food>> = foodDao.getAllFoods()
    
    fun getFoodById(foodId: Long): Flow<Food?> = foodDao.getFoodById(foodId)
    
    fun getFoodsByCategory(categoryId: Long): Flow<List<Food>> = foodDao.getFoodsByCategory(categoryId)
    
    fun getPopularFoods(): Flow<List<Food>> = foodDao.getPopularFoods()

    fun getDiscountedFoods(): Flow<List<Food>> = foodDao.getDiscountedFoods()
    
    fun getFavoriteFoods(): Flow<List<Food>> = foodDao.getFavoriteFoods()
    
    fun searchFoods(query: String): Flow<List<Food>> = foodDao.searchFoods(query)
    
    suspend fun insertFoods(foods: List<Food>) {
        foodDao.insertFoods(foods)
    }
    
    suspend fun toggleFavorite(food: Food) {
        foodDao.updateFood(food.copy(isFavorite = !food.isFavorite))
    }
}
