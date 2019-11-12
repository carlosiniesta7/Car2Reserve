package com.xxxxx.myparking.repositories

import android.location.Location
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.xxxxx.myparking.models.SaveParkingRequest

class RemoteRepository(private val parkingService: ParkingService) : IRepository {

    companion object {
        private const val USER = "4321"
    }
    override suspend fun saveLocation(location: Location?): Boolean {
        return location?.let {
            val latLng = LatLng(it.latitude, it.longitude)
            saveLocation(latLng)
        } ?: false
    }

    override suspend fun saveLocation(latLng: LatLng?): Boolean {
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
        } ?: return false

    }

    override suspend fun getSavedLocation(): Location? {

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
    }

    override suspend fun removeLocation(): Boolean {

        return try {
            val response = parkingService.deleteParkingSpot(USER)
            response.isSuccessful && (response.body()?.success ?: false)
        } catch (e: Exception) {
            false
        }
    }
}