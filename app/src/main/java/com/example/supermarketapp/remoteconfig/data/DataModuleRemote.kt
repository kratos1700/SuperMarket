package com.example.supermarketapp.remoteconfig.data

import com.example.supermarketapp.R
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModuleRemote {

    @Singleton
    @Provides
    fun provideRemoteConfig(): FirebaseRemoteConfig{
        return Firebase.remoteConfig.apply {
            setConfigSettingsAsync(
                remoteConfigSettings {
                    minimumFetchIntervalInSeconds = 3600   // 1 hora
                }
            )
            setDefaultsAsync(R.xml.remote_config_defaults)
            fetchAndActivate()  // fetch i activate en un sol pas perque no es bloquegi la UI


        }

    }


}