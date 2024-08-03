package com.example.supermarketapp.remoteconfig.data

import android.content.Context
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class Repository @Inject constructor(
    val remoteConfig: FirebaseRemoteConfig,

    @ApplicationContext private val context: Context
) {

    companion object {
        const val MIN_VERSION_RC = "min_version"
    }

    //exemple de com fer una crida a la configuracio remota
    suspend fun getAppInfo(): String {

        remoteConfig.fetch(0)  // per fer una actualitzacio al moment
        remoteConfig.activate()
            .await()  // per activar la nova configuracio hem de fer un await perque es una crida asincrona

        val title = remoteConfig.getString("Title")
        val happy = remoteConfig.getBoolean("Happy")

        return "El title es $title i el happy es $happy"
    }


    /**
     * Control de versions, torna una llista de ints amb la versio de l'app
     *
     * Esta al gradle
     *  versionName = "1.0.0"
     *  1 -> es canvia per algo molt gran de la app
     *  0 -> es canvia per alguna nova funcionalitat
     *  0 -> es canvia per alguna correccio
     */
    fun getAppVersion(): List<Int> {
   return    try {

       val packageInfo = context.packageManager.getPackageInfo(context.packageName,0) // agafem la info de la app
       packageInfo.versionName.split(".") // separem el string per punts i ho convertim a una llista de ints
           .map { it.toInt() } // convertim els strings a ints

        } catch (e: Exception) {
            listOf(0, 0, 0)
        }


    }

    /**
     * Esta funció es conecta a remote config i agafa la versio minima que es permet
     * per la app
     *
     */
    suspend fun getMinAllowedVersion(): List<Int> {
        remoteConfig.fetch(0)  // per fer una actualitzacio al moment
        remoteConfig.activate()
            .await()  // per activar la nova configuracio hem de fer un await perque es una crida asincrona

        val minVersion = remoteConfig.getString(MIN_VERSION_RC)
        if (minVersion.isBlank()){
            return listOf(0,0,0)
        }
        return minVersion.split(".")
            .map { it.toInt() }


    }


}