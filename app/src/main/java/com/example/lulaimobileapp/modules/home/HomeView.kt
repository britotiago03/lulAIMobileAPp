package com.example.lulaimobileapp.modules.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lulaimobileapp.utils.DataStoreManager
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@Composable
fun HomeView(dataStoreManager: DataStoreManager) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var userName by remember { mutableStateOf<String?>(null) }
    var showErrorAlert by remember { mutableStateOf(false) }
    var alertMessage by remember { mutableStateOf("") }

    // Fetch username from dataStoreManager
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val username = dataStoreManager.getUsername().firstOrNull()
            if (username.isNullOrEmpty()) {
                alertMessage = "No user found. Please log in again."
                showErrorAlert = true
            } else {
                userName = username
            }
        }
    }

    // Scaffold with TabBarView as the bottom navigation
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            // TabBarView will handle the bottom navigation
            // No need to include it here as it's already wrapped in AppNavGraph
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Greeting Message
            Text(
                text = "Hello, ${userName ?: "User"}!",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Sample content in HomeView
            Text(
                text = "What are you looking for today?",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // You can add more UI components (e.g., search bar, featured items, etc.)
            // Example: Featured Product Section (can be customized)
            Text(
                text = "Featured Products",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            // Add additional content or UI components below as needed
        }

        // Error Alert Dialog (if there's an issue with the username retrieval)
        if (showErrorAlert) {
            AlertDialog(
                onDismissRequest = { showErrorAlert = false },
                title = { Text("Error") },
                text = { Text(alertMessage) },
                confirmButton = {
                    TextButton(onClick = { showErrorAlert = false }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}
