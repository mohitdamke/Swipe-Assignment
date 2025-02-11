package com.example.productapi.domain

import android.util.Log
import com.example.productapi.data.ApiResponse
import com.example.productapi.data.Product
import com.example.productapi.data.ProductRequest

class ProductRepository() {

    private val apiService = RetrofitInstance.api  // Ensure consistency

    suspend fun getProducts(): List<Product> {
        return RetrofitInstance.api.getProducts()
    }
        suspend fun addProduct(product: ProductRequest): ApiResponse {
            return try {
                apiService.addProduct(product)
            } catch (e: Exception) {
                Log.e("API_ERROR", "Exception: ${e.localizedMessage}", e)
                throw e  // Re-throw to let ViewModel handle it
            }
        }
    }

