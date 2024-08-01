package com.example.supermarketapp.ui.home

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.supermarketapp.R
import com.example.supermarketapp.databinding.ActivityHomeBinding
import com.example.supermarketapp.domain.model.Product
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

   // private val viewModel: HomeViewModel by viewModels()  // no serveix per cargar dades si tenim un init al viewmodel ja que es un delegat
   private lateinit var homeViewModel: HomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java] // aixi carreguem les dades del viewmodel si tenim un init al viewmodel

        initUI()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initUI() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                homeViewModel.uiState.collect{state ->
                    renderLastProduct(state.lastProduct)
                    renderTopProducts(state.topProducts)
                    renderProducts(state.products)



                }

            }
        }

    }

    private fun renderProducts(products: List<Product>) {

    }

    private fun renderTopProducts(topProducts: List<Product>) {

    }

    private fun renderLastProduct(lastProduct: Product?) {

    }
}