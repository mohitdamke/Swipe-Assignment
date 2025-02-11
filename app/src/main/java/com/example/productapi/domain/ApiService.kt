package com.example.productapi.domain

import com.example.productapi.data.ApiResponse
import com.example.productapi.data.Product
import com.example.productapi.data.ProductRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @GET("public/get")
    suspend fun getProducts(): List<Product>

    @Headers("Content-Type: application/json")
    @POST("public/add")
   suspend fun addProduct(@Body product:ProductRequest):ApiResponse

}
