package com.hoaki.food.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hoaki.food.data.model.Category
import com.hoaki.food.data.model.Food
import com.hoaki.food.data.repository.CategoryRepository
import com.hoaki.food.data.repository.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val foodRepository: FoodRepository
) : ViewModel() {
    
    val categories: StateFlow<List<Category>> = categoryRepository.getAllCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    val popularFoods: StateFlow<List<Food>> = foodRepository.getPopularFoods()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    val allFoods: StateFlow<List<Food>> = foodRepository.getAllFoods()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    private val _selectedCategoryId = MutableStateFlow<Long?>(null)
    val selectedCategoryId: StateFlow<Long?> = _selectedCategoryId.asStateFlow()
    
    val foodsByCategory: StateFlow<List<Food>> = _selectedCategoryId.flatMapLatest { categoryId ->
        if (categoryId != null) {
            foodRepository.getFoodsByCategory(categoryId)
        } else {
            flowOf(emptyList())
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    init {
        initializeSampleData()
    }
    
    fun selectCategory(categoryId: Long?) {
        _selectedCategoryId.value = categoryId
    }
    
    private fun initializeSampleData() {
        viewModelScope.launch {
            // Check database directly to avoid race condition with Flow
            val categoryCount = categoryRepository.getCategoryCount()
            if (categoryCount == 0) {
                insertSampleCategories()
                insertSampleFoods()
            }
        }
    }
    
    private suspend fun insertSampleCategories() {
        val sampleCategories = listOf(
            Category(id = 1, name = "Món chính", displayOrder = 1),
            Category(id = 2, name = "Món phụ", displayOrder = 2),
            Category(id = 3, name = "Đồ uống", displayOrder = 3),
            Category(id = 4, name = "Tráng miệng", displayOrder = 4),
            Category(id = 5, name = "Món ăn vặt", displayOrder = 5)
        )
        categoryRepository.insertCategories(sampleCategories)
    }
    
    private suspend fun insertSampleFoods() {
        val sampleFoods = listOf(
            Food(id = 1, name = "Phở bò", description = "Phở bò truyền thống Hà Nội", price = 45000.0, 
                categoryId = 1, rating = 4.5f, reviewCount = 120, isPopular = true, preparationTime = 20),
            Food(id = 2, name = "Bún chả", description = "Bún chả Hà Nội đặc biệt", price = 40000.0,
                categoryId = 1, rating = 4.7f, reviewCount = 95, isPopular = true, preparationTime = 25),
            Food(id = 3, name = "Cơm tấm", description = "Cơm tấm sườn bì chả", price = 35000.0,
                categoryId = 1, rating = 4.3f, reviewCount = 80, isPopular = true, preparationTime = 15),
            Food(id = 4, name = "Bánh mì", description = "Bánh mì thịt nguội pate", price = 20000.0,
                categoryId = 1, rating = 4.6f, reviewCount = 150, isPopular = true, preparationTime = 10),
            Food(id = 5, name = "Gỏi cuốn", description = "Gỏi cuốn tôm thịt", price = 25000.0,
                categoryId = 2, rating = 4.4f, reviewCount = 60, isPopular = false, preparationTime = 15),
            Food(id = 6, name = "Nem rán", description = "Nem rán giòn tan", price = 30000.0,
                categoryId = 2, rating = 4.5f, reviewCount = 70, isPopular = true, preparationTime = 20),
            Food(id = 7, name = "Trà đào cam sả", description = "Trá đào cam sả mát lạnh", price = 25000.0,
                categoryId = 3, rating = 4.8f, reviewCount = 200, isPopular = true, preparationTime = 5),
            Food(id = 8, name = "Cà phê sữa đá", description = "Cà phê phin truyền thống", price = 20000.0,
                categoryId = 3, rating = 4.7f, reviewCount = 180, isPopular = true, preparationTime = 5),
            Food(id = 9, name = "Sinh tố bơ", description = "Sinh tố bơ sánh mịn", price = 30000.0,
                categoryId = 3, rating = 4.6f, reviewCount = 90, isPopular = false, preparationTime = 5),
            Food(id = 10, name = "Chè ba màu", description = "Chè ba màu truyền thống", price = 20000.0,
                categoryId = 4, rating = 4.4f, reviewCount = 75, isPopular = false, preparationTime = 10)
        )
        foodRepository.insertFoods(sampleFoods)
    }
}
