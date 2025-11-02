package com.hoaki.food.data.local.dao

import androidx.room.*
import com.hoaki.food.data.model.Order
import com.hoaki.food.data.model.OrderStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders WHERE userId = :userId ORDER BY createdAt DESC")
    fun getUserOrders(userId: Long): Flow<List<Order>>
    
    @Query("SELECT * FROM orders WHERE id = :orderId")
    fun getOrderById(orderId: Long): Flow<Order?>
    
    @Query("SELECT * FROM orders WHERE userId = :userId AND status = :status ORDER BY createdAt DESC")
    fun getOrdersByStatus(userId: Long, status: OrderStatus): Flow<List<Order>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: Order): Long
    
    @Update
    suspend fun updateOrder(order: Order)
    
    @Delete
    suspend fun deleteOrder(order: Order)
    
    @Query("UPDATE orders SET status = :status, updatedAt = :updatedAt WHERE id = :orderId")
    suspend fun updateOrderStatus(orderId: Long, status: OrderStatus, updatedAt: Long = System.currentTimeMillis())
}
