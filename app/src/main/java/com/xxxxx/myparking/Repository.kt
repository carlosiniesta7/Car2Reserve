package com.xxxxx.myparking

import android.content.Context
import android.location.Location
import android.util.Log
import com.google.android.gms.maps.model.LatLng

class Repository(private val context: Context) {

    fun saveLocation(location: Location?) {
        location?.let {
            Log.d("posicion a guardar", "" + location.latitude + " " + location.longitude)

            val prefs = context.getSharedPreferences("locationPreferences", Context.MODE_PRIVATE)
            prefs.edit().apply {
                putString("latitude", location.latitude.toString())
                putString("longitude", location.longitude.toString())
            }.apply()
        }
    }

    fun saveLocation(latLng: LatLng?) {
        Log.d("posicion a guardar", "" + latLng?.latitude + " " + latLng?.longitude)
        latLng?.let {
            val prefs =
                context.getSharedPreferences("locationPreferences", Context.MODE_PRIVATE)
            prefs.edit().apply {
                putString("latitude", it.latitude.toString())
                putString("longitude", it.longitude.toString())
            }.apply()
        }

    }

    fun getSavedLocation(): Location? {
        val prefs = context.getSharedPreferences("locationPreferences", Context.MODE_PRIVATE)

        val location = Location("")
        val latitude = prefs.getString("latitude", "INVALID")
        val longitude = prefs.getString("longitude", "INVALID")

        if (latitude == "INVALID" || longitude == "INVALID") {
            return null
        } else {
            location.latitude = latitude?.toDouble() ?: 0.0
            location.longitude = longitude?.toDouble() ?: 0.0
        }

        return location
    }

    fun removeLocation() {
        val prefs = context.getSharedPreferences("locationPreferences", Context.MODE_PRIVATE)

        prefs.edit().apply {
            remove("latitude")
            remove("longitude")
        }.apply()
    }
}