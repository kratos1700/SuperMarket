package com.example.supermarketapp.ui.addproduct

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supermarketapp.data.network.FirebaseDatabaseService
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class AddProductViewModel @Inject constructor(
    private val firebaseDatabaseService: FirebaseDatabaseService,
) : ViewModel() {

    private var _uiState = MutableStateFlow<AddProducState>(AddProducState())
    val uiState: StateFlow<AddProducState> = _uiState

    fun onNameChange(name: CharSequence?) {
        _uiState.update {
            it.copy(name = name.toString())
        }

    }


    fun onPriceChange(price: CharSequence?) {
        _uiState.update {
            it.copy(price = price.toString())
        }

    }

    fun onDescriptioChange(description: CharSequence?) {
        _uiState.update {
            it.copy(description = description.toString())
        }

    }

    fun onImageChange(uri: Uri) {

        viewModelScope.launch {
            showLoading(true)
            val result = withContext(Dispatchers.IO) {
                firebaseDatabaseService.uploadAndDownloadImage(uri)  // cridem a la funcio de la base de dades que puja la imatge i la descarrega
            }

            _uiState.update { it.copy(image = result) }  // actualitzem la imatge amb la uri
            showLoading(false)

        }

    }

    private fun showLoading(show: Boolean) {
        _uiState.update { it.copy(isLoading = show) }
    }

    fun onAddProductSelected(onSuccessProduct: () -> Unit) {
        viewModelScope.launch {
            showLoading(true)
            val result = withContext(Dispatchers.IO) {
                firebaseDatabaseService.addProduct(
                    uiState.value.name,
                    uiState.value.description,
                    uiState.value.price,
                    uiState.value.image
                )
            }
            if (result) {
                onSuccessProduct()

            } else {
                _uiState.update {
                    it.copy(error = "Error adding product")

                }

                showLoading(false)
            }

        }

    }


    data class AddProducState(
        val image: String = "",
        val name: String = "",
        val description: String = "",
        val price: String = "",
        val isLoading: Boolean = false,
        val error: String? = null
    ) {
        fun isValidProduct(): Boolean {
            return name.isNotBlank() && description.isNotBlank() && price.isNotBlank()
        }
    }
}