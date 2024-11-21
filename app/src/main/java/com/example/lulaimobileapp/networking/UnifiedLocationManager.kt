package com.example.lulaimobileapp.networking

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*

class UnifiedLocationManager(private val context: Context) {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    val currentLocation = mutableStateOf<android.location.Location?>(null)

    @SuppressLint("MissingPermission")
    fun requestLocation() {
        // Check for permission
        val permission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        if (permission != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            // Request permission in your Activity/Fragment
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            currentLocation.value = location
        }
    }
}