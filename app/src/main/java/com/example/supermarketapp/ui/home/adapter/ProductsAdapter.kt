package com.example.supermarketapp.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.layout.Layout
import androidx.recyclerview.widget.RecyclerView
import com.example.supermarketapp.R
import com.example.supermarketapp.domain.model.Product

class ProductsAdapter (var products:List<Product> = emptyList()):RecyclerView.Adapter<ProductsViewHolder>() {

        fun updateList(products: List<Product>){
            this.products = products
            notifyDataSetChanged()
        }


    // esta clase se encarga de adaptar los productos para mostrarlos en la UI
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductsViewHolder(view)
    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        holder.render(products[position])
    }
}