package com.example.stuma.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.stuma.data.model.UpdateProfileRequest
import com.example.stuma.data.repository.ProfileRepository
import com.example.stuma.utils.Result
import com.example.stuma.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController,
    profileRepository: ProfileRepository
) {
    // Inisialisasi ProfileViewModel secara manual
    val profileViewModel = remember { ProfileViewModel(profileRepository) }

    // State untuk form input
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    val profileState by profileViewModel.profileState.collectAsState()
    val updateProfileState by profileViewModel.updateProfileState.collectAsState()

    // Muat data profil saat pertama kali
    LaunchedEffect(Unit) {
        profileViewModel.getProfile()
    }

    // Perbarui form dengan data profil
    LaunchedEffect(profileState) {
        if (profileState is Result.Success) {
            val profile = (profileState as Result.Success).data
            name = profile.name
            phone = profile.phone
            address = profile.address
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Edit Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        content = { innerPadding ->
            EditProfileContent(
                modifier = Modifier.padding(innerPadding),
                name = name,
                phone = phone,
                address = address,
                onNameChange = { name = it },
                onPhoneChange = { phone = it },
                onAddressChange = { address = it },
                onSaveClick = {
                    profileViewModel.updateProfile(
                        UpdateProfileRequest(name, phone, address)
                    )
                },
                updateProfileState = updateProfileState,
                navController = navController
            )
        }
    )
}

@Composable
fun EditProfileContent(
    modifier: Modifier = Modifier,
    name: String,
    phone: String,
    address: String,
    onNameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    updateProfileState: Result<com.example.stuma.data.model.ApiResponse>?,
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
            text = "Update Your Profile",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Input untuk Name
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Input untuk Phone
        OutlinedTextField(
            value = phone,
            onValueChange = onPhoneChange,
            label = { Text("Phone") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Input untuk Address
        OutlinedTextField(
            value = address,
            onValueChange = onAddressChange,
            label = { Text("Address") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Tombol Save
        Button(
            onClick = onSaveClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Status pembaruan
        when (updateProfileState) {
            is Result.Success -> {
                Text(
                    text = "Profile updated successfully!",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 16.dp)
                )
                // Navigasi kembali ke ProfileScreen
                LaunchedEffect(Unit) {
                    navController.popBackStack()
                }
            }
            is Result.Failure -> {
                Text(
                    text = (updateProfileState as Result.Failure).exception?.message
                        ?: "Update failed",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 16.dp)
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
                // Default state: tidak melakukan apapun
            }
        }
    }
}
