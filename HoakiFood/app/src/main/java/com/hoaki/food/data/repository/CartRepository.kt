package com.hoaki.food.data.repository

import com.hoaki.food.data.local.dao.CartDao
import com.hoaki.food.data.model.CartItem
import com.hoaki.food.data.model.Food
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepository @Inject constructor(
    private val cartDao: CartDao
) {
    fun getCartItems(userId: Long): Flow<List<CartItem>> = cartDao.getCartItems(userId)
    
    fun getCartItemCount(userId: Long): Flow<Int> = cartDao.getCartItemCount(userId)
    
    suspend fun addToCart(userId: Long, food: Food, quantity: Int = 1): Result<Unit> {
        return try {
            val existingItem = cartDao.getCartItem(userId, food.id)
            if (existingItem != null) {
                // Update quantity if item already exists
                val updatedItem = existingItem.copy(quantity = existingItem.quantity + quantity)
                cartDao.updateCartItem(updatedItem)
            } else {
                // Add new item
                val cartItem = CartItem(
                    foodId = food.id,
                    foodName = food.name,
                    foodPrice = food.price,
                    foodImageUrl = food.imageUrl,
                    quantity = quantity,
                    userId = userId
                )
                cartDao.insertCartItem(cartItem)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateCartItemQuantity(cartItem: CartItem, quantity: Int) {
        if (quantity <= 0) {
            cartDao.deleteCartItem(cartItem)
        } else {
            val updatedItem = cartItem.copy(quantity = quantity)
            cartDao.updateCartItem(updatedItem)
        }
    }
    
    suspend fun removeFromCart(cartItem: CartItem) = cartDao.deleteCartItem(cartItem)
    
    suspend fun clearCart(userId: Long) = cartDao.clearCart(userId)
}
