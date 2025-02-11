package com.example.productapi.data

data class ProductRequest(
    val productType: String,
    val productName: String,
    val sellingPrice: Float,
    val taxRate: Float,
    val imageBase64: String? = null
)

data class ApiResponse(
    val success: Boolean,
    val message: String
)
