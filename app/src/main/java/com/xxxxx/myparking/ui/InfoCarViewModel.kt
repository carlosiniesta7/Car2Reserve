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

class InfoCarViewModel (application: Application, carsService: CarsService): AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext
    private val remoteRepository = RemoteRepository(carsService)
    private val localRepository =
        LocalRepository(context.getSharedPreferences("locationPreferences", Context.MODE_PRIVATE))

    private var location: Location = Location("")
    val stateLiveEvent = LiveEvent<InfoCarFragmentState>()
    private lateinit var locationManager: LocationManager


    fun bookCar(carId: Int) {
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                remoteRepository.bookCar(carId)
            }
            if (!response) {
                stateLiveEvent.postValue(InfoCarFragmentState.ErrorState("Ha ocurrido un error"))
            } else {
                stateLiveEvent.postValue(InfoCarFragmentState.OkState("Reserva realizada correctamente"))
            }
        }
    }

    fun saveBookCar(carId: Int) {
        localRepository.saveCar(carId.toString())
    }

    sealed class InfoCarFragmentState {
        data class OkState (
            val ok: String
        ): InfoCarFragmentState()

        data class ErrorState (
            val error: String
        ): InfoCarFragmentState()
    }

    class StartViewModelFactory(
        private val application: Application,
        private val carsService: CarsService
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(Application::class.java, CarsService::class.java)
                .newInstance(application, carsService)
        }
    }
}