package com.hoaki.food.data.repository

import com.hoaki.food.data.local.dao.OrderDao
import com.hoaki.food.data.model.CartItem
import com.hoaki.food.data.model.Order
import com.hoaki.food.data.model.OrderStatus
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepository @Inject constructor(
    private val orderDao: OrderDao,
    private val cartRepository: CartRepository,
    private val gson: Gson
) {
    fun getUserOrders(userId: Long): Flow<List<Order>> = orderDao.getUserOrders(userId)
    
    fun getOrderById(orderId: Long): Flow<Order?> = orderDao.getOrderById(orderId)
    
    fun getOrdersByStatus(userId: Long, status: OrderStatus): Flow<List<Order>> = 
        orderDao.getOrdersByStatus(userId, status)
    
    suspend fun createOrder(
        userId: Long,
        cartItems: List<CartItem>,
        deliveryAddress: String,
        phoneNumber: String,
        deliveryFee: Double = 15000.0,
        note: String? = null
    ): Result<Order> {
        return try {
            val subtotal = cartItems.sumOf { it.foodPrice * it.quantity }
            val total = subtotal + deliveryFee
            val orderNumber = generateOrderNumber()
            val itemsJson = gson.toJson(cartItems)
            
            val order = Order(
                userId = userId,
                orderNumber = orderNumber,
                items = itemsJson,
                subtotal = subtotal,
                deliveryFee = deliveryFee,
                total = total,
                deliveryAddress = deliveryAddress,
                phoneNumber = phoneNumber,
                note = note,
                status = OrderStatus.PENDING
            )
            
            val orderId = orderDao.insertOrder(order)
            
            // Clear cart after successful order
            cartRepository.clearCart(userId)
            
            Result.success(order.copy(id = orderId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateOrderStatus(orderId: Long, status: OrderStatus) {
        orderDao.updateOrderStatus(orderId, status)
    }
    
    suspend fun cancelOrder(orderId: Long) {
        orderDao.updateOrderStatus(orderId, OrderStatus.CANCELLED)
    }
    
    private fun generateOrderNumber(): String {
        val dateFormat = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
        return "HF${dateFormat.format(Date())}"
    }
}
