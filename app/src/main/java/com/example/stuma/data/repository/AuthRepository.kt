package com.example.stuma.data.repository

import com.example.stuma.data.api.RetrofitInstance
import com.example.stuma.data.model.*
import com.example.stuma.utils.Result
import com.example.stuma.utils.TokenManager

class AuthRepository(private val tokenManager: TokenManager) {

    // Login
    suspend fun login(loginRequest: LoginRequest): Result<LoginResponse> {
        return try {
            val response = RetrofitInstance.api.login(loginRequest)
            if (response.isSuccessful) {
                val loginResponse = response.body()!!
                // Simpan token
                tokenManager.saveToken(loginResponse.token)
                Result.Success(loginResponse)
            } else {
                Result.Failure(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    // Register
    suspend fun register(registerRequest: RegisterRequest): Result<ApiResponse> {
        return try {
            val response = RetrofitInstance.api.register(registerRequest)
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Failure(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    // Change Password
    suspend fun changePassword(request: ChangePasswordRequest): Result<ApiResponse> {
        return try {
            val token = tokenManager.getToken()
            if (token == null) {
                Result.Failure(Exception("No token found. Please log in again."))
            } else {
                // Sertakan token di header Authorization
                val response = RetrofitInstance.api.changePassword("Bearer $token", request)
                if (response.isSuccessful) {
                    Result.Success(response.body()!!)
                } else {
                    Result.Failure(Exception(response.message()))
                }
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
}