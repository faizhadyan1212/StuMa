package com.example.stuma.data.model

// Digunakan untuk response generik
data class ApiResponse(
    val message: String,
    val data: List<ItemResponse> // Menambahkan field items sebagai list
)
