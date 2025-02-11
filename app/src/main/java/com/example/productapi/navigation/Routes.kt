package com.example.productapi.navigation

sealed class Routes (val routes: String) {
    data object SplashScreen :Routes("SplashScreen")
    data object HomeScreen :Routes("HomeScreen")
    data object AddProductScreen :Routes("AddProductScreen")

}