package com.example.stuma.data.repository

import android.util.Log
import com.example.stuma.data.api.RetrofitInstance
import com.example.stuma.data.model.ApiResponse
import com.example.stuma.data.model.ItemResponse
import com.example.stuma.utils.Result
import com.example.stuma.utils.TokenManager

class HomeRepository(private val tokenManager: TokenManager) {

    suspend fun getItems(): Result<List<ItemResponse>> {
        return try {
            val token = tokenManager.getToken()
            if (token == null) {
                Log.e("HomeRepository", "Token not found. User needs to log in again.")
                return Result.Failure(Exception("No token found. Please log in again."))
            }

            Log.d("HomeRepository", "Using token: Bearer $token")

            val response = RetrofitInstance.api.getItems("Bearer $token")
            if (response.isSuccessful) {
                val apiResponse = response.body()
                Log.d("HomeRepository", "Response body: $apiResponse") // Log respons lengkap
                if (apiResponse != null && apiResponse.data != null) {
                    Log.d("HomeRepository", "Fetched items: ${apiResponse.data}")
                    return Result.Success(apiResponse.data) // Menggunakan apiResponse.data
                } else {
                    Log.e("HomeRepository", "Items or response body is null.")
                    return Result.Failure(Exception("Items or response body is null."))
                }
            } else {
                Log.e("HomeRepository", "API call failed with code ${response.code()} and message ${response.message()}")
                return Result.Failure(Exception("Failed to fetch items: ${response.message()}"))
            }

        } catch (e: Exception) {
            Log.e("HomeRepository", "Exception occurred while fetching items: ${e.message}")
            return Result.Failure(e)
        }
    }

}

