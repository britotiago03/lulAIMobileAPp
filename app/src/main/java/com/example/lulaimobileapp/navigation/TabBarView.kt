package com.example.lulaimobileapp.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier // Make sure Modifier is imported
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.lulaimobileapp.R
import com.example.lulaimobileapp.modules.chat.ChatView
import com.example.lulaimobileapp.modules.home.HomeView
import com.example.lulaimobileapp.modules.profile.ProfileView
import com.example.lulaimobileapp.modules.shoppinglist.ShoppingListView
import com.example.lulaimobileapp.modules.store.StoreView
import com.example.lulaimobileapp.utils.DataStoreManager
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Person

@Composable
fun TabBarView(dataStoreManager: DataStoreManager) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { NavigationBar(navController = navController) }
    ) { paddingValues -> // paddingValues is passed here
        NavHost(
            navController = navController,
            startDestination = "home", // Default to HomeView
            modifier = Modifier.padding(paddingValues) // Apply padding to NavHost
        ) {
            composable("home") { HomeView(dataStoreManager) }
            composable("store") { StoreView() }
            composable("chat") { ChatView() }
            composable("shoppingList") { ShoppingListView() }
            composable("profile") { ProfileView(navController, dataStoreManager) }
        }
    }
}

@Composable
fun NavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem("Home", Icons.Filled.Home, null, "home"),
        BottomNavItem("Store", null, R.drawable.store, "store"),
        BottomNavItem("Chat", null, R.drawable.chat, "chat"),
        BottomNavItem("List", Icons.Filled.ShoppingCart, null, "shoppingList"),
        BottomNavItem("Profile", Icons.Filled.Person, null, "profile")
    )
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    item.icon?.let { icon ->
                        Icon(imageVector = icon, contentDescription = item.title)
                    } ?: item.drawableRes?.let { drawableRes ->
                        Icon(
                            painter = painterResource(id = drawableRes),
                            contentDescription = item.title
                        )
                    }
                },
                label = { Text(item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                    }
                },
                alwaysShowLabel = true
            )
        }
    }
}

data class BottomNavItem(
    val title: String,
    val icon: ImageVector? = null,
    val drawableRes: Int? = null,
    val route: String
)
