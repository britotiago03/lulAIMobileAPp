package com.example.lulaimobileapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.lulaimobileapp.navigation.AppNavGraph
import com.example.lulaimobileapp.utils.DataStoreManager

class MainActivity : ComponentActivity() {

    private lateinit var dataStoreManager: DataStoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataStoreManager = DataStoreManager(this)

        setContent {
            val isLoggedInState = dataStoreManager.isLoggedIn().collectAsState(initial = false)
            val isLoggedIn = isLoggedInState.value
            val navController = rememberNavController()

            AppNavGraph(
                dataStoreManager = dataStoreManager,
                navController = navController,
                paddingValues = PaddingValues(0.dp)
            )
        }
    }
}
