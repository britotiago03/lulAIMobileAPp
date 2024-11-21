package com.example.lulaimobileapp.navigation

import androidx.compose.runtime.Composable
import com.example.lulaimobileapp.utils.DataStoreManager

@Composable
fun MainScreen(dataStoreManager: DataStoreManager) {
    // TabBarView manages its own navigation
    TabBarView(dataStoreManager = dataStoreManager)
}
