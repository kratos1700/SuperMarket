package com.example.supermarketapp.remoteconfig.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supermarketapp.remoteconfig.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RemoteViewModel @Inject constructor(private val repository: Repository) : ViewModel() {


    fun initApp() {
        viewModelScope.launch(Dispatchers.IO) {


            val text = repository.getAppInfo()

            Log.i("RemoteViewModel", text)

        }
    }


}