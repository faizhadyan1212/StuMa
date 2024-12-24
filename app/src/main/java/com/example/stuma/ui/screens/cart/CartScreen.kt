package com.example.stuma.ui.screens.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.stuma.R
import com.example.stuma.data.model.ItemResponse
import com.example.stuma.viewmodel.HomeViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(navController: NavController, homeViewModel: HomeViewModel) {
    val cart by homeViewModel.cart.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cart") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(R.drawable.back),
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
                .padding(16.dp)
        ) {
            if (cart.isNotEmpty()) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(cart.keys.toList()) { item ->
                        CartItemCard(
                            item = item,
                            quantity = cart[item] ?: 0,
                            onIncrease = { homeViewModel.addToCart(item) },
                            onDecrease = { homeViewModel.removeFromCart(item) },
                            onBuy = { /* Navigate to checkout or trigger purchase logic */ }
                        )
                    }
                }
            } else {
                Text(
                    text = "Your cart is empty.",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun CartItemCard(
    item: ItemResponse,
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onBuy: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "Name: ${item.items_name}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Price: Rp ${item.price.toCurrency()}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Stock: ${item.stock}", style = MaterialTheme.typography.bodyMedium)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = onDecrease, enabled = quantity > 0) {
                    Text("-")
                }
                Text(
                    text = quantity.toString(),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                Button(onClick = onIncrease, enabled = quantity < item.stock) {
                    Text("+")
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = onDecrease) {
                    Text("Remove from Cart")
                }
                Button(onClick = onBuy) {
                    Text("Buy")
                }
            }
        }
    }
}

fun Double.toCurrency(): String {
    val format = NumberFormat.getNumberInstance(Locale("id", "ID"))
    return format.format(this)
}
