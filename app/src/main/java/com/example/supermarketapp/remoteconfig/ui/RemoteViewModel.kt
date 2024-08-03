package com.example.supermarketapp.remoteconfig.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supermarketapp.remoteconfig.data.Repository
import com.example.supermarketapp.remoteconfig.domain.CanAccesToApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RemoteViewModel @Inject constructor(
    private val repository: Repository,
    private val canAccesToApp: CanAccesToApp
) : ViewModel() {

    private val _showBlockDialog = MutableStateFlow<Boolean>(false)
    val showBlockDialog: StateFlow<Boolean> = _showBlockDialog


    fun initApp() {
        viewModelScope.launch(Dispatchers.IO) {


            val text = repository.getAppInfo()

            Log.i("RemoteViewModel", text)

        }


        // recuperem les dades de la configuracio remota i al realitzar la comprovacio de la versio de la app li pasem
        // la comprovacio de si es pot accedir a la app o no al showBlockDialog perque ens mostri el dialog si no es pot accedir
        viewModelScope.launch {
            val canAcces = withContext(Dispatchers.IO) {
                canAccesToApp()
            }
            _showBlockDialog.value = !canAcces
            Log.i("RemoteViewModel", "Can acces to app: $canAcces")
        }


    }


}