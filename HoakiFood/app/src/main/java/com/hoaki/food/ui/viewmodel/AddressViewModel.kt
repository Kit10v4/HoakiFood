package com.hoaki.food.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hoaki.food.data.model.Address
import com.hoaki.food.data.repository.AddressRepository
import com.hoaki.food.data.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val addressRepository: AddressRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val userId = userPreferencesRepository.userId
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val addresses = userId.flatMapLatest { id ->
        if (id != null) {
            addressRepository.getAddressesByUserId(id)
        } else {
            flowOf(emptyList())
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _saveState = MutableStateFlow<SaveAddressState>(SaveAddressState.Idle)
    val saveState: StateFlow<SaveAddressState> = _saveState.asStateFlow()

    private val _deleteState = MutableStateFlow<DeleteAddressState>(DeleteAddressState.Idle)
    val deleteState: StateFlow<DeleteAddressState> = _deleteState.asStateFlow()

    fun saveAddress(
        id: Long = 0,
        label: String,
        fullAddress: String,
        city: String,
        district: String,
        ward: String,
        isDefault: Boolean
    ) {
        viewModelScope.launch {
            try {
                _saveState.value = SaveAddressState.Loading
                val currentUserId = userId.value
                
                if (currentUserId == null) {
                    _saveState.value = SaveAddressState.Error("User not logged in")
                    return@launch
                }

                val address = Address(
                    id = id,
                    userId = currentUserId,
                    label = label,
                    fullAddress = fullAddress,
                    city = city,
                    district = district,
                    ward = ward,
                    isDefault = isDefault
                )

                if (id == 0L) {
                    addressRepository.insertAddress(address)
                } else {
                    addressRepository.updateAddress(address)
                }

                _saveState.value = SaveAddressState.Success
            } catch (e: Exception) {
                _saveState.value = SaveAddressState.Error(e.message ?: "Failed to save address")
            }
        }
    }

    fun deleteAddress(address: Address) {
        viewModelScope.launch {
            try {
                _deleteState.value = DeleteAddressState.Loading
                addressRepository.deleteAddress(address)
                _deleteState.value = DeleteAddressState.Success
            } catch (e: Exception) {
                _deleteState.value = DeleteAddressState.Error(e.message ?: "Failed to delete address")
            }
        }
    }

    fun setDefaultAddress(addressId: Long) {
        viewModelScope.launch {
            try {
                val currentUserId = userId.value ?: return@launch
                addressRepository.setDefaultAddress(addressId, currentUserId)
            } catch (e: Exception) {
                // Handle error silently or show toast
            }
        }
    }

    fun resetSaveState() {
        _saveState.value = SaveAddressState.Idle
    }

    fun resetDeleteState() {
        _deleteState.value = DeleteAddressState.Idle
    }
}

sealed class SaveAddressState {
    object Idle : SaveAddressState()
    object Loading : SaveAddressState()
    object Success : SaveAddressState()
    data class Error(val message: String) : SaveAddressState()
}

sealed class DeleteAddressState {
    object Idle : DeleteAddressState()
    object Loading : DeleteAddressState()
    object Success : DeleteAddressState()
    data class Error(val message: String) : DeleteAddressState()
}
