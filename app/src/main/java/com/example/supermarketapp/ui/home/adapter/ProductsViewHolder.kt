package com.example.supermarketapp.ui.home.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.supermarketapp.databinding.ItemProductBinding
import com.example.supermarketapp.domain.model.Product


class ProductsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val binding = ItemProductBinding.bind(view)
    fun render(product: Product) {
        // render product

        binding.apply {
            tvName.text = product.name
            tvDescription.text = product.description
            tvPrice.text = "${product.price} €"
            Glide.with(itemView.context)
                .load(product.image)
                .centerCrop()
                .into(imgProduct)


        }



    }
}