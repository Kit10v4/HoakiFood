package com.hoaki.food.data.repository

import com.hoaki.food.data.local.dao.AddressDao
import com.hoaki.food.data.model.Address
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddressRepository @Inject constructor(
    private val addressDao: AddressDao
) {
    fun getAddressesByUserId(userId: Long): Flow<List<Address>> {
        return addressDao.getAddressesByUserId(userId)
    }

    suspend fun getAddressById(addressId: Long): Address? {
        return addressDao.getAddressById(addressId)
    }

    suspend fun getDefaultAddress(userId: Long): Address? {
        return addressDao.getDefaultAddress(userId)
    }

    suspend fun insertAddress(address: Address): Long {
        // If this is set as default, clear other defaults first
        if (address.isDefault) {
            addressDao.clearDefaultAddress(address.userId)
        }
        return addressDao.insertAddress(address)
    }

    suspend fun updateAddress(address: Address) {
        // If this is set as default, clear other defaults first
        if (address.isDefault) {
            addressDao.clearDefaultAddress(address.userId)
        }
        addressDao.updateAddress(address)
    }

    suspend fun deleteAddress(address: Address) {
        addressDao.deleteAddress(address)
    }

    suspend fun setDefaultAddress(addressId: Long, userId: Long) {
        val address = addressDao.getAddressById(addressId)
        if (address != null) {
            addressDao.clearDefaultAddress(userId)
            addressDao.updateAddress(address.copy(isDefault = true))
        }
    }

    suspend fun deleteAllAddressesByUserId(userId: Long) {
        addressDao.deleteAllAddressesByUserId(userId)
    }
}
