package com.example.stuma.ui.screens.details

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.stuma.data.model.ItemResponse
import com.example.stuma.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailScreen(navController: NavController, item: ItemResponse, homeViewModel: HomeViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = item.name) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack, // Gunakan icon bawaan
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Name: ${item.name}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Price: ${item.price}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Stock: ${item.stock}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Category: ${item.category}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Description: ${item.description}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Seller ID: ${item.user_id}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = { homeViewModel.addToWishlist(item) }) {
                    Text("Add to Wishlist")
                }

                Button(onClick = { homeViewModel.addToCart(item) }) {
                    Text("Add to Cart")
                }
            }
        }
    }
}
