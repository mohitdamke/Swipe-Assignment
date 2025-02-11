package com.example.productapi.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.productapi.dimension.FamilyDim
import com.example.productapi.dimension.FontDim
import com.example.productapi.navigation.Routes
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    LaunchedEffect(key1 = Unit) {
        delay(3000L)
        navController.navigate(Routes.HomeScreen.routes)
    }



    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Swipe Product \nApi App",
            fontSize = FontDim.extraLargeTextSize,
            fontWeight = FontWeight.W700,
            fontFamily = FamilyDim.Bold
        )
    }
}