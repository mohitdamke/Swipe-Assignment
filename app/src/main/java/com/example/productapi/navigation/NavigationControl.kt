package com.example.productapi.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.productapi.screens.AddProductScreen
import com.example.productapi.screens.HomeScreen
import com.example.productapi.screens.SplashScreen


@Composable
fun NavigationControl() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.SplashScreen.routes) {

        composable(route = Routes.SplashScreen.routes) {
            SplashScreen(navController = navController)
        }
        composable(route = Routes.HomeScreen.routes) {
            HomeScreen(navController = navController)
        }
        composable(route = Routes.AddProductScreen.routes) {
            AddProductScreen(navController = navController)
        }
    }
}