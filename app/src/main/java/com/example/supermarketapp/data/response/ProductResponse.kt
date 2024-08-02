package com.example.supermarketapp.data.response

import com.example.supermarketapp.domain.model.Product

data class ProductResponse(


    val id: String = "",
    val name: String = "",
    val description: String = "",
    val image: String = "",
    val price: String = ""

    ){

    // esta funcion convierte un ProductResponse a un Product
    fun toDomain():Product{
        return Product(
            id = id,
            title = name,
            description = description,
            imageUrl = image,
            price = price
        )
    }
}

