package com.example.supermarketapp.ui.addproduct

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.supermarketapp.R
import com.example.supermarketapp.databinding.ActivityAddProductBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Objects

@AndroidEntryPoint
class AddProductActivity : AppCompatActivity() {

    //creador de navegacio per aquesta activity
    companion object {
        fun create(context: Context): Intent {   // aixi cridem aquesta activity desde qualsevol altre activity
            return Intent(context, AddProductActivity::class.java)
        }
    }

    private lateinit var uri: Uri

    private lateinit var binding: ActivityAddProductBinding
    private val addProductViewModel: AddProductViewModel by viewModels()

    private val intentCameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it && uri.path?.isNotEmpty() == true) {
                addProductViewModel.onImageChange(uri)
            }

        }

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
                    binding.pbAddProduct.isVisible = it.isLoading
                    binding.btnAddProduct.isEnabled = it.isValidProduct()
                    showImage(it.image)
                    if (it.error.isNullOrBlank()){
                        // motrariem un error
                    }

                }

            }
        }
    }

    private fun showImage(image: String) {

        val emptyImage: Boolean = image.isEmpty() //

        // si la imatge es buida mostrem el textview i l'edittext de la imatge, sino mostrem la imatge del producte en un cardview
        // aixi podem mostrar la imatge del producte o el textview i l'edittext per a pujar la imatge
        binding.apply {
            tvImage.isVisible = emptyImage
            etImage.isVisible = emptyImage
            cvImageProduct.isVisible = !emptyImage
            Glide.with(this@AddProductActivity).load(image).centerCrop().into(ivProduct)
        }

    }

    private fun initListeners() {
        binding.etName.doOnTextChanged { text, _, _, _ ->
            addProductViewModel.onNameChange(text)
        }

        binding.etDescription.doOnTextChanged { text, _, _, _ ->
            addProductViewModel.onDescriptioChange(text)
        }

        binding.etPrice.doOnTextChanged { text, _, _, _ ->
            addProductViewModel.onPriceChange(text)
        }
        binding.etImage.setOnClickListener {
            takePhoto()
        }

        binding.btnAddProduct.setOnClickListener {
            addProductViewModel.onAddProductSelected {

                setResult(RESULT_OK)  // si s'ha afegit el producte correctament retornem un resultat ok
                finish() // tanquem l'activity
            }
        }

        binding.ivBack.setOnClickListener {
            setResult(RESULT_CANCELED) // si no s'ha afegit el producte correctament retornem un resultat cancelat
            finish()
        }
    }

    private fun takePhoto() {
        generateUri()
        intentCameraLauncher.launch(uri)

    }

    private fun generateUri() {
        uri = FileProvider.getUriForFile(
            Objects.requireNonNull(this), // aixi es com es fa referencia a la classe actual en kotlin
            "com.example.supermarketapp.fileprovider",
            createImageFile()
        )
    }

    private fun createImageFile(): File {
        val name: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date()) + "image"
        return File.createTempFile(name, ".jpg", externalCacheDir)

    }
}