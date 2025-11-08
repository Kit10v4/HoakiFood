package com.hoaki.food.data.repository

import com.hoaki.food.data.local.dao.CategoryDao
import com.hoaki.food.data.model.Category
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepository @Inject constructor(
    private val categoryDao: CategoryDao
) {
    fun getAllCategories(): Flow<List<Category>> = categoryDao.getAllCategories()
    
    fun getCategoryById(categoryId: Long): Flow<Category?> = categoryDao.getCategoryById(categoryId)
    
    suspend fun getCategoryCount(): Int = categoryDao.getCategoryCount()
    
    suspend fun insertCategory(category: Category) = categoryDao.insertCategory(category)
    
    suspend fun insertCategories(categories: List<Category>) = categoryDao.insertCategories(categories)
    
    suspend fun updateCategory(category: Category) = categoryDao.updateCategory(category)
    
    suspend fun deleteCategory(category: Category) = categoryDao.deleteCategory(category)
}
