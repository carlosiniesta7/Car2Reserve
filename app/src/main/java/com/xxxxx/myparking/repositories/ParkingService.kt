package com.xxxxx.myparking.repositories

import com.xxxxx.myparking.models.BaseResponse
import com.xxxxx.myparking.models.ParkingResponse
import com.xxxxx.myparking.models.SaveParkingRequest
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ParkingService {

    @GET("getparkingdata")
    suspend fun getParkingSpot(@Query("userId") userId: String): Response<ParkingResponse>

    @POST("saveparkingspot")
    suspend fun saveParkingSpot(@Body saveParkingRequest: SaveParkingRequest): Response<BaseResponse>

    @DELETE("removeparkingspot")
    suspend fun deleteParkingSpot(@Query("userId") userId: String): Response<BaseResponse>
}