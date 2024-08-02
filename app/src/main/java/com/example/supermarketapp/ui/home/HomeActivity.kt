package com.example.supermarketapp.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.supermarketapp.R
import com.example.supermarketapp.databinding.ActivityHomeBinding
import com.example.supermarketapp.domain.model.Product
import com.example.supermarketapp.ui.addproduct.AddProductActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    // private val viewModel: HomeViewModel by viewModels()  // no serveix per cargar dades si tenim un init al viewmodel ja que es un delegat
    private lateinit var homeViewModel: HomeViewModel


    private val addProductLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            homeViewModel.getData()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java] // aixi carreguem les dades del viewmodel si tenim un init al viewmodel

        initUI()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initUI() {
        initListeners()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                homeViewModel.uiState.collect { state ->
                    renderLastProduct(state.lastProduct)
                    renderTopProducts(state.topProducts)
                    renderProducts(state.products)


                }

            }
        }

    }

    private fun initListeners() {

        binding.viewToolbar.tvAddProduct.setOnClickListener {
           // startActivity(Intent(this, AddProductActivity::class.java))
            addProductLauncher.launch(AddProductActivity.create(this))


        }
    }

    private fun renderProducts(products: List<Product>) {

    }

    private fun renderTopProducts(topProducts: List<Product>) {

    }

    private fun renderLastProduct(lastProduct: Product?) {

        if (lastProduct == null) return  // si no hi ha cap product no fem res

        binding.viewLastProduct.tvTitle.text = lastProduct.title
        binding.viewLastProduct.tvDescription.text = lastProduct.description
        Glide.with(this).load(lastProduct.imageUrl)
            .placeholder(R.drawable.ic_shop)
            .into(binding.viewLastProduct.ivLastProduct)
        binding.viewLastProduct.root.isVisible = true


    }
}