package com.example.lulaimobileapp.modules.store

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreView() {
    // Original list of stores
    val allStores = listOf(
        Store(1, "FreshMart Grocery", LatLng(60.7950, 10.6930), "0.2 km", true),
        Store(2, "TechWorld Electronic", LatLng(60.7900, 10.6950), "0.5 km", false),
        Store(3, "The Coffee Corner", LatLng(60.7500, 10.6900), "0.3 km", true)
    )

    // State for the search query
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    // Filtered stores based on search query
    val filteredStores = allStores.filter {
        it.name.contains(searchQuery.text, ignoreCase = true)
    }

    // Camera position state
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(60.7950, 10.6930), 14f)
    }

    Column {
        // Search bar
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            placeholder = { Text("Search for stores...") },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        )

        // Google Map
        Box(modifier = Modifier.height(300.dp)) {
            GoogleMap(
                modifier = Modifier.matchParentSize(),
                cameraPositionState = cameraPositionState
            ) {
                filteredStores.forEach { store ->
                    Marker(
                        state = MarkerState(position = store.coordinate),
                        title = store.name,
                        snippet = if (store.isOpen) "Open" else "Closed"
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Nearby Stores list
        Text(
            text = "Nearby Stores",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )

        filteredStores.forEach { store ->
            StoreRowView(store)
            HorizontalDivider()
        }
    }
}

@Composable
fun StoreRowView(store: Store) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = store.name,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = store.distance,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Text(
            text = if (store.isOpen) "Open" else "Closed",
            color = if (store.isOpen) MaterialTheme.colorScheme.primary else Color.Red
        )
    }
}
