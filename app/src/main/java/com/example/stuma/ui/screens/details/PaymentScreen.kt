package com.example.stuma.ui.screens.details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(navController: NavController, initialPrice: Double, initialAddress: String) {
    var paymentMethod by remember { mutableStateOf("Cash on Delivery") }
    var deliveryOption by remember { mutableStateOf("Cepat") }
    var address by remember { mutableStateOf(initialAddress) }
    var quantity by remember { mutableStateOf(1) } // Menyimpan jumlah barang yang dibeli

    val deliveryFee = when (deliveryOption) {
        "Sangat Cepat" -> 10000.0
        "Flash" -> 30000.0
        else -> 0.0
    }
    val totalPrice = (initialPrice * quantity) + deliveryFee // Mengalikan harga barang dengan jumlah

    // Fungsi untuk format harga menjadi Rupiah
    fun formatToRupiah(amount: Double): String {
        val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        return format.format(amount)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Payment") },
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()), // Menambahkan scroll vertikal
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Metode Pembayaran
            Text("Payment Method", style = MaterialTheme.typography.bodyLarge)
            DropdownMenuContent(
                options = listOf("Cash on Delivery", "DANA", "GoPay", "OVO"),
                selectedOption = paymentMethod,
                onOptionSelected = { paymentMethod = it }
            )

            // Opsi Pengiriman
            Text("Delivery Option", style = MaterialTheme.typography.bodyLarge)
            DropdownMenuContent(
                options = listOf("Cepat", "Sangat Cepat", "Flash"),
                selectedOption = deliveryOption,
                onOptionSelected = { deliveryOption = it }
            )

            // Alamat
            Text("Delivery Address", style = MaterialTheme.typography.bodyLarge)
            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                modifier = Modifier.fillMaxWidth()
            )

            // Jumlah Barang
            Text("Quantity", style = MaterialTheme.typography.bodyLarge)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = { if (quantity > 1) quantity-- },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(Icons.Filled.Remove, contentDescription = "Decrease Quantity")
                }
                Text(
                    text = "$quantity",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                IconButton(
                    onClick = { quantity++ },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Increase Quantity")
                }
            }

            // Total Harga
            Text(
                text = "Total: ${formatToRupiah(totalPrice)}",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tombol Konfirmasi
            Button(
                onClick = {
                    // Pastikan totalPrice dikirim dengan benar ke halaman konfirmasi
                    navController.navigate("confirmation/$paymentMethod/$deliveryOption/$address/${totalPrice.toString()}")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Confirm and Pay")
            }
        }
    }
}

@Composable
fun DropdownMenuContent(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        // Input Field dengan Icon Dropdown
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            label = { Text("Select Option") },
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Expand Dropdown")
                }
            }
        )

        // Dropdown Menu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        onOptionSelected(option) // Update pilihan
                        expanded = false        // Tutup menu
                    },
                    text = { Text(option) }
                )
            }
        }
    }
}
