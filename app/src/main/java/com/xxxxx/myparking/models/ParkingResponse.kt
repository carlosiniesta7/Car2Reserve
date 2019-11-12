package com.xxxxx.myparking.models

data class ParkingResponse (
    val success: Boolean,
    val userId: String,
    val latitude: Double,
    val longitude: Double
)