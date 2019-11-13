package com.xxxxx.myparking.repositories

import android.content.SharedPreferences
import android.location.Location
import com.google.android.gms.maps.model.LatLng

class LocalRepository(private val sharedPrefs: SharedPreferences) {

    //TODO: Meter un json para varios coches
     fun saveCar(carId: String?) {
        sharedPrefs.edit().apply {
            putString("coche", carId)
        }.commit()
    }

    fun getBookedCar(): String? {
        val carId = sharedPrefs.getString("coche", "INVALID")
        if (carId == "INVALID") {
            return null
        }
        return carId
    }

    fun deleteBookedCar() {
        sharedPrefs.edit().apply() {
            remove("coche")
        }.commit()
    }

}