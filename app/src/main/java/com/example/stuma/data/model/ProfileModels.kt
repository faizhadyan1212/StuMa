package com.example.stuma.data.model

// Digunakan untuk response profile
data class ProfileResponse(
    val id: Int,
    val name: String,
    val phone: String,
    val address: String,
    val email: String
)

// Digunakan untuk request update profile
data class UpdateProfileRequest(
    val name: String,
    val phone: String,
    val address: String
)
