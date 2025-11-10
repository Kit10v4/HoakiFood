package com.hoaki.food.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.hoaki.food.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val message: String) : AuthState()
    data class Error(val message: String) : AuthState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val googleSignInClient: GoogleSignInClient
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    val isLoggedIn: StateFlow<Boolean> = userRepository.isLoggedIn()
        .stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000), false)

    val userName: StateFlow<String?> = userRepository.userName
        .stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000), null)

    val userEmail: StateFlow<String?> = userRepository.userEmail
        .stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000), null)

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = userRepository.login(email, password)
            result.onSuccess {
                _authState.value = AuthState.Success("Đăng nhập thành công")
            }.onFailure {
                _authState.value = AuthState.Error(it.message ?: "Đã có lỗi xảy ra")
            }
        }
    }

    fun register(email: String, password: String, phone: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            // Create a default full name from the email
            val fullName = email.substringBefore('@')
            val result = userRepository.register(email, password, fullName, phone)
            result.onSuccess {
                _authState.value = AuthState.Success("Đăng ký thành công")
            }.onFailure {
                _authState.value = AuthState.Error(it.message ?: "Đã có lỗi xảy ra")
            }
        }
    }

    fun loginWithGoogle(email: String, displayName: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = userRepository.loginWithGoogle(email, displayName)
            result.onSuccess {
                _authState.value = AuthState.Success("Đăng nhập Google thành công")
            }.onFailure {
                _authState.value = AuthState.Error(it.message ?: "Đã có lỗi xảy ra")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            googleSignInClient.signOut().addOnCompleteListener { 
                viewModelScope.launch {
                    userRepository.logout()
                }
            }
        }
    }

    fun resetAuthState() {
        _authState.value = AuthState.Idle
    }
}
