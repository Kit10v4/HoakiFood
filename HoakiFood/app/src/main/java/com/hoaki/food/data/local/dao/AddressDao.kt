package com.hoaki.food.data.local.dao

import androidx.room.*
import com.hoaki.food.data.model.Address
import kotlinx.coroutines.flow.Flow

@Dao
interface AddressDao {
    @Query("SELECT * FROM addresses WHERE userId = :userId ORDER BY isDefault DESC, id DESC")
    fun getAddressesByUserId(userId: Long): Flow<List<Address>>

    @Query("SELECT * FROM addresses WHERE id = :addressId")
    suspend fun getAddressById(addressId: Long): Address?

    @Query("SELECT * FROM addresses WHERE userId = :userId AND isDefault = 1 LIMIT 1")
    suspend fun getDefaultAddress(userId: Long): Address?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddress(address: Address): Long

    @Update
    suspend fun updateAddress(address: Address)

    @Delete
    suspend fun deleteAddress(address: Address)

    @Query("UPDATE addresses SET isDefault = 0 WHERE userId = :userId")
    suspend fun clearDefaultAddress(userId: Long)

    @Query("DELETE FROM addresses WHERE userId = :userId")
    suspend fun deleteAllAddressesByUserId(userId: Long)
}
