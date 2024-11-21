package com.example.lulaimobileapp.modules.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lulaimobileapp.utils.DataStoreManager
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@Composable
fun ProfileView(navController: NavController, dataStoreManager: DataStoreManager) {
    val coroutineScope = rememberCoroutineScope()
    var username by remember { mutableStateOf<String?>(null) }
    var email by remember { mutableStateOf<String?>(null) }

    // Load user data from DataStore
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            username = dataStoreManager.getUsername().firstOrNull()
            email = dataStoreManager.getEmail().firstOrNull()
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
            if (username != null && email != null) {
                Text(
                    text = "Username: ${username ?: "N/A"}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Email: ${email ?: "N/A"}",
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                Text(
                    text = "Loading user data...",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Navigation Options
            Button(
                onClick = { navController.navigate("editProfile") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Edit Profile")
            }
            Button(
                onClick = { navController.navigate("manageSubscription") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Manage Subscription")
            }
            Button(
                onClick = { navController.navigate("deleteAccount") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Delete Account")
            }

            Spacer(modifier = Modifier.weight(1f))

            // Logout Button
            Button(
                onClick = {
                    coroutineScope.launch {
                        dataStoreManager.setLoggedIn(false)
                        navController.navigate("login") {
                            popUpTo("profile") { inclusive = true }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Log Out", color = MaterialTheme.colorScheme.onError)
            }
        }
    }
}
