// AuthViewModel.kt
package com.example.stuma.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stuma.data.model.*
import com.example.stuma.data.repository.AuthRepository
import com.example.stuma.utils.Result
import com.example.stuma.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    // State untuk Login
    private val _loginState = MutableStateFlow<Result<LoginResponse>?>(null)
    val loginState: StateFlow<Result<LoginResponse>?> = _loginState

    // State untuk Register
    private val _registerState = MutableStateFlow<Result<ApiResponse>?>(null)
    val registerState: StateFlow<Result<ApiResponse>?> = _registerState

    // State untuk Change Password
    private val _changePasswordState = MutableStateFlow<Result<ApiResponse>?>(null)
    val changePasswordState: StateFlow<Result<ApiResponse>?> = _changePasswordState

    // Login
    fun loginUser(loginRequest: LoginRequest) {
        viewModelScope.launch {
            _loginState.value = Result.Loading
            _loginState.value = repository.login(loginRequest)
        }
    }

    // Register
    fun registerUser(registerRequest: RegisterRequest) {
        viewModelScope.launch {
            _registerState.value = Result.Loading
            _registerState.value = repository.register(registerRequest)
        }
    }

    // Change Password
    fun changePassword(request: ChangePasswordRequest) {
        viewModelScope.launch {
            _changePasswordState.value = Result.Loading
            _changePasswordState.value = repository.changePassword(request)
        }
    }

    fun resetChangePasswordState() {
        _changePasswordState.value = null
    }
}
