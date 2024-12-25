package com.example.stuma.ui.screens.details

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmationScreen(
    navController: NavController,
    paymentMethod: String,
    deliveryOption: String,
    address: String,
    totalPrice: Double
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Payment Successful") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
            // Detail Pembayaran
            Text("Payment Details", style = MaterialTheme.typography.headlineSmall)

            Spacer(modifier = Modifier.height(16.dp))

            Text("Payment Method: $paymentMethod", style = MaterialTheme.typography.bodyLarge)
            Text("Delivery Option: $deliveryOption", style = MaterialTheme.typography.bodyLarge)
            Text("Delivery Address: $address", style = MaterialTheme.typography.bodyLarge)
            Text("Total Amount: Rp ${"%,.2f".format(totalPrice)}", style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(16.dp))

            // Tombol untuk kembali ke halaman utama atau keluar
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back to Home")
            }
        }
    }
}
