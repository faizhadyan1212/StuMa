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

    // State untuk items yang diambil dari repository
    private val _itemsState = MutableStateFlow<Result<List<ItemResponse>>?>(null)
    val itemsState: StateFlow<Result<List<ItemResponse>>?> = _itemsState

    // State untuk wishlist
    private val _wishlist = MutableStateFlow<List<ItemResponse>>(emptyList())
    val wishlist: StateFlow<List<ItemResponse>> = _wishlist

    // State untuk cart dengan jumlah item
    private val _cart = MutableStateFlow<Map<ItemResponse, Int>>(emptyMap())
    val cart: StateFlow<Map<ItemResponse, Int>> = _cart

    // State untuk filtered items
    private val _filteredItemsState = MutableStateFlow<List<ItemResponse>>(emptyList())
    val filteredItemsState: StateFlow<List<ItemResponse>> = _filteredItemsState

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory

    /**
     * Tambahkan item ke cart.
     * Jika item sudah ada, tambahkan jumlahnya hingga stok tersedia.
     */
    fun addToCart(item: ItemResponse) {
        if (item.items_name != null) { // Pastikan item tidak null
            _cart.value = _cart.value.toMutableMap().apply {
                val currentQuantity = this[item] ?: 0
                if (currentQuantity < item.stock) {
                    this[item] = currentQuantity + 1
                }
            }
        } else {
            Log.e("HomeViewModel", "Item is null: ${item}")
        }
    }


    /**
     * Hapus item dari cart. Jika jumlah item menjadi 0, hapus dari cart.
     */
    fun removeFromCart(item: ItemResponse) {
        _cart.value = _cart.value.toMutableMap().apply {
            if (this.containsKey(item)) {
                this[item] = this[item]?.minus(1) ?: 0
                if ((this[item] ?: 0) <= 0) remove(item) // Hapus jika jumlah <= 0
            }
        }
    }

    /**
     * Kosongkan seluruh isi cart.
     */
    fun clearCart() {
        _cart.value = emptyMap()
    }

    /**
     * Tambahkan item ke wishlist.
     */
    fun addToWishlist(item: ItemResponse) {
        _wishlist.value = _wishlist.value.toMutableList().apply {
            if (!contains(item)) add(item)
        }
    }

    /**
     * Hapus item dari wishlist.
     */
    fun removeFromWishlist(item: ItemResponse) {
        _wishlist.value = _wishlist.value.toMutableList().apply {
            remove(item)
        }
    }

    /**
     * Ambil semua items dari repository.
     */
    fun fetchItems() {
        viewModelScope.launch {
            Log.d("HomeViewModel", "Fetching items from repository...")
            _itemsState.value = Result.Loading

            try {
                val result = homeRepository.getItems()
                _itemsState.value = result

                if (result is Result.Success) {
                    filterItemsByCategory("All") // Set default filter to All
                }

                Log.d("HomeViewModel", "ItemsState: $result")

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

    /**
     * Ambil item berdasarkan ID dari state items saat ini.
     */
    fun getItemById(itemId: Int?): ItemResponse? {
        val currentItems = (_itemsState.value as? Result.Success)?.data
        return currentItems?.find { it.id == itemId }
    }

    /**
     * Filter items berdasarkan kategori.
     */
    fun filterItemsByCategory(category: String) {
        _selectedCategory.value = category
        val currentItems = (_itemsState.value as? Result.Success)?.data ?: emptyList()
        _filteredItemsState.value = if (category == "All") {
            currentItems
        } else {
            currentItems.filter { it.category.equals(category, ignoreCase = true) }
        }
    }

    fun searchItems(query: String) {
        val currentItems = (_itemsState.value as? Result.Success)?.data ?: emptyList()
        _filteredItemsState.value = if (query.isEmpty()) {
            currentItems
        } else {
            currentItems.filter {
                it.items_name.contains(query, ignoreCase = true) ||
                        it.category.contains(query, ignoreCase = true)
            }
        }
    }
}
