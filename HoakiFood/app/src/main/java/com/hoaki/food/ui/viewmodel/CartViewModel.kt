package com.hoaki.food.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hoaki.food.data.model.CartItem
import com.hoaki.food.data.repository.CartRepository
import com.hoaki.food.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    
    private val currentUserId: StateFlow<Long?> = userRepository.getCurrentUserId()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    
    val cartItems: StateFlow<List<CartItem>> = currentUserId.flatMapLatest { userId ->
        if (userId != null) {
            cartRepository.getCartItems(userId)
        } else {
            flowOf(emptyList())
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    val cartItemCount: StateFlow<Int> = currentUserId.flatMapLatest { userId ->
        if (userId != null) {
            cartRepository.getCartItemCount(userId)
        } else {
            flowOf(0)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    
    val subtotal: StateFlow<Double> = cartItems.map { items ->
        items.sumOf { it.foodPrice * it.quantity }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)
    
    val deliveryFee: StateFlow<Double> = MutableStateFlow(15000.0)
    
    val total: StateFlow<Double> = combine(subtotal, deliveryFee) { sub, fee ->
        sub + fee
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)
    
    fun updateQuantity(cartItem: CartItem, quantity: Int) {
        viewModelScope.launch {
            cartRepository.updateCartItemQuantity(cartItem, quantity)
        }
    }
    
    fun removeItem(cartItem: CartItem) {
        viewModelScope.launch {
            cartRepository.removeFromCart(cartItem)
        }
    }
    
    fun clearCart() {
        viewModelScope.launch {
            currentUserId.value?.let { userId ->
                cartRepository.clearCart(userId)
            }
        }
    }
}
