package com.hoaki.food.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hoaki.food.data.model.CartItem
import com.hoaki.food.data.model.Order
import com.hoaki.food.data.model.OrderStatus
import com.hoaki.food.data.repository.OrderRepository
import com.hoaki.food.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    
    private val currentUserId: StateFlow<Long?> = userRepository.getCurrentUserId()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    
    val orders: StateFlow<List<Order>> = currentUserId.flatMapLatest { userId ->
        if (userId != null) {
            orderRepository.getUserOrders(userId)
        } else {
            flowOf(emptyList())
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    private val _checkoutState = MutableStateFlow<CheckoutState>(CheckoutState.Idle)
    val checkoutState: StateFlow<CheckoutState> = _checkoutState.asStateFlow()
    
    fun createOrder(
        cartItems: List<CartItem>,
        deliveryAddress: String,
        phoneNumber: String,
        note: String? = null
    ) {
        viewModelScope.launch {
            val userId = currentUserId.value
            if (userId != null) {
                _checkoutState.value = CheckoutState.Loading
                val result = orderRepository.createOrder(
                    userId = userId,
                    cartItems = cartItems,
                    deliveryAddress = deliveryAddress,
                    phoneNumber = phoneNumber,
                    note = note
                )
                _checkoutState.value = if (result.isSuccess) {
                    CheckoutState.Success(result.getOrNull()!!)
                } else {
                    CheckoutState.Error(result.exceptionOrNull()?.message ?: "Đặt hàng thất bại")
                }
            } else {
                _checkoutState.value = CheckoutState.Error("Vui lòng đăng nhập để đặt hàng")
            }
        }
    }
    
    fun updateOrderStatus(orderId: Long, status: OrderStatus) {
        viewModelScope.launch {
            orderRepository.updateOrderStatus(orderId, status)
        }
    }
    
    fun cancelOrder(orderId: Long) {
        viewModelScope.launch {
            orderRepository.cancelOrder(orderId)
        }
    }
    
    fun resetCheckoutState() {
        _checkoutState.value = CheckoutState.Idle
    }
}

sealed class CheckoutState {
    object Idle : CheckoutState()
    object Loading : CheckoutState()
    data class Success(val order: Order) : CheckoutState()
    data class Error(val message: String) : CheckoutState()
}
