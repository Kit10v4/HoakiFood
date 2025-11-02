package com.hoaki.food.data.local.dao

import androidx.room.*
import com.hoaki.food.data.model.Food
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {
    @Query("SELECT * FROM foods")
    fun getAllFoods(): Flow<List<Food>>
    
    @Query("SELECT * FROM foods WHERE id = :foodId")
    fun getFoodById(foodId: Long): Flow<Food?>
    
    @Query("SELECT * FROM foods WHERE categoryId = :categoryId")
    fun getFoodsByCategory(categoryId: Long): Flow<List<Food>>
    
    @Query("SELECT * FROM foods WHERE isPopular = 1 ORDER BY rating DESC LIMIT 10")
    fun getPopularFoods(): Flow<List<Food>>
    
    @Query("SELECT * FROM foods WHERE isFavorite = 1")
    fun getFavoriteFoods(): Flow<List<Food>>
    
    @Query("SELECT * FROM foods WHERE name LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%'")
    fun searchFoods(query: String): Flow<List<Food>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFood(food: Food)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoods(foods: List<Food>)
    
    @Update
    suspend fun updateFood(food: Food)
    
    @Delete
    suspend fun deleteFood(food: Food)
    
    @Query("DELETE FROM foods")
    suspend fun deleteAllFoods()
}
