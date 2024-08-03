package com.example.supermarketapp.remoteconfig.domain

import com.example.supermarketapp.remoteconfig.data.Repository
import javax.inject.Inject

class CanAccesToApp @Inject constructor( private val repository: Repository) {

    suspend operator  fun invoke(): Boolean {
        val appVersion = repository.getAppVersion()
        val minVersion = repository.getMinAllowedVersion()
        return appVersion.zip(minVersion).all { (appV , minV) -> // zip fa una llista de tuples amb els elements de les dues llistes que li passem com a parametre i les recorre a la vegada comparant-les
            appV >= minV
        } // si tots els elements de la llista son true retorna true sino false


    }

}