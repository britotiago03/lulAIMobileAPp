package com.example.lulaimobileapp.authentication.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lulaimobileapp.R
import com.example.lulaimobileapp.networking.APIService
import com.example.lulaimobileapp.utils.DataStoreManager
import kotlinx.coroutines.launch

@Composable
fun LoginView(navController: NavController, dataStoreManager: DataStoreManager) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.lulai_logo_black),
                contentDescription = "LuAI Logo",
                modifier = Modifier.size(150.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Title
            Text(
                text = "Sign In",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Email TextField
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Enter your Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Password TextField
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Enter Your Password") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            painter = painterResource(id = if (isPasswordVisible) R.drawable.visibility else R.drawable.visibility_off),
                            contentDescription = if (isPasswordVisible) "Hide Password" else "Show Password"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Forgot Password Link
            TextButton(onClick = { navController.navigate("resetPassword") }) {
                Text("Forgot Password?")
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Sign In Button
            Button(
                onClick = {
                    coroutineScope.launch {
                        authenticateUser(
                            email = email,
                            password = password,
                            navController = navController,
                            dataStoreManager = dataStoreManager,
                            snackbarHostState = snackbarHostState
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Sign In")
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Sign Up Link
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Don't have an account?")
                TextButton(onClick = { navController.navigate("signUp") }) {
                    Text("Sign up")
                }
            }
        }
    }
}

private suspend fun authenticateUser(
    email: String,
    password: String,
    navController: NavController,
    dataStoreManager: DataStoreManager,
    snackbarHostState: SnackbarHostState
) {
    if (email.isEmpty() || password.isEmpty()) {
        snackbarHostState.showSnackbar("Email or Password cannot be empty")
        return
    }

    val (success, token) = APIService.login(email, password)
    if (success && token != null) {
        dataStoreManager.setLoggedIn(true)
        dataStoreManager.saveToken(token)
        navController.navigate("home") {
            popUpTo("login") { inclusive = true }
        }
    } else {
        snackbarHostState.showSnackbar("Login failed, Email or Password incorrect. Try again!")
    }
}
