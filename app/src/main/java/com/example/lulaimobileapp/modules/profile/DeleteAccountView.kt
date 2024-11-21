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
fun DeleteAccountView(navController: NavController, dataStoreManager: DataStoreManager) {
    val coroutineScope = rememberCoroutineScope()
    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(remember { SnackbarHostState() }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Are you sure you want to delete your account?",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "This action cannot be undone and all your data will be permanently deleted.",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { showDeleteDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Delete Account", color = MaterialTheme.colorScheme.onError)
            }
        }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Delete Account") },
                text = { Text("Are you sure you want to delete your account? This action is irreversible.") },
                confirmButton = {
                    TextButton(onClick = {
                        showDeleteDialog = false
                        coroutineScope.launch {
                            val token = dataStoreManager.getToken().firstOrNull()
                            if (!token.isNullOrEmpty()) {
                                val success = APIService.deleteUser(token)
                                if (success) {
                                    dataStoreManager.setLoggedIn(false)
                                    navController.navigate("login") {
                                        popUpTo("deleteAccount") { inclusive = true }
                                    }
                                } else {
                                    // Show error snackbar or alert
                                }
                            }
                        }
                    }) {
                        Text("Delete", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}
