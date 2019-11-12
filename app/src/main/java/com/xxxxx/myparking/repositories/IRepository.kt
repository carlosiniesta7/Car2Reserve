package com.xxxxx.myparking.repositories

import android.location.Location
import com.google.android.gms.maps.model.LatLng

interface IRepository {
    suspend fun saveLocation(location: Location?): Boolean

    suspend fun saveLocation(latLng: LatLng?): Boolean

    suspend fun getSavedLocation(): Location?

    suspend fun removeLocation(): Boolean
}