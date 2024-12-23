package com.example.stuma.data.model

// Request model untuk menambah item
data class ItemRequest(
    val name: String,
    val category: String,
    val description: String,
    val stock: Int,
    val price: Double
)

// Response model untuk mendapatkan item
data class ItemResponse(
    val id: Int,
    val name: String,
    val category: String,
    val description: String,
    val stock: Int,
    val price: Double,
    val user_id: Int,
    val created_at: String,
    val updated_at: String
)

