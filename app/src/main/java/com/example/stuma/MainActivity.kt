package com.example.stuma

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.stuma.data.repository.AuthRepository
import com.example.stuma.data.repository.HomeRepository
import com.example.stuma.data.repository.ProfileRepository
import com.example.stuma.data.repository.SellRepository
import com.example.stuma.ui.screens.auth.LoginScreen
import com.example.stuma.ui.screens.auth.RegisterScreen
import com.example.stuma.ui.screens.cart.CartScreen
import com.example.stuma.ui.screens.details.ItemDetailScreen
import com.example.stuma.ui.screens.home.HomeScreen
import com.example.stuma.ui.screens.profile.EditProfileScreen
import com.example.stuma.ui.screens.profile.ProfileScreen
import com.example.stuma.ui.screens.sell.SellScreen
import com.example.stuma.ui.screens.settings.ChangePasswordScreen
import com.example.stuma.ui.screens.wishlist.WishlistScreen
import com.example.stuma.ui.theme.StuMaTheme
import com.example.stuma.utils.TokenManager
import com.example.stuma.viewmodel.AuthViewModel
import com.example.stuma.viewmodel.HomeViewModel
import com.example.stuma.viewmodel.SellViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Instansiasi TokenManager
        val tokenManager = TokenManager(applicationContext)

        // Instansiasi Repositories
        val profileRepository = ProfileRepository(applicationContext)
        val authRepository = AuthRepository(tokenManager)
        val homeRepository = HomeRepository(tokenManager)
        val sellRepository = SellRepository(tokenManager) // Berikan tokenManager di sini

        setContent {
            StuMaTheme {
                MainNavigation(
                    profileRepository = profileRepository,
                    authRepository = authRepository,
                    homeRepository = homeRepository,
                    sellRepository = sellRepository,
                    tokenManager = tokenManager
                )
            }
        }
    }
}

@Composable
fun MainNavigation(
    profileRepository: ProfileRepository,
    authRepository: AuthRepository,
    homeRepository: HomeRepository,
    sellRepository: SellRepository,
    tokenManager: TokenManager
) {
    val navController = rememberNavController()

    val homeViewModel = remember { HomeViewModel(homeRepository) }
    val authViewModel = remember { AuthViewModel(authRepository) }
    val sellViewModel = remember { SellViewModel(sellRepository) }

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        // Login Screen
        composable("login") {
            LoginScreen(navController = navController, authViewModel = authViewModel)
        }

        // Register Screen
        composable("register") {
            RegisterScreen(navController = navController, authViewModel = authViewModel)
        }

        // Home Screen
        composable("home") {
            HomeScreen(
                navController = navController,
                homeViewModel = homeViewModel
            )
        }

        // Profile Screen
        composable("profile") {
            ProfileScreen(
                navController = navController,
                profileRepository = profileRepository
            )
        }

        // Edit Profile Screen
        composable("edit_profile") {
            EditProfileScreen(
                navController = navController,
                profileRepository = profileRepository
            )
        }

        // Change Password Screen
        composable("change_password") {
            ChangePasswordScreen(
                navController = navController,
                authRepository = authRepository
            )
        }
        // Details Screen
        composable("details/{itemId}") { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId")?.toIntOrNull()

            // Ambil item berdasarkan ID
            if (itemId != null) {
                val item = homeViewModel.getItemById(itemId) // Pastikan fungsi ini tersedia di HomeViewModel
                if (item != null) {
                    ItemDetailScreen(navController, item, homeViewModel)
                } else {
                    Text("Item not found") // Tampilkan pesan jika item tidak ditemukan
                }
            } else {
                Text("Invalid item ID") // Tampilkan pesan jika ID tidak valid
            }
        }

        composable("cart") {
            CartScreen(navController = navController, homeViewModel = homeViewModel)
        }

        composable("wishlist") {
            WishlistScreen(
                navController = navController,
                homeViewModel = homeViewModel
            )
        }
        // Sell Screen
        composable("sell") {
            SellScreen(
                navController = navController,
                sellViewModel = sellViewModel
            )
        }
    }
}
