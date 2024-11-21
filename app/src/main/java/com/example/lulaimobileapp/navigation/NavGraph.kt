package com.example.lulaimobileapp.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.lulaimobileapp.authentication.views.LoginView
import com.example.lulaimobileapp.authentication.views.SignUpView
import com.example.lulaimobileapp.authentication.views.ResetPasswordView
import com.example.lulaimobileapp.authentication.views.EmailVerificationView
import com.example.lulaimobileapp.authentication.views.SetPasswordView
import com.example.lulaimobileapp.utils.DataStoreManager

@Composable
fun AppNavGraph(
    dataStoreManager: DataStoreManager,
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = "welcome", // Start at WelcomeView
        modifier = Modifier.padding(paddingValues)
    ) {
        // Authentication Flow
        composable("welcome") {
            WelcomeView(navController)
        }
        composable("login") {
            LoginView(navController, dataStoreManager)
        }
        composable("signUp") {
            SignUpView(navController, dataStoreManager)
        }
        composable("resetPassword") {
            ResetPasswordView(navController)
        }
        composable("emailVerification/{email}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            EmailVerificationView(email, navController)
        }
        composable("setPassword/{email}/{otp}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            val otp = backStackEntry.arguments?.getString("otp") ?: ""
            SetPasswordView(email, otp, navController)
        }

        // Main Content (TabBar wraps all these views)
        composable("main") {
            MainScreen(dataStoreManager)
        }
    }
}
