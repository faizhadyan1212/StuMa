package com.example.stuma.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stuma.data.model.ItemRequest
import com.example.stuma.data.repository.SellRepository
import com.example.stuma.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SellViewModel(private val sellRepository: SellRepository) : ViewModel() {

    // State untuk menampilkan status pengiriman item
    private val _sellState = MutableStateFlow<Result<Unit>?>(null)
    val sellState: StateFlow<Result<Unit>?> = _sellState

    // Fungsi untuk mengirim item ke repository
    fun sellItem(itemRequest: ItemRequest) {
        viewModelScope.launch {
            _sellState.value = Result.Loading // Set loading state
            Log.d("SellViewModel", "Sending item data to repository: $itemRequest")

            try {
                val result = sellRepository.sellItem(itemRequest)
                _sellState.value = result

                if (result is Result.Success) {
                    Log.d("SellViewModel", "Item successfully sent to repository.")
                } else if (result is Result.Failure) {
                    Log.e("SellViewModel", "Failed to send item: ${result.exception?.message}")
                }
            } catch (e: Exception) {
                Log.e("SellViewModel", "Exception occurred: ${e.message}")
                _sellState.value = Result.Failure(e)
            }
        }
    }

    // Reset state setelah operasi selesai
    fun resetState() {
        _sellState.value = null
        Log.d("SellViewModel", "Sell state reset.")
    }
}
