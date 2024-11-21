package com.example.lulaimobileapp.modules.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ManageSubscriptionView(navController: NavController) {
    var currentPlan by remember { mutableStateOf("Premium") }
    var showAlert by remember { mutableStateOf(false) }
    var alertMessage by remember { mutableStateOf("") }

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
                text = "Subscription Plan",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Current Plan: $currentPlan",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Renewal Date: Dec 31, 2024",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    // Logic to cancel subscription
                    currentPlan = "Free"
                    alertMessage = "Your subscription has been canceled."
                    showAlert = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancel Subscription")
            }
        }

        if (showAlert) {
            AlertDialog(
                onDismissRequest = { showAlert = false },
                title = { Text("Subscription Update") },
                text = { Text(alertMessage) },
                confirmButton = {
                    TextButton(onClick = { showAlert = false }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}
