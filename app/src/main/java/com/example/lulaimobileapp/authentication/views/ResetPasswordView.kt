package com.example.lulaimobileapp.authentication.views

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun ResetPasswordView(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Reset Password Screen", modifier = Modifier.padding(bottom = 16.dp))

        Button(
            onClick = {
                // Handle reset password logic
                navController.navigate("login") // Navigate back to login after password reset
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Reset Password")
        }
    }
}