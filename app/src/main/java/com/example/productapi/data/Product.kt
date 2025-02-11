package com.example.productapi.data

data class Product(
    val product_name: String? = "No Name",
    val price: String? = "0",
    val description: String? = "No Description",
    val image: String? = null
) {
    // Function to get a valid image URL or fallback error image
    fun getImageOrPlaceholder(): String {
        return if (image.isNullOrBlank()) {
            "https://as1.ftcdn.net/v2/jpg/09/38/32/80/1000_F_938328025_t3LcT034ZelstQa26VCuXn4MjDooyuRW.jpg" // Replace with your error image URL
        } else {
            image
        }
    }
}
