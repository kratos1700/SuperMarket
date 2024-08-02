package com.example.supermarketapp.ui.addproduct

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


@HiltViewModel
class AddProductViewModel @Inject constructor() : ViewModel() {

    private var _uiState = MutableStateFlow<AddProducState>(AddProducState())
    val uiState: StateFlow<AddProducState> = _uiState


}


data class AddProducState(
    val image: String = "",
    val name: String = "",
    val description: String = "",
    val price: String = "",
    val isLoading: Boolean = false
)