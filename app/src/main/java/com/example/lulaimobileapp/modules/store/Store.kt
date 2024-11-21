package com.example.lulaimobileapp.modules.store

import com.google.android.gms.maps.model.LatLng

data class Store(
    val id: Int,
    val name: String,
    val coordinate: LatLng,
    val distance: String,
    val isOpen: Boolean
)