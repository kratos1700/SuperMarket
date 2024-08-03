package com.example.supermarketapp.data.network

import android.net.Uri
import com.example.supermarketapp.data.response.ProductResponse
import com.example.supermarketapp.data.response.TopProductsResponse
import com.example.supermarketapp.domain.model.Product
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.storageMetadata
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine

import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject
import kotlin.coroutines.resume

class FirebaseDatabaseService @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) {

    companion object {
        const val PRODUCTS_PATH = "products"
        const val MANAGEMENT_PATH = "management"
        const val TOP_PRODUCT_DOCUMENT = "top_products"
        const val ID_PATH = "id"
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
            .toObject(TopProductsResponse::class.java)?.ids ?: emptyList() // retorna una lista de los productos mas vendidos de la base de datos de Firebase o una lista vacia



    }

    suspend fun uploadAndDownloadImage(uri: Uri): String {
        return suspendCancellableCoroutine<String> { suspendCancellable ->
            val reference =
                storage.reference.child("download/${uri.lastPathSegment}") // referencia a la carpeta download en Firebase Storage
            reference.putFile(uri, createMetadata())
                .addOnSuccessListener {

                    downloadImage(it, suspendCancellable)

                }
                .addOnFailureListener {
                    suspendCancellable.resume("")
                }
        }

    }

    private fun downloadImage(
        uploadTask: UploadTask.TaskSnapshot,
        suspendCancellable: CancellableContinuation<String>
    ) {

        uploadTask.storage.downloadUrl.addOnSuccessListener {  // descarga la imagen subida a Firebase Storage y la convierte en una URL
            suspendCancellable.resume(it.toString()) // retorna la URL de la imagen subida a Firebase Storage en formato String
        }.addOnFailureListener { // si no se puede descargar la imagen subida a Firebase Storage
            suspendCancellable.resume("") // retorna un String vacio
        }

    }

    private fun createMetadata(): StorageMetadata {
        val metadata: StorageMetadata = storageMetadata {
            contentType = "image/jpeg"
            setCustomMetadata(
                "date",
                Date().time.toString()
            )  // metadata de la imagen subida a Firebase Storage con la fecha actual
        }
        return metadata
    }

    suspend fun addProduct(name: String, description: String, price: String, image: String): Boolean {

        val id = generateProductID()

        val product = hashMapOf(
            "id" to id,
            "name" to name,
            "description" to description,
            "price" to price,
            "image" to image
        )

      return  suspendCancellableCoroutine { cancellableCoroutine ->

            firestore.collection(PRODUCTS_PATH).document(id).set(product)
                .addOnSuccessListener {
                    cancellableCoroutine.resume(true)

                }
                .addOnFailureListener {
                    cancellableCoroutine.resume(false)
                }

        }


    }

    private fun generateProductID(): String {
        return Date().time.toString() // genera un ID para el producto con la fecha actual

    }


}