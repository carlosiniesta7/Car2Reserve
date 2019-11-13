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

    fun getBookedCars(){
        //TODO: llamada a SharedPrefereces
    }

    fun startCurrentPositionListener(activity: Activity){
        locationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager
        val client = LocationServices.getFusedLocationProviderClient(activity)
        client.lastLocation.addOnCompleteListener {
            val location = it.result

            location?.let {
                this.location = it
                //stateLiveEvent.value = StartFragmentState.ListUpdateState()
            }
        }
    }
}

sealed class StartFragmentState () {
    data class ListUpdateState (
        val carsList: List<Cars>
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