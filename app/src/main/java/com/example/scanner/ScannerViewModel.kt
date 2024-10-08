package com.example.scanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ScannerViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Initial)
    val uiState: StateFlow<UiState> = _uiState

    fun getProductInfo(barcode: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val response = RetrofitClient.openFoodFactsApi.getProductInfo(barcode)
                _uiState.value = UiState.Success(response.product)
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error al obtener la información del producto")
            }
        }
    }

    sealed class UiState {
        object Initial : UiState()
        object Loading : UiState()
        data class Success(val product: Product) : UiState()
        data class Error(val message: String) : UiState()
    }
}