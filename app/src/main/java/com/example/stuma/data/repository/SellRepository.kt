package com.example.stuma.data.repository

import android.util.Log
import com.example.stuma.data.api.RetrofitInstance
import com.example.stuma.data.model.ItemRequest
import com.example.stuma.utils.Result
import com.example.stuma.utils.TokenManager

class SellRepository(private val tokenManager: TokenManager) {

    // Fungsi untuk mengirim item ke server
    suspend fun sellItem(itemRequest: ItemRequest): Result<Unit> {
        return try {
            // Ambil token dari TokenManager
            val token = tokenManager.getToken()
            if (token == null) {
                Log.e("SellRepository", "Token not found. User needs to log in again.")
                return Result.Failure(Exception("No token found. Please log in again."))
            }

            Log.d("SellRepository", "Calling API to sell item: $itemRequest with token: Bearer $token")

            // Memanggil API endpoint
            val response = RetrofitInstance.api.sellItem("Bearer $token", itemRequest)
            if (response.isSuccessful) {
                Log.d("SellRepository", "API call successful. Item added successfully.")
                Result.Success(Unit) // Mengembalikan sukses jika API merespons dengan sukses
            } else {
                Log.e("SellRepository", "API call failed: ${response.message()}")
                Result.Failure(Exception("Failed to add item: ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("SellRepository", "Exception occurred: ${e.message}")
            Result.Failure(e) // Menangkap kesalahan dan mengembalikan failure
        }
    }
}
