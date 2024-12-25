package com.example.stuma.data.model

// Request model untuk menambah item
data class ItemRequest(
    val items_name: String,
    val category: String,
    val description: String,
    val stock: Int,
    val price: Double
)

// Response model untuk mendapatkan item
data class ItemResponse(
    val id: Int,
    val items_name: String,
    val category: String,
    val description: String,
    val stock: Int,
    val price: Double,
    val seller_name: String,
    val created_at: String,
    val updated_at: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ItemResponse) return false
        return id == other.id // Bandingkan berdasarkan id saja
    }

    override fun hashCode(): Int {
        return id.hashCode() // Gunakan id untuk hashCode
    }
}


