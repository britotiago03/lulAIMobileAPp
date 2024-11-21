package com.example.lulaimobileapp.modules.shoppinglist

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class ShoppingListViewModel : ViewModel() {
    val shoppingItems = mutableStateListOf(
        ShoppingItem(1, "Bread", 2),
        ShoppingItem(2, "Eggs", 1)
    )
}