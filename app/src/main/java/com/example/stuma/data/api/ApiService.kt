package com.example.stuma.data.api

import com.example.stuma.data.model.ApiResponse
import com.example.stuma.data.model.ChangePasswordRequest
import com.example.stuma.data.model.ItemRequest
import com.example.stuma.data.model.ItemResponse
import com.example.stuma.data.model.LoginRequest
import com.example.stuma.data.model.LoginResponse
import com.example.stuma.data.model.ProfileResponse
import com.example.stuma.data.model.RegisterRequest
import com.example.stuma.data.model.UpdateProfileRequest
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // Register
    @POST("api/auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<ApiResponse>

    // Login
    @POST("api/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    // Get Profile
    @GET("api/profile")
    suspend fun getProfile(
        @Header("Authorization") token: String
    ): Response<ProfileResponse>

    // Update Profile
    @PUT("api/profile")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Body request: UpdateProfileRequest
    ): Response<ApiResponse>

    // Change Password
    @POST("api/profile/change-password")
    suspend fun changePassword(
        @Header("Authorization") token: String,
        @Body request: ChangePasswordRequest
    ): Response<ApiResponse>

    // Sell Item
    @POST("api/items")
    suspend fun sellItem(
        @Header("Authorization") token: String,
        @Body itemRequest: ItemRequest
    ): Response<Unit>

    // Get Items
    @GET("api/items") // Sesuaikan dengan endpoint API Anda
    suspend fun getItems(@Header("Authorization") authHeader: String): Response<ApiResponse>
}
