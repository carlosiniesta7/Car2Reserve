package com.xxxxx.myparking.repositories

import com.xxxxx.myparking.models.*
import retrofit2.Response
import retrofit2.http.*

interface CarsService {

    @GET("getavailablecars")
    suspend fun getCars(): Response<AvailableCarsResponse>

    @POST("pickcar")
    suspend fun bookCars(@Body bookCarBody: BookCarBody): Response<BaseResponse>

    @POST("returncar")
    suspend fun returnCar(@Body returnCarBody: ReturnCarBody): Response<ReturnCarResponse>
}