package com.example.stuma.ui.screens.home

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import com.example.stuma.data.model.ItemResponse
import java.text.NumberFormat
import java.util.Locale

// Fungsi untuk memformat harga menjadi format Rupiah
fun formatToRupiah(amount: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    return format.format(amount)
}

@Composable
fun HomeScreen(navController: NavController, homeViewModel: HomeViewModel) {
    LaunchedEffect(Unit) {
        Log.d("HomeScreen", "Fetching items from HomeViewModel")
        homeViewModel.fetchItems()
    }

    Scaffold(
        topBar = { HomeTopBar(navController, homeViewModel) },
        bottomBar = { HomeBottomBar(navController) }
    ) { innerPadding ->
        HomeContent(
            modifier = Modifier.padding(innerPadding),
            homeViewModel = homeViewModel,
            navController = navController
        )
    }
}

@Composable
fun HomeTopBar(navController: NavController, homeViewModel: HomeViewModel) {
    var searchQuery by remember { mutableStateOf("") }

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
                value = searchQuery,
                onValueChange = { query ->
                    searchQuery = query
                    homeViewModel.searchItems(query)
                },
                placeholder = { Text("Search items...") },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                shape = RoundedCornerShape(8.dp)
            )

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clickable { navController.navigate("cart") },
                contentAlignment = Alignment.Center
            ) {
                CartIcon()
            }
        }

        FilterRow(homeViewModel)
    }
}

@Composable
fun FilterRow(homeViewModel: HomeViewModel) {
    val filters = listOf("All", "Clothes", "Stationery", "Furniture", "Electronic")
    val selectedFilter by homeViewModel.selectedCategory.collectAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        filters.forEach { filter ->
            TextButton(
                onClick = { homeViewModel.filterItemsByCategory(filter) },
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
            onClick = { navController.navigate("wishlist") },
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
fun HomeContent(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel,
    navController: NavController
) {
    val filteredItems by homeViewModel.filteredItemsState.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = "Welcome to StuMa!",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        if (filteredItems.isEmpty()) {
            item {
                Text(
                    text = "No items available.",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        } else {
            items(filteredItems) { item ->
                ItemCard(item = item, navController = navController)
            }
        }
    }
}

@Composable
fun ItemCard(item: ItemResponse, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("details/${item.id}") }
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
            Text(
                text = "Name: ${item.items_name}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Price: ${formatToRupiah(item.price)}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Stock: ${item.stock}",
                style = MaterialTheme.typography.bodyMedium
            )
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
