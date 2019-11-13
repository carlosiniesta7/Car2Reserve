package com.xxxxx.myparking.models

data class AvailableCarsResponse (
    val success: Boolean,
    val cars: List<Cars>
)