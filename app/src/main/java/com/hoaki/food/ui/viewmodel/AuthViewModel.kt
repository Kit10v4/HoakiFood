package com.hoaki.food.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hoaki.food.data.model.User
import com.hoaki.food.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()
    
    val isLoggedIn: StateFlow<Boolean> = userRepository.isLoggedIn()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
    
    val currentUserId: StateFlow<Long?> = userRepository.getCurrentUserId()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = userRepository.login(email, password)
            _authState.value = if (result.isSuccess) {
                AuthState.Success(result.getOrNull()!!)
            } else {
                AuthState.Error(result.exceptionOrNull()?.message ?: "Đăng nhập thất bại")
            }
        }
    }
    
    fun register(email: String, password: String, fullName: String, phone: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = userRepository.register(email, password, fullName, phone)
            _authState.value = if (result.isSuccess) {
                AuthState.Success(result.getOrNull()!!)
            } else {
                AuthState.Error(result.exceptionOrNull()?.message ?: "Đăng ký thất bại")
            }
        }
    }
    
    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
            _authState.value = AuthState.Idle
        }
    }
    
    fun resetAuthState() {
        _authState.value = AuthState.Idle
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: User) : AuthState()
    data class Error(val message: String) : AuthState()
}
