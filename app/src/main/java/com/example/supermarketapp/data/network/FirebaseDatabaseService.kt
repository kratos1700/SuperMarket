package com.example.supermarketapp.data.network

import com.example.supermarketapp.data.response.ProductResponse
import com.example.supermarketapp.data.response.TopProductsResponse
import com.example.supermarketapp.domain.model.Product
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseDatabaseService @Inject constructor(private val firestore: FirebaseFirestore) {

    companion object {
        const val PRODUCTS_PATH = "products"
        const val MANAGEMENT_PATH = "management"
        const val TOP_PRODUCT_DOCUMENT = "topProducts"
        const val ID_PATH = "ID"
    }


    // esta funcion obtiene todos los productos de la base de datos de Firebase
    suspend fun getAllProducts(): List<Product> {

        return firestore.collection(PRODUCTS_PATH).get().await().map {

            it.toObject(ProductResponse::class.java)
                .toDomain()  // retorna un Product de la clase ProductResponse de la base de datos de Firebase
        }


    }

    // esta funcion obtiene el ultimo producto de la base de datos de Firebase
    suspend fun getLastProducts(): Product? {
        return firestore.collection(PRODUCTS_PATH).orderBy(ID_PATH, Query.Direction.DESCENDING)
            .limit(1).get().await()
            .firstOrNull()?.toObject(ProductResponse::class.java)?.toDomain()

    }

    // esta funcion obtiene los productos mas vendidos de la base de datos de Firebase
    suspend fun getTopProducts(): List<String> {
        return firestore.collection(MANAGEMENT_PATH).document(TOP_PRODUCT_DOCUMENT).get().await()
            .toObject(TopProductsResponse::class.java)?.ids ?: emptyList()  // retorna una lista de los productos mas vendidos de la base de datos de Firebase o una lista vacia

    }


}