package com.example.stuma.ui.screens.home

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.stuma.R
import com.example.stuma.viewmodel.HomeViewModel
import com.example.stuma.utils.Result
import com.example.stuma.data.model.ItemResponse

@Composable
fun HomeScreen(navController: NavController, homeViewModel: HomeViewModel) {
    // Memastikan data diambil setiap kali HomeScreen dimuat
    LaunchedEffect(Unit) {
        Log.d("HomeScreen", "Fetching items from HomeViewModel")
        homeViewModel.fetchItems()
    }

    Scaffold(
        topBar = { HomeTopBar(navController) },
        bottomBar = { HomeBottomBar(navController) }
    ) { innerPadding ->
        HomeContent(
            modifier = Modifier.padding(innerPadding),
            homeViewModel = homeViewModel
        )
    }
}

@Composable
fun HomeTopBar(navController: NavController) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clickable { navController.navigate("profile") },
                contentAlignment = Alignment.Center
            ) {
                ProfileIcon()
            }

            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Search items...") },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                shape = RoundedCornerShape(8.dp)
            )

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clickable { /* Future cart implementation */ },
                contentAlignment = Alignment.Center
            ) {
                CartIcon()
            }
        }

        FilterRow()
    }
}

@Composable
fun FilterRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val filters = listOf("All", "Clothes", "Stationary", "Furniture", "Electronic")
        var selectedFilter by remember { mutableStateOf("All") }

        filters.forEach { filter ->
            TextButton(
                onClick = { selectedFilter = filter },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = if (selectedFilter == filter) MaterialTheme.colorScheme.primary else Color.Gray
                )
            ) {
                Text(
                    text = filter,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun HomeBottomBar(navController: NavController) {
    NavigationBar {
        NavigationBarItem(
            selected = false,
            onClick = { /* Future wishlist implementation */ },
            icon = { WishlistIcon() },
            label = { Text("Wishlist") }
        )
        NavigationBarItem(
            selected = true,
            onClick = { /* Stay on Home */ },
            icon = { HomeIcon() },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("sell") },
            icon = { AddIcon() },
            label = { Text("Add") }
        )
    }
}

@Composable
fun HomeContent(modifier: Modifier = Modifier, homeViewModel: HomeViewModel) {
    val itemsState by homeViewModel.itemsState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Welcome to StuMa!",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        when (val state = itemsState) {
            is Result.Loading -> {
                Log.d("HomeContent", "Loading items...")
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            is Result.Success -> {
                Log.d("HomeContent", "Items fetched successfully: ${state.data.size} items")
                if (state.data.isNotEmpty()) {
                    ItemList(items = state.data)
                } else {
                    Text(
                        text = "No items available.",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
            is Result.Failure -> {
                Log.e("HomeContent", "Error fetching items", state.exception)
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Error: ${state.exception?.message ?: "Unknown error"}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { homeViewModel.fetchItems() }) {
                        Text("Retry")
                    }
                }
            }
            null -> {
                Log.d("HomeContent", "No items available.")
                Text(
                    text = "No items available.",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun ItemList(items: List<ItemResponse>) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items.forEach { item ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = "Name: ${item.name}", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Price: ${item.price}", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Stock: ${item.stock}", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

@Composable
fun ProfileIcon() {
    Icon(
        painter = painterResource(R.drawable.user),
        contentDescription = "Profile Icon",
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier.size(24.dp)
    )
}

@Composable
fun CartIcon() {
    Icon(
        painter = painterResource(R.drawable.shopping_cart),
        contentDescription = "Cart Icon",
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier.size(24.dp)
    )
}

@Composable
fun WishlistIcon() {
    Icon(
        painter = painterResource(R.drawable.wishlist),
        contentDescription = "Wishlist Icon",
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier.size(24.dp)
    )
}

@Composable
fun HomeIcon() {
    Icon(
        painter = painterResource(R.drawable.home),
        contentDescription = "Home Icon",
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier.size(24.dp)
    )
}

@Composable
fun AddIcon() {
    Icon(
        painter = painterResource(R.drawable.add),
        contentDescription = "Add Icon",
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier.size(24.dp)
    )
}
