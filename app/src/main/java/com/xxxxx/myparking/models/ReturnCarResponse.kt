package com.xxxxx.myparking.models

data class ReturnCarResponse (
    val success: Boolean,
    val carType: Int,
    val usedTime: Int,
    val kms: Double
)