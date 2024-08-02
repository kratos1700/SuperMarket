package com.example.supermarketapp.ui.addproduct

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.supermarketapp.R
import com.example.supermarketapp.databinding.ActivityAddProductBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddProductActivity : AppCompatActivity() {

    //creador de navegacio per aquesta activity
    companion object{
        fun create(context: Context): Intent {   // aixi cridem aquesta activity desde qualsevol altre activity
            return Intent(context, AddProductActivity::class.java)
        }
    }

   private  lateinit var binding: ActivityAddProductBinding
   private  val addProductViewModel: AddProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initUI()
    }

    private fun initUI() {
        initListeners()
        initUIState()
    }

    private fun initUIState() {
        lifecycleScope.launch {

            repeatOnLifecycle(Lifecycle.State.STARTED) {
                addProductViewModel.uiState.collect {

                }

            }
        }
    }

    private fun initListeners() {

    }
}