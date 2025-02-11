package com.example.productapi.viewmodel

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productapi.data.Product
import com.example.productapi.data.ProductRequest
import com.example.productapi.domain.ProductRepository
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class ProductViewModel : ViewModel() {
    private val repository = ProductRepository()

    var products by mutableStateOf<List<Product>>(emptyList())
        private set

    var filteredProducts by mutableStateOf<List<Product>>(emptyList()) // Filtered list
        private set

    var searchQuery by mutableStateOf("") // Search query
        private set

    var productType by mutableStateOf("")
    var productName by mutableStateOf("")
    var sellingPrice by mutableStateOf("")
    var taxRate by mutableStateOf("")
    var selectedImage by mutableStateOf<Bitmap?>(null)
    var message by mutableStateOf<String?>(null)

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun fetchProducts() {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = repository.getProducts()
                if (response.isNotEmpty()) {
                    products = response
                    filteredProducts = response // Initially, show all products
                    errorMessage = null
                } else {
                    errorMessage = "No products found"
                }
                Log.d("API_RESPONSE", "Fetched products: $response")  // Debugging
            } catch (e: Exception) {
                errorMessage = "Failed to load products"
                Log.e("API_ERROR", "Error: ${e.localizedMessage}", e)
            } finally {
                isLoading = false
            }
        }
    }

    // 🔍 Search function
    fun searchProducts(query: String) {
        searchQuery = query
        filteredProducts = if (query.isBlank()) {
            products // Show all products if query is empty
        } else {
            products.filter { it.product_name?.contains(query, ignoreCase = true) == true }
        }
    }



    fun submitProduct() {
        if (!validateInputs()) return

        val product = ProductRequest(
            productType = productType,
            productName = productName,
            sellingPrice = sellingPrice.toFloat(),
            taxRate = taxRate.toFloat(),
            imageBase64 = selectedImage?.let { bitmapToBase64(it) }
        )

        Log.d("API_REQUEST", "Sending Product: $product") // Debugging

        viewModelScope.launch {
            isLoading = true
            try {
                val response = repository.addProduct(product)
                Log.d("API_RESPONSE", "Response: $response") // Debugging
                message = response.message
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error: ${e.localizedMessage}", e)
                message = "Error: ${e.localizedMessage}"
            } finally {
                isLoading = false  // Ensure loading stops
            }
        }
    }


    private fun validateInputs(): Boolean {
        return when {
            productType.isBlank() -> {
                message = "Select a product type"
                false
            }

            productName.isBlank() -> {
                message = "Enter product name"
                false
            }

            sellingPrice.toFloatOrNull() == null -> {
                message = "Enter a valid selling price"
                false
            }

            taxRate.toFloatOrNull() == null -> {
                message = "Enter a valid tax rate"
                false
            }

            else -> true
        }
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
    }
}
