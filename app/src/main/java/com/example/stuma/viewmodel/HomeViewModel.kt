package com.example.stuma.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stuma.data.model.ItemResponse
import com.example.stuma.data.repository.HomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.stuma.utils.Result

class HomeViewModel(private val homeRepository: HomeRepository) : ViewModel() {

    private val _itemsState = MutableStateFlow<Result<List<ItemResponse>>?>(null)
    val itemsState: StateFlow<Result<List<ItemResponse>>?> = _itemsState

    fun fetchItems() {
        viewModelScope.launch {
            Log.d("HomeViewModel", "Fetching items from repository...")
            _itemsState.value = Result.Loading

            try {
                val result = homeRepository.getItems()
                _itemsState.value = result
                Log.d("HomeViewModel", "ItemsState: $result") // Log tambahan

                when (result) {
                    is Result.Success -> Log.d("HomeViewModel", "Fetched ${result.data.size} items successfully.")
                    is Result.Failure -> Log.e("HomeViewModel", "Failed to fetch items: ${result.exception?.message}")
                    else -> Log.e("HomeViewModel", "Unexpected state.")
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Exception occurred while fetching items: ${e.message}")
                _itemsState.value = Result.Failure(e)
            }
        }
    }


    fun resetState() {
        _itemsState.value = null
        Log.d("HomeViewModel", "Items state reset.")
    }
}
