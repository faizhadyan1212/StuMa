package com.example.stuma.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.stuma.data.model.ChangePasswordRequest
import com.example.stuma.data.repository.AuthRepository
import com.example.stuma.utils.Result
import com.example.stuma.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    navController: NavController,
    authRepository: AuthRepository // Pastikan repository diteruskan dari MainActivity
) {
    // Inisialisasi AuthViewModel secara manual
    val authViewModel = remember { AuthViewModel(authRepository) }

    // State untuk input form
    var email by remember { mutableStateOf("") }
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    val changePasswordState by authViewModel.changePasswordState.collectAsState()

    // Reset state saat layar dimuat ulang
    LaunchedEffect(Unit) {
        authViewModel.resetChangePasswordState()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Change Password") },
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
            ChangePasswordContent(
                modifier = Modifier.padding(innerPadding),
                email = email,
                oldPassword = oldPassword,
                newPassword = newPassword,
                onEmailChange = { email = it },
                onOldPasswordChange = { oldPassword = it },
                onNewPasswordChange = { newPassword = it },
                onSubmitClick = {
                    authViewModel.changePassword(
                        ChangePasswordRequest(email, oldPassword, newPassword)
                    )
                },
                changePasswordState = changePasswordState,
                navController = navController
            )
        }
    )
}

@Composable
fun ChangePasswordContent(
    modifier: Modifier = Modifier,
    email: String,
    oldPassword: String,
    newPassword: String,
    onEmailChange: (String) -> Unit,
    onOldPasswordChange: (String) -> Unit,
    onNewPasswordChange: (String) -> Unit,
    onSubmitClick: () -> Unit,
    changePasswordState: Result<com.example.stuma.data.model.ApiResponse>?,
    navController: NavController
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Change Password",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = oldPassword,
            onValueChange = onOldPasswordChange,
            label = { Text("Old Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = newPassword,
            onValueChange = onNewPasswordChange,
            label = { Text("New Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onSubmitClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Change Password")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (changePasswordState) {
            is Result.Success -> {
                Text(
                    text = "Password changed successfully!",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium
                )
                LaunchedEffect(Unit) {
                    navController.navigate("login") {
                        popUpTo("change_password") { inclusive = true }
                    }
                }
            }
            is Result.Failure -> {
                changePasswordState.exception?.message?.let { errorMessage ->
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
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
                // Default state
            }
        }
    }
}
