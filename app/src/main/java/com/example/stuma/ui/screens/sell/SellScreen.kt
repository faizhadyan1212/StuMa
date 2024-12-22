package com.example.stuma.ui.screens.sell

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.stuma.data.model.ItemRequest
import com.example.stuma.viewmodel.SellViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellScreen(
    navController: NavController,
    sellViewModel: SellViewModel
) {
    // State untuk form input
    var itemName by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    // State untuk validasi error
    var errorMessage by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Sell Item") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                // Input Fields
                OutlinedTextField(
                    value = itemName,
                    onValueChange = { itemName = it },
                    label = { Text("Item Name") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )

                DropdownMenuContent(
                    selectedCategory = category,
                    onCategorySelected = { category = it }
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = stock,
                    onValueChange = { stock = it },
                    label = { Text("Stock") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Price") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                )

                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                // Sell Button
                Button(
                    onClick = {
                        if (validateInput(itemName, category, description, stock, price)) {
                            val itemRequest = ItemRequest(
                                name = itemName,
                                category = category,
                                description = description,
                                stock = stock.toInt(),
                                price = price.toDouble()
                            )
                            Log.d("SellScreen", "Sending item data: $itemRequest")
                            sellViewModel.sellItem(itemRequest)
                            navController.popBackStack()
                        } else {
                            errorMessage = "Please fill out all fields correctly."
                            Log.d("SellScreen", "Validation failed: $itemName, $category, $description, $stock, $price")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Sell")
                }
            }
        }
    )
}

@Composable
fun DropdownMenuContent(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val categories = listOf("Furniture", "Clothes", "Electronic", "Stationary")

    Box {
        OutlinedTextField(
            value = selectedCategory,
            onValueChange = {},
            label = { Text("Category") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(Icons.Filled.ArrowDropDown, contentDescription = null)
                }
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = { Text(category) },
                    onClick = {
                        onCategorySelected(category)
                        expanded = false
                    }
                )
            }
        }
    }
}

fun validateInput(
    itemName: String,
    category: String,
    description: String,
    stock: String,
    price: String
): Boolean {
    return itemName.isNotBlank() &&
            category.isNotBlank() &&
            description.isNotBlank() &&
            stock.toIntOrNull() != null &&
            price.toDoubleOrNull() != null
}
