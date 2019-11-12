package com.xxxxx.myparking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.xxxxx.myparking.repositories.ParkingService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var service: ParkingService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findNavController(R.id.container)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(findNavController(R.id.container), null)
    }

    fun getServiceInstance (): ParkingService {

        if (!::service.isInitialized) {
            service = Retrofit.Builder()
                .baseUrl("https://us-central1-correos-desarrollo.cloudfunctions.net/v1/myparking/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ParkingService::class.java)
        }
        return service
    }

}
