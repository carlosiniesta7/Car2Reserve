package com.xxxxx.myparking.ui

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.location.Location
import android.location.LocationManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.xxxxx.myparking.BuildConfig
import com.xxxxx.myparking.repositories.RemoteRepository
import com.xxxxx.myparking.base.LiveEvent
import com.xxxxx.myparking.repositories.LocalRepository
import com.xxxxx.myparking.repositories.ParkingService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StartViewModel (application: Application, parkingService: ParkingService): AndroidViewModel(application){

    private val context = getApplication<Application>().applicationContext
    private val repository = if (BuildConfig.FLAVOR == "pro")
        RemoteRepository(parkingService)
    else
        LocalRepository(context.getSharedPreferences("locationPreferences", Context.MODE_PRIVATE))

    private var location: Location = Location("")
    val stateLiveEvent = LiveEvent<StartFragmentState>()
    private lateinit var locationManager: LocationManager

    fun saveLocation(): LatLng {
        viewModelScope.launch {
            val success = withContext(Dispatchers.IO) {
                repository.saveLocation(location)

            }
            if (!success) stateLiveEvent.postValue(StartFragmentState.ErrorState("Ha ocurrido un error"))
        }

        return LatLng(location.latitude, location.longitude)
    }

    fun saveLocation(latLng: LatLng) {
        viewModelScope.launch {
            val success = withContext(Dispatchers.IO) {
                repository.saveLocation(latLng)

            }
            if (!success) stateLiveEvent.postValue(StartFragmentState.ErrorState("Ha ocurrido un error"))
        }
    }
    fun getSavedLocation() {

        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                repository.getSavedLocation()
            }
            if (response == null || (response.latitude == 0.0 && response.longitude == 0.0)) {
                stateLiveEvent.postValue(StartFragmentState.ErrorState("Ha ocurrido un error"))
            } else {
                stateLiveEvent.postValue(StartFragmentState.PositionState(response))
            }
        }
    }

    fun removeLocation() {
        viewModelScope.launch {
            val success = withContext(Dispatchers.IO) {
                repository.removeLocation()
            }
            if (!success) stateLiveEvent.postValue(StartFragmentState.ErrorState("Ha ocurrido un error"))
        }
    }

    fun startCurrentPositionListener(activity: Activity){
        locationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager
        val client = LocationServices.getFusedLocationProviderClient(activity)
        client.lastLocation.addOnCompleteListener {
            val location = it.result

            location?.let {
                this.location = it
                stateLiveEvent.value = StartFragmentState.LocationUpdateState(it)
            }

        }
    }
}

sealed class StartFragmentState () {
    data class LocationUpdateState (
        val location: Location
    ): StartFragmentState()

    data class PositionState (
        val location: Location
    ): StartFragmentState()

    data class ErrorState (
        val error: String
    ): StartFragmentState()
}

class StartViewModelFactory(private val application: Application,
                            private val parkingService: ParkingService): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(Application::class.java, ParkingService::class.java)
            .newInstance(application, parkingService)
    }
}