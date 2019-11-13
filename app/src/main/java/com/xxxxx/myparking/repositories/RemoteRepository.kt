package com.xxxxx.myparking.repositories

import android.location.Location
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.xxxxx.myparking.models.BookCarBody
import com.xxxxx.myparking.models.Cars
import com.xxxxx.myparking.models.ReturnCarBody
import com.xxxxx.myparking.models.ReturnCarResponse

class RemoteRepository(private val carsService: CarsService) {

    companion object {
        private const val userId = "3"
    }

    suspend fun getAvailableCars(): List<Cars>? {
        return try {
            val response = carsService.getCars()

            if (response.isSuccessful && (response.body()?.success ?: false)) {
                response.body()?.cars!!

            } else {
                null
            }

        } catch (e: Exception) {
            null
        }
    }

    //TODO: Tras reservar deberemos añadirlo a SharedPreferences
    suspend fun bookCar(carId: Int): Boolean {
        return try {
        val response = carsService.bookCars(BookCarBody(userId, carId))
        response.isSuccessful && (response.body()?.success ?: false)
        } catch (e: Exception) {
            false
        }
    }

    //TODO: Tras devolver deberemos eliminarlo del SharedPreferences
    suspend fun returnCar(carId: Int, latitude: Double, longitude: Double): Boolean {
        return try {
            val response = carsService.returnCar(ReturnCarBody(userId, carId, latitude, longitude))
            response.isSuccessful && (response.body()?.success ?: false)
        } catch (e: Exception) {
            false
        }
    }

}