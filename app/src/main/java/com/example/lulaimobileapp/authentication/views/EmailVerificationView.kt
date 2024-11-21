package com.example.lulaimobileapp.authentication.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lulaimobileapp.networking.APIService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun EmailVerificationView(email: String, navController: NavController) {
    val otpDigits = remember { mutableStateListOf("", "", "", "", "", "") }
    val focusRequesters = List(6) { FocusRequester() }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Email Verification",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Enter your OTP code",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // OTP Input Fields
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                for (index in 0..5) {
                    OutlinedTextField(
                        value = otpDigits[index],
                        onValueChange = { newValue ->
                            if (newValue.length <= 1) {
                                otpDigits[index] = newValue
                                if (newValue.isNotEmpty() && index < 5) {
                                    focusRequesters[index + 1].requestFocus()
                                }
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .width(40.dp)
                            .focusRequester(focusRequesters[index])
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Resend OTP Link
            Row {
                Text("Didn't receive code?")
                Spacer(modifier = Modifier.width(4.dp))
                TextButton(onClick = {
                    resendOTP(email, coroutineScope, snackbarHostState)
                }) {
                    Text("Resend again")
                }
            }
            Spacer(modifier = Modifier.height(32.dp))

            // Verify Button
            Button(
                onClick = {
                    verifyOTPAndNavigate(
                        email,
                        otpDigits.joinToString(""),
                        navController,
                        coroutineScope,
                        snackbarHostState
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Verify")
            }
        }
    }
}

fun verifyOTPAndNavigate(
    email: String,
    otp: String,
    navController: NavController,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState
) {
    if (otp.length != 6) {
        coroutineScope.launch {
            snackbarHostState.showSnackbar("OTP must be 6 digits.")
        }
        return
    }

    coroutineScope.launch {
        val result = APIService.verifyOTP(email, otp)
        val (success, message) = result

        if (success) {
            navController.navigate("setPassword/$email/$otp")
        } else {
            snackbarHostState.showSnackbar(message ?: "Invalid or expired OTP.")
        }
    }
}

fun resendOTP(
    email: String,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState
) {
    coroutineScope.launch {
        val result = APIService.forgotPassword(email)
        val (_, message) = result // 'success' not used, hence replaced with `_`

        snackbarHostState.showSnackbar(message ?: "Failed to resend OTP.")
    }
}
