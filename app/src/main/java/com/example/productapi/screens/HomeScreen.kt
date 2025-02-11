package com.example.productapi.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.productapi.R
import com.example.productapi.data.Product
import com.example.productapi.dimension.FamilyDim
import com.example.productapi.dimension.FontDim
import com.example.productapi.navigation.Routes
import com.example.productapi.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val viewModel: ProductViewModel = viewModel()
    val products = viewModel.filteredProducts
    val isLoading = viewModel.isLoading
    val errorMessage = viewModel.errorMessage
    val searchQuery = viewModel.searchQuery

    LaunchedEffect(Unit) {
        viewModel.fetchProducts()
        viewModel.searchQuery
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Product List",
                maxLines = 1,
                letterSpacing = 1.sp,
                color = Black,
                textAlign = TextAlign.Center,
                fontSize = FontDim.extraLargeTextSize,
                overflow = TextOverflow.Visible,
                fontFamily = FamilyDim.Bold,) })
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate(Routes.AddProductScreen.routes) },
                containerColor = White,
                contentColor = Black,
                icon = { Icon(Icons.Filled.Add, "Add Products.") },
                text = {
                    Text(
                        text = "Add Products",
                        fontSize = FontDim.mediumTextSize,
                        fontFamily = FamilyDim.Normal
                    )
                })
        }
    ) { paddingValues ->
        Column(modifier = modifier.fillMaxSize().padding(paddingValues).padding(10.dp)) {
            SearchOutlineText(
                value = searchQuery,
                onValueChange = { viewModel.searchProducts(it) },
                label = "Search items",
                icons = Icons.Default.Search,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text, imeAction = ImeAction.Search
                )
            )
            Spacer(modifier = modifier.padding(4.dp))
            when {
                isLoading -> LoadingScreen()
                errorMessage != null -> ErrorScreen(errorMessage)
                else -> ProductList(products)
            }
        }
    }
}

@Composable
fun ProductList(products: List<Product>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        items(products) { product ->
            ProductItem(product)
        }
    }
}

@Composable
fun ProductItem(product: Product) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clip(RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(product.getImageOrPlaceholder()),  // Use error image if needed
                contentDescription = "Product Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentScale = ContentScale.Crop,
            )

            Text(
                text = product.product_name ?: "No Name",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "₹${product.price}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            product.description?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}


@Composable
fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorScreen(message: String?) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = message ?: "Unknown Error", color = Color.Red)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchOutlineText(
    modifier: Modifier = Modifier,
    value: String,
    icons: ImageVector,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardOptions: KeyboardOptions
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = icons,
                contentDescription = "",
                modifier = Modifier.padding(10.dp),
                tint = Black
            )
        },
        placeholder = {
            Text(
                text = "Enter your $label",
                fontSize = FontDim.mediumTextSize,
                fontFamily = FamilyDim.SemiBold,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Visible
            )
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedPlaceholderColor = Color.Gray,
            focusedPlaceholderColor = Color.Gray,
            focusedBorderColor = Black,
            unfocusedBorderColor = Black,
            focusedTextColor = Black,
            unfocusedTextColor = Black
        ),
        keyboardOptions = keyboardOptions,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(100.dp),
        minLines = 1
    )
}