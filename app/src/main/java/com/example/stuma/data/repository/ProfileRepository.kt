// ProfileRepository.kt
package com.example.stuma.data.repository

import android.content.Context
import com.example.stuma.data.api.RetrofitInstance
import com.example.stuma.data.model.*
import com.example.stuma.utils.Result
import com.example.stuma.utils.TokenManager

class ProfileRepository(private val context: Context) {

    private val tokenManager = TokenManager(context)

    // Get Profile
    suspend fun getProfile(): Result<ProfileResponse> {
        return try {
            val token = tokenManager.getToken()
            if (token == null) {
                println("DEBUG: Token is null")
                Result.Failure(Exception("Token not found. Please log in again."))
            } else {
                println("DEBUG: Token retrieved: $token")
                val response = RetrofitInstance.api.getProfile("Bearer $token")
                println("DEBUG: API Response Code: ${response.code()}")
                println("DEBUG: API Response Message: ${response.message()}")
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        println("DEBUG: Profile data: $body")
                        Result.Success(body)
                    } else {
                        println("DEBUG: Response body is null")
                        Result.Failure(Exception("Response body is null."))
                    }
                } else {
                    println("DEBUG: API Error Body: ${response.errorBody()?.string()}")
                    Result.Failure(Exception("API Error: ${response.message()}"))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Failure(e)
        }
    }

    // Update Profile
    suspend fun updateProfile(updateProfileRequest: UpdateProfileRequest): Result<ApiResponse> {
        return try {
            val token = tokenManager.getToken()
            if (token == null) {
                Result.Failure(Exception("Token not found. Please log in again."))
            } else {
                val response = RetrofitInstance.api.updateProfile("Bearer $token", updateProfileRequest)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        Result.Success(body)
                    } else {
                        Result.Failure(Exception("Response body is null."))
                    }
                } else {
                    Result.Failure(Exception("API Error: ${response.message()}"))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Failure(e)
        }
    }
}
