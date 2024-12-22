package com.example.stuma.data.model

// Digunakan untuk request register
data class RegisterRequest(
    val name: String,
    val phone: String,
    val address: String,
    val email: String,
    val password: String
)

// Digunakan untuk request login
data class LoginRequest(
    val email: String,
    val password: String
)

// Digunakan untuk response login
data class LoginResponse(
    val message: String,
    val token: String
)

data class ChangePasswordRequest(
    val email: String,
    val oldPassword: String,
    val newPassword: String
)

