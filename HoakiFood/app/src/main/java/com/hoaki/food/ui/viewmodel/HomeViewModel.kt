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
            Food(
                id = 1, 
                name = "Phở bò", 
                description = "Phở bò truyền thống Hà Nội", 
                price = 45000.0,
                imageUrl = "android.resource://com.hoaki.food/drawable/phobotai",
                categoryId = 1, 
                rating = 4.5f, 
                reviewCount = 120, 
                isPopular = true, 
                preparationTime = 20,
                discount = 0
            ),
            Food(
                id = 2, 
                name = "Bún chả", 
                description = "Bún chả Hà Nội đặc biệt", 
                price = 40000.0,
                imageUrl = "android.resource://com.hoaki.food/drawable/buncha",
                categoryId = 1, 
                rating = 4.7f, 
                reviewCount = 95, 
                isPopular = true, 
                preparationTime = 25,
                discount = 12  // 12% off - Deal sốc
            ),
            Food(
                id = 3, 
                name = "Cơm tấm", 
                description = "Cơm tấm sườn bì chả", 
                price = 35000.0,
                imageUrl = "android.resource://com.hoaki.food/drawable/comgaxoimo",
                categoryId = 1, 
                rating = 4.3f, 
                reviewCount = 80, 
                isPopular = true, 
                preparationTime = 15,
                discount = 8   // 8% off - Deal sốc
            ),
            Food(
                id = 4, 
                name = "Bánh mì", 
                description = "Bánh mì thịt nguội pate", 
                price = 20000.0,
                imageUrl = "android.resource://com.hoaki.food/drawable/banhmithitnuong",
                categoryId = 1, 
                rating = 4.6f, 
                reviewCount = 150, 
                isPopular = true, 
                preparationTime = 10,
                discount = 0
            ),
            Food(
                id = 5, 
                name = "Bún đậu mắm tôm", 
                description = "Bún đậu mắm tôm nguyên liệu tươi", 
                price = 89000.0,
                imageUrl = "android.resource://com.hoaki.food/drawable/bundaumamtom",
                categoryId = 1, 
                rating = 4.4f, 
                reviewCount = 60, 
                isPopular = true, 
                preparationTime = 15,
                discount = 16  // 16% off - Deal sốc
            ),
            Food(
                id = 6, 
                name = "Cháo lòng", 
                description = "Cháo lòng nóng hổi", 
                price = 30000.0,
                imageUrl = "android.resource://com.hoaki.food/drawable/chaolong",
                categoryId = 1, 
                rating = 4.5f, 
                reviewCount = 70, 
                isPopular = false, 
                preparationTime = 20,
                discount = 0
            ),
            Food(
                id = 7, 
                name = "Hủ tiếu", 
                description = "Hủ tiếu Nam Vang đặc biệt", 
                price = 38000.0,
                imageUrl = "android.resource://com.hoaki.food/drawable/hutieu",
                categoryId = 1, 
                rating = 4.8f, 
                reviewCount = 200, 
                isPopular = true, 
                preparationTime = 20,
                discount = 0
            ),
            Food(
                id = 8, 
                name = "Cơm chiên dương châu", 
                description = "Cơm chiên dương châu hải sản", 
                price = 45000.0,
                imageUrl = "android.resource://com.hoaki.food/drawable/comtronhanquoc",
                categoryId = 1, 
                rating = 4.7f, 
                reviewCount = 180, 
                isPopular = true, 
                preparationTime = 15,
                discount = 0
            ),
            Food(
                id = 9, 
                name = "Trà sữa trân châu", 
                description = "Trà sữa trân châu đường đen", 
                price = 30000.0,
                imageUrl = "android.resource://com.hoaki.food/drawable/thucuong",
                categoryId = 3, 
                rating = 4.6f, 
                reviewCount = 90, 
                isPopular = false, 
                preparationTime = 5,
                discount = 0
            ),
            Food(
                id = 10, 
                name = "Ăn vặt", 
                description = "Combo ăn vặt đa dạng", 
                price = 50000.0,
                imageUrl = "android.resource://com.hoaki.food/drawable/anvat",
                categoryId = 5, 
                rating = 4.4f, 
                reviewCount = 75, 
                isPopular = false, 
                preparationTime = 10,
                discount = 0
            )
        )
        foodRepository.insertFoods(sampleFoods)
    }
}
