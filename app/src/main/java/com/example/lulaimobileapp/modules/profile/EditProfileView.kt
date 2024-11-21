package com.example.lulaimobileapp.modules.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lulaimobileapp.networking.APIService
import com.example.lulaimobileapp.utils.DataStoreManager
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@Composable
fun EditProfileView(navController: NavController, dataStoreManager: DataStoreManager) {
    val coroutineScope = rememberCoroutineScope()
    var username by remember { mutableStateOf("") }
    var showAlert by remember { mutableStateOf(false) }
    var alertMessage by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val token = dataStoreManager.getToken().firstOrNull() // Ensure this returns String?
            if (!token.isNullOrEmpty()) {
                val user = APIService.fetchUser(token)
                username = user?.userName ?: "" // Default to an empty string if null
            } else {
                showAlert = true
                alertMessage = "Failed to fetch user data. Please try again."
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(remember { SnackbarHostState() }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Edit Profile",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    coroutineScope.launch {
                        val token = dataStoreManager.getToken().firstOrNull()
                        alertMessage = when {
                            token.isNullOrEmpty() -> "Failed to update profile. Token is missing."
                            APIService.updateUserProfile(token, username) -> "Profile successfully updated!"
                            else -> "Failed to update profile."
                        }
                        showAlert = true
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Changes")
            }
        }

        if (showAlert) {
            AlertDialog(
                onDismissRequest = { showAlert = false },
                title = { Text("Message") },
                text = { Text(alertMessage) },
                confirmButton = {
                    TextButton(onClick = {
                        showAlert = false
                        if (alertMessage == "Profile successfully updated!") {
                            navController.popBackStack()
                        }
                    }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}
