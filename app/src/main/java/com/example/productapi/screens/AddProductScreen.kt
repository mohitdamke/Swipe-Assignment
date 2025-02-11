package com.example.productapi.screens

import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.productapi.dimension.FamilyDim
import com.example.productapi.dimension.FontDim
import com.example.productapi.viewmodel.ProductViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {

    val viewModel: ProductViewModel = viewModel()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val isLoading = viewModel.isLoading
    val message = viewModel.message
    val productName by viewModel::productName
    val sellingPrice by viewModel::sellingPrice
    val taxRate by viewModel::taxRate


    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
            viewModel.selectedImage = bitmap
        }
    }
    Scaffold(modifier = modifier
        .fillMaxSize()
        .background(White)
        .nestedScroll(scrollBehavior.nestedScrollConnection), topBar = {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = White,
                actionIconContentColor = White,
                navigationIconContentColor = White,
                scrolledContainerColor = Transparent,
            ),
            title = {
                Text(
                    text = "Add Product",
                    maxLines = 1,
                    letterSpacing = 1.sp,
                    color = Black,
                    textAlign = TextAlign.Center,
                    fontSize = FontDim.extraLargeTextSize,
                    overflow = TextOverflow.Visible,
                    fontFamily = FamilyDim.Bold,
                )
            },
            scrollBehavior = scrollBehavior,
            navigationIcon = {
                Icon(
                    Icons.Rounded.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            scope.launch {
                                navController.navigateUp()
                            }
                        },
                    tint = Black
                )
            },
        )
    }) { paddingValue ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
                .padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(100.dp)  // Adjust circle size
                    .clip(CircleShape)
                    .background(Color.Gray)
                    .clickable { launcher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (viewModel.selectedImage != null) {
                    Image(
                        bitmap = viewModel.selectedImage!!.asImageBitmap(),
                        contentDescription = "Selected Image",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Default Profile",
                        modifier = Modifier.size(100.dp),
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = { launcher.launch("image/*") }) {
                Text(text = "Select Image")
            }

            Spacer(modifier = Modifier.height(18.dp))

            ProductTypeDropdown(viewModel)
            Spacer(modifier = Modifier.height(18.dp))

            AddProductOutlineText(
                value = viewModel.productName,
                onValueChange = { viewModel.productName = it },  // ✅ Updating ViewModel directly
                label = "Product Name",
                icons = Icons.Default.ShoppingCart,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(18.dp))

            AddProductOutlineText(
                value = viewModel.sellingPrice,
                onValueChange = { viewModel.sellingPrice = it },  // ✅ Updating ViewModel directly
                label = "Selling Price",
                icons = Icons.Default.Edit,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(18.dp))

            AddProductOutlineText(
                value = viewModel.taxRate,
                onValueChange = { viewModel.taxRate = it },  // ✅ Updating ViewModel directly
                label = "Tax Rate",
                icons = Icons.Default.Edit,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
                )
            )

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = {
                    scope.launch { viewModel.submitProduct() }
                }, modifier = modifier
                    .fillMaxWidth()
                    .padding(2.dp)
            ) {
                Text(text = "Submit", fontSize = FontDim.largeTextSize)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Show loading indicator in center
            if (isLoading) {
                Box(modifier = modifier) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        message?.let { message ->
            AlertDialog(
                onDismissRequest = {},
                confirmButton = {
                    Button(onClick = {
                        viewModel.message = null
                    }) { Text("OK") }
                },
                text = { Text(text = message) })
        }
    }
}

@Composable
fun ProductTypeDropdown(viewModel: ProductViewModel) {
    val options = listOf("Electronics", "Clothing", "Accessories", "Home Appliances")
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.TopStart)
                .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(81.dp))
                .clip(RoundedCornerShape(18.dp))
                .background(MaterialTheme.colorScheme.surface)
                .clickable { expanded = true }
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = viewModel.productType.ifEmpty { "Select Product Type" },
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (viewModel.productType.isEmpty()) Color.Gray else MaterialTheme.colorScheme.onSurface
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown Icon",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option, style = MaterialTheme.typography.bodyLarge) },
                    onClick = {
                        viewModel.productType = option
                        expanded = false
                    },
                    leadingIcon = {
                        if (viewModel.productType == option) {
                            Icon(
                                imageVector = Icons.Outlined.Check,
                                contentDescription = "Selected",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddProductOutlineText(
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
                tint = Gray
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
            focusedBorderColor = Gray,
            unfocusedBorderColor = Gray,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black
        ),
        keyboardOptions = keyboardOptions,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(100.dp),
        minLines = 1
    )
}
