package com.example.supermarketapp.remoteconfig.data

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class Repository @Inject constructor(val remoteConfig: FirebaseRemoteConfig) {

   suspend fun getAppInfo(): String {

        remoteConfig.fetch(0)  // per fer una actualitzacio al moment
        remoteConfig.activate().await()  // per activar la nova configuracio hem de fer un await perque es una crida asincrona

        val title = remoteConfig.getString("Title")
       val happy = remoteConfig.getBoolean("Happy")

        return "El title es $title i el happy es $happy"
    }
}