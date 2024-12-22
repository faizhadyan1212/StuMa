// ProfileScreen.kt
package com.example.stuma.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.stuma.R
import com.example.stuma.data.repository.ProfileRepository
import com.example.stuma.utils.Result
import com.example.stuma.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    profileRepository: ProfileRepository
) {
    val profileViewModel = remember { ProfileViewModel(profileRepository) }
    val profileState by profileViewModel.profileState.collectAsState()

    LaunchedEffect(Unit) {
        profileViewModel.getProfile()
        profileViewModel.resetState()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(R.drawable.back),
                            contentDescription = "Back Icon",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        },
        content = { innerPadding ->
            ProfileContent(
                modifier = Modifier.padding(innerPadding),
                profileState = profileState,
                navController = navController
            )
        }
    )
}

@Composable
fun ProfileContent(
    modifier: Modifier = Modifier,
    profileState: Result<com.example.stuma.data.model.ProfileResponse>?,
    navController: NavController
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Your Profile",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        when (profileState) {
            is Result.Success -> {
                val profile = profileState.data

                // Profile Icon
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.user),
                        contentDescription = "Profile Icon",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(64.dp)
                    )
                }

                // Name Field
                OutlinedTextField(
                    value = profile.name,
                    onValueChange = {},
                    label = { Text("Name") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )

                // Phone Field
                OutlinedTextField(
                    value = profile.phone,
                    onValueChange = {},
                    label = { Text("Phone") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )

                // Address Field
                OutlinedTextField(
                    value = profile.address,
                    onValueChange = {},
                    label = { Text("Address") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                )

                // Edit Profile Button
                Button(
                    onClick = {
                        navController.navigate("edit_profile")
                    },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                ) {
                    Text("Edit Profile")
                }

                // Change Password Button
                Button(
                    onClick = {
                        navController.navigate("change_password")
                    },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                ) {
                    Text("Change Password")
                }

                // Logout Button
                Button(
                    onClick = {
                        navController.navigate("login") {
                            popUpTo("profile") { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Logout")
                }

            }
            is Result.Failure -> {
                val errorMessage = profileState.exception?.message ?: "Unknown error occurred"
                Text(
                    text = "Error: $errorMessage",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            is Result.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}
