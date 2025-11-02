package com.hoaki.food.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hoaki.food.data.model.Food
import com.hoaki.food.data.repository.CartRepository
import com.hoaki.food.data.repository.FoodRepository
import com.hoaki.food.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodDetailViewModel @Inject constructor(
    private val foodRepository: FoodRepository,
    private val cartRepository: CartRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    
    private val _foodId = MutableStateFlow<Long?>(null)
    
    val food: StateFlow<Food?> = _foodId.flatMapLatest { id ->
        if (id != null) {
            foodRepository.getFoodById(id)
        } else {
            flowOf(null)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    
    private val _quantity = MutableStateFlow(1)
    val quantity: StateFlow<Int> = _quantity.asStateFlow()
    
    private val _addToCartState = MutableStateFlow<AddToCartState>(AddToCartState.Idle)
    val addToCartState: StateFlow<AddToCartState> = _addToCartState.asStateFlow()
    
    private val currentUserId: StateFlow<Long?> = userRepository.getCurrentUserId()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    
    fun setFoodId(foodId: Long) {
        _foodId.value = foodId
    }
    
    fun increaseQuantity() {
        _quantity.value += 1
    }
    
    fun decreaseQuantity() {
        if (_quantity.value > 1) {
            _quantity.value -= 1
        }
    }
    
    fun addToCart() {
        viewModelScope.launch {
            val foodItem = food.value
            val userId = currentUserId.value
            
            if (foodItem != null && userId != null) {
                _addToCartState.value = AddToCartState.Loading
                val result = cartRepository.addToCart(userId, foodItem, quantity.value)
                _addToCartState.value = if (result.isSuccess) {
                    AddToCartState.Success
                } else {
                    AddToCartState.Error(result.exceptionOrNull()?.message ?: "Thêm vào giỏ hàng thất bại")
                }
            } else {
                _addToCartState.value = AddToCartState.Error("Vui lòng đăng nhập để thêm vào giỏ hàng")
            }
        }
    }
    
    fun toggleFavorite() {
        viewModelScope.launch {
            food.value?.let { foodRepository.toggleFavorite(it) }
        }
    }
    
    fun resetAddToCartState() {
        _addToCartState.value = AddToCartState.Idle
    }
}

sealed class AddToCartState {
    object Idle : AddToCartState()
    object Loading : AddToCartState()
    object Success : AddToCartState()
    data class Error(val message: String) : AddToCartState()
}
