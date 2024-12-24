package com.example.stuma.ui.screens.details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.stuma.data.model.ItemResponse
import com.example.stuma.viewmodel.HomeViewModel
import java.text.NumberFormat
import java.util.Locale

// Fungsi untuk memformat harga menjadi format Rupiah
fun formatToRupiah(amount: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    return format.format(amount)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailScreen(navController: NavController, item: ItemResponse, homeViewModel: HomeViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = item.items_name) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Informasi utama
            Text(
                text = item.items_name,
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = "Category: ${item.category}",
                style = MaterialTheme.typography.bodyMedium
            )
            Divider()

            // Deskripsi dengan "See More"
            DescriptionSection(description = item.description)

            Divider()

            // Informasi tambahan
            Text(
                text = "Price: ${formatToRupiah(item.price)}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Stock: ${item.stock}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Seller: ${item.seller_name}",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tombol aksi
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { homeViewModel.addToWishlist(item) },
                    modifier = Modifier.weight(1f) // Membuat tombol menyesuaikan ruang
                ) {
                    Text("Add to Wishlist")
                }
                Button(
                    onClick = { homeViewModel.addToCart(item) },
                    modifier = Modifier.weight(1f) // Membuat tombol menyesuaikan ruang
                ) {
                    Text("Add to Cart")
                }
                Button(
                    onClick = { navController.navigate("payment/${item.price}") },
                    modifier = Modifier.weight(1f) // Membuat tombol menyesuaikan ruang
                ) {
                    Text("Buy Item")
                }
            }
        }
    }
}

@Composable
fun DescriptionSection(description: String) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        if (expanded) {
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        } else {
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        ClickableText(
            text = AnnotatedString(if (expanded) "See Less" else "See More"),
            onClick = { expanded = !expanded },
            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.primary)
        )
    }
}
