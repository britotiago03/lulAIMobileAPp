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
fun SetPasswordView(email: String, otp: String, navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Set Password for $email", modifier = Modifier.padding(bottom = 16.dp))
        Text(text = "OTP: $otp", modifier = Modifier.padding(bottom = 16.dp))

        Button(
            onClick = {
                // Handle setting the password logic
                navController.navigate("login") // Navigate back to login after setting password
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Set Password")
        }
    }
}