package com.example.supermarketapp.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supermarketapp.data.network.FirebaseDatabaseService
import com.example.supermarketapp.domain.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(val repository: FirebaseDatabaseService) : ViewModel() {

    private var _uiState: MutableStateFlow<HomeUIState> = MutableStateFlow(HomeUIState())
    val uiState: StateFlow<HomeUIState> = _uiState

    init {
        getData()
    }

    fun getData() {
        getLastProduct()
        getAllProducts()
    }

    private fun getAllProducts() {
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                repository.getAllProducts()
            }

            _uiState.update { it.copy(products = response) }  // actualiza el estado de la UI
            getTopProducts(response)
        }
    }

    private fun getTopProducts(products: List<Product>) {
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                repository.getTopProducts()   // obtiene los productos mas top
            }

            val topProducts = products.filter { response .contains(it.id) }

            _uiState.update { it.copy(topProducts = topProducts) }  // actualiza el estado de la UI
        }
    }

    private fun getLastProduct() {
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                repository.getLastProducts()
            }
            //    _uiState.value = _uiState.value.copy(lastProduct = response)  // actualiza el estado de la UI

            _uiState.update { it.copy(lastProduct = response) }  // actualiza el estado de la UI
            Log.i("HomeViewModel", "getLastProduct: $response")
        }
    }


}


data class HomeUIState(
    val lastProduct: Product? = null,
    val products: List<Product> = emptyList(),
    val topProducts: List<Product> = emptyList(),
)