package com.xxxxx.myparking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.xxxxx.myparking.repositories.CarsService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var service: CarsService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findNavController(R.id.container)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(findNavController(R.id.container), null)
    }

    fun getServiceInstance (): CarsService {

        if (!::service.isInitialized) {
            service = Retrofit.Builder()
                .baseUrl("https://us-central1-correos-desarrollo.cloudfunctions.net/v1/car2reserve/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CarsService::class.java)
        }
        return service
    }

}
