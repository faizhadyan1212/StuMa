// ProfileViewModel.kt
package com.example.stuma.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stuma.data.model.ApiResponse
import com.example.stuma.data.model.ProfileResponse
import com.example.stuma.data.model.UpdateProfileRequest
import com.example.stuma.data.repository.ProfileRepository
import com.example.stuma.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(repository: ProfileRepository) : ViewModel() {

    private val _profileState = MutableStateFlow<Result<ProfileResponse>?>(null)
    val profileState: StateFlow<Result<ProfileResponse>?> = _profileState

    private val _updateProfileState = MutableStateFlow<Result<ApiResponse>?>(null)
    val updateProfileState: StateFlow<Result<ApiResponse>?> = _updateProfileState

    private val repo: ProfileRepository = repository

    fun getProfile() {
        viewModelScope.launch {
            _profileState.value = Result.Loading
            _profileState.value = repo.getProfile()
        }
    }

    fun updateProfile(updateProfileRequest: UpdateProfileRequest) {
        viewModelScope.launch {
            _updateProfileState.value = Result.Loading
            _updateProfileState.value = repo.updateProfile(updateProfileRequest)
        }
    }

    fun resetState() {
        _profileState.value = null
        _updateProfileState.value = null
    }

}