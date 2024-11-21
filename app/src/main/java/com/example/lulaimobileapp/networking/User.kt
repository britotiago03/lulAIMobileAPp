package com.example.lulaimobileapp.networking

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id") val id: String,
    @SerializedName("username") val userName: String,
    @SerializedName("email") val email: String
)
