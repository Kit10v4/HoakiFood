package com.hoaki.food.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hoaki.food.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val userName: StateFlow<String?> = userRepository.userName
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val userEmail: StateFlow<String?> = userRepository.userEmail
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
}
