package com.example.productapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.productapi.navigation.NavigationControl
import com.example.productapi.ui.theme.ProductApiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProductApiTheme {
                NavigationControl()
            }
        }
    }
}
