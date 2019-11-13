package com.xxxxx.myparking.models

data class ReturnCarResponse (
    val success: Boolean,
    val usedTime: Int,
    val kms: Int
)