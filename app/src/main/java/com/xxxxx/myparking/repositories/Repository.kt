package com.xxxxx.myparking.repositories

import android.content.Context
import android.content.SharedPreferences
import android.location.Location
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.xxxxx.myparking.models.SaveParkingRequest
import kotlinx.coroutines.delay

class Repository(private val parkingService: ParkingService, private val sharedPrefs: SharedPreferences) {

    companion object {
        private const val USER = "4321"
    }
    suspend fun saveLocation(location: Location?) {
        location?.let {
            val latLng = LatLng(it.latitude, it.longitude)
            saveLocation(latLng)
        }
    }

    suspend fun saveLocation(latLng: LatLng?): Boolean {
        Log.d("posicion a guardar", "" + latLng?.latitude + " " + latLng?.longitude)
        latLng?.let {
            return try {
                val response = parkingService.saveParkingSpot(
                    SaveParkingRequest(
                        USER,
                        it.latitude,
                        it.longitude
                    )
                )

                response.isSuccessful && (response.body()?.success ?: false)
            }catch (e: Exception) {
                false
            }


            /*sharedPrefs.edit().apply {
                putString("latitude", it.latitude.toString())
                putString("longitude", it.longitude.toString())
            }.apply()*/
        } ?: return false

    }

    suspend fun getSavedLocation(): Location? {

        return try {
            val response = parkingService.getParkingSpot(USER)

            if (response.isSuccessful && (response.body()?.success ?: false)) {
                Location("").apply {
                    latitude = response.body()?.latitude ?: 0.0
                    longitude = response.body()?.longitude ?: 0.0
                }
            } else {
                null
            }

        } catch (e: Exception) {
            null
        }

        /*val location = Location("")
        val latitude = sharedPrefs.getString("latitude", "INVALID")
        val longitude = sharedPrefs.getString("longitude", "INVALID")

        if (latitude == "INVALID" || longitude == "INVALID") {
            return null
        } else {
            location.latitude = latitude?.toDouble() ?: 0.0
            location.longitude = longitude?.toDouble() ?: 0.0
        }

        return location*/
    }

    suspend fun removeLocation(): Boolean {

        return try {
            val response = parkingService.deleteParkingSpot(USER)
            response.isSuccessful && (response.body()?.success ?: false)
        } catch (e: Exception) {
            false
        }

        /*sharedPrefs.edit().apply {
            remove("latitude")
            remove("longitude")
        }.apply()*/
    }
}