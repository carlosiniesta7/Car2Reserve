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
import com.xxxxx.myparking.models.Cars
import com.xxxxx.myparking.models.ReturnCarBody
import com.xxxxx.myparking.repositories.LocalRepository
import com.xxxxx.myparking.repositories.CarsService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StartViewModel (application: Application, carsService: CarsService): AndroidViewModel(application){

    private val context = getApplication<Application>().applicationContext
    private val remoteRepository =  RemoteRepository(carsService)
    private val localRepository = LocalRepository(context.getSharedPreferences("locationPreferences", Context.MODE_PRIVATE))

    private var location: Location = Location("")
    val stateLiveEvent = LiveEvent<StartFragmentState>()
    private lateinit var locationManager: LocationManager


    fun getAvailableCars() {
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                remoteRepository.getAvailableCars()
            }
            if (response == null) {
                stateLiveEvent.postValue(StartFragmentState.ErrorState("Ha ocurrido un error"))
            } else {
                stateLiveEvent.postValue(StartFragmentState.ListUpdateState(response))
            }
        }
    }

    fun returnCar(carId: Int, location: Location) {
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                remoteRepository.returnCar(carId, location.latitude, location.longitude)
            }
            if (response) {
                stateLiveEvent.postValue(StartFragmentState.ErrorState("Reserva finalizada correctamente"))
            } else {
                stateLiveEvent.postValue(StartFragmentState.ErrorState("Ha ocurrido un error"))
            }
        }
    }
    fun deleteBooked(){
        localRepository.deleteBookedCar()
    }

    fun getBooked() : String?{
        return localRepository.getBookedCar()
    }

    fun startCurrentPositionListener(activity: Activity){
        locationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager
        val client = LocationServices.getFusedLocationProviderClient(activity)
        client.lastLocation.addOnCompleteListener {
            val location = it.result

            location?.let {
                this.location = it
                stateLiveEvent.value = StartFragmentState.LocationState(it)
            }
        }
    }

}


sealed class StartFragmentState () {
    data class ListUpdateState (
        val carsList: List<Cars>
    ): StartFragmentState()

    data class LocationState (
        val location: Location
    ): StartFragmentState()

    data class BookedCarState (
        val car: String
    ): StartFragmentState()

    data class ErrorState (
        val error: String
    ): StartFragmentState()
}

class StartViewModelFactory(private val application: Application,
                            private val carsService: CarsService): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(Application::class.java, CarsService::class.java)
            .newInstance(application, carsService)
    }
}