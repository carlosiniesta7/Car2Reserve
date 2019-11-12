package com.xxxxx.myparking.repositories

import android.content.SharedPreferences
import android.location.Location
import com.google.android.gms.maps.model.LatLng

class LocalRepository(private val sharedPrefs: SharedPreferences): IRepository {

    override suspend fun saveLocation(location: Location?): Boolean {
        return location?.let {
            val latLng = LatLng(it.latitude, it.longitude)
            saveLocation(latLng)
        } ?: false
    }

    override suspend fun saveLocation(latLng: LatLng?): Boolean {

        return latLng?.let {
            sharedPrefs.edit().apply {
                putString("latitude", it.latitude.toString())
                putString("longitude", it.longitude.toString())
            }.commit()
        } ?: false
    }

    override suspend fun getSavedLocation(): Location? {
        val location = Location("")
        val latitude = sharedPrefs.getString("latitude", "INVALID")
        val longitude = sharedPrefs.getString("longitude", "INVALID")

        if (latitude == "INVALID" || longitude == "INVALID") {
            return null
        } else {
            location.latitude = latitude?.toDouble() ?: 0.0
            location.longitude = longitude?.toDouble() ?: 0.0
        }

        return location
    }

    override suspend fun removeLocation(): Boolean {
        return sharedPrefs.edit().apply {
            remove("latitude")
            remove("longitude")
        }.commit()
    }
}