package com.xxxxx.myparking.ui

import android.app.Activity
import android.app.Application
import android.content.Context.LOCATION_SERVICE
import android.location.Location
import android.location.LocationManager
import androidx.lifecycle.AndroidViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.xxxxx.myparking.MainActivity
import com.xxxxx.myparking.Repository
import com.xxxxx.myparking.base.LiveEvent

class StartViewModel (application: Application): AndroidViewModel(application){

    private val context = getApplication<Application>().applicationContext
    private val repository = Repository(context)

    val locationLiveEvent = LiveEvent<Location>()
    private lateinit var locationManager: LocationManager

    fun saveLocation() = repository.saveLocation(locationLiveEvent.value)
    fun saveLocation(latLng: LatLng) = repository.saveLocation(latLng)
    fun getSavedLocation():Location? = repository.getSavedLocation()
    fun removeLocation() = repository.removeLocation()

    fun startCurrentPositionListener(activity: Activity){
        locationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager
        val client = LocationServices.getFusedLocationProviderClient(activity)
        client.lastLocation.addOnCompleteListener {
            val location = it.result

            location?.let {
                locationLiveEvent.value = it
            }

        }
    }

}