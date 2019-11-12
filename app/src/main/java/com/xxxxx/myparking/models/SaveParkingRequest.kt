package com.xxxxx.myparking.models

data class SaveParkingRequest (
    val userId: String,
    val latitude: Double,
    val longitude: Double
)