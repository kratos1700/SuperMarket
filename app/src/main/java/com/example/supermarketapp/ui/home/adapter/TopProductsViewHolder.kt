package com.example.supermarketapp.ui.home.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.supermarketapp.databinding.ItemTopProductBinding
import com.example.supermarketapp.domain.model.Product

class TopProductsViewHolder(view: View):RecyclerView.ViewHolder(view) {

    private val binding = ItemTopProductBinding.bind(view)
    fun render(product: Product) {  // render product  a la vista
        binding.apply {
            tvTopProductName.text = product.name
            Glide.with(tvTopProductName.context)
                .load(product.image)
              // .centerCrop()
                .into(ivTopProduct)
        }
    }


}