package com.example.supermarketapp.remoteconfig.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.supermarketapp.R
import com.example.supermarketapp.ui.addproduct.AddProductActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RemoteConfigActivity : AppCompatActivity() {

    //creador de navegacio per aquesta activity
    companion object {
        fun create(context: Context): Intent {   // aixi cridem aquesta activity desde qualsevol altre activity
            return Intent(context, RemoteConfigActivity::class.java)
        }
    }

    private  val remoteViewModel: RemoteViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_remote_config)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        remoteViewModel.initApp()
    }
}