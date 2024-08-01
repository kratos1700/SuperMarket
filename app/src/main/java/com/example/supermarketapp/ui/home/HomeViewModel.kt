package com.example.supermarketapp.ui.home

import androidx.lifecycle.ViewModel
import com.example.supermarketapp.domain.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() :ViewModel() {

    private var _uiState:MutableStateFlow<HomeUIState> = MutableStateFlow(HomeUIState())
    val uiState : StateFlow<HomeUIState> = _uiState


}



data class HomeUIState(
    val lastProduct: Product? = null,
    val products: List<Product> = emptyList(),
    val topProducts: List<Product> = emptyList(),
)