package com.xxxxx.myparking.ui

import android.app.AlertDialog
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.transition.Transition
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import com.xxxxx.myparking.BuildConfig
import com.xxxxx.myparking.MainActivity
import com.xxxxx.myparking.R
import com.xxxxx.myparking.models.Cars
import kotlinx.android.synthetic.main.info_car_fragment.*
import kotlinx.android.synthetic.main.start_fragment.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.fixedRateTimer

class StartFragment : Fragment(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var marker: Marker
    private lateinit var viewModel: StartViewModel
    private  var currentLocation = Location("")
    lateinit var mapFragment: SupportMapFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.start_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()

        getCarsPeriod()

        checkBookedButton()

        mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    private fun getCarsPeriod() {
        Handler().postDelayed({
            viewModel.getAvailableCars()
            viewModel.startCurrentPositionListener(requireActivity())
            getCarsPeriod()
        }, 30000)
    }

    private fun checkBookedButton() {
        val carBooked = viewModel.getBooked()
        if (carBooked == null) {
            bookedButton.visibility = View.GONE
        } else {
            bookedButton.visibility = View.VISIBLE
            bookedButton.setOnClickListener {
                showDialogBookedCars(carBooked)
            }
        }
    }


    fun showCurrentPosition(location: Location) {
        Log.d("Position", location.latitude.toString() + "  " + location.longitude.toString())
    }

    fun setupViewModel() {
        val factory = StartViewModelFactory(
            activity!!.application,
            (activity as MainActivity).getServiceInstance()
        )
        viewModel = ViewModelProvider(this, factory).get(StartViewModel::class.java)
        viewModel.startCurrentPositionListener(requireActivity())

        viewModel.stateLiveEvent.observe(
            viewLifecycleOwner, Observer {
                when(it) {
                    is StartFragmentState.ListUpdateState -> {
                        val listCar = it.carsList
                        for(car in listCar){
                            addMapMarker(car)
                        }
                        Log.d("START FRAGMENT", "Numero de Coches Disponibles: ${listCar.size}")
                        googleMap.setOnMarkerClickListener {
                            findNavController().navigate(StartFragmentDirections.actionStartFragmentToInfoCarFragment(it.tag.toString()))
                            true
                        }
                    }
                    is StartFragmentState.LocationState -> {
                        currentLocation = it.location
                    }
                    is StartFragmentState.ErrorState -> {
                        Snackbar.make(requireView(), it.error, Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        )
    }


    override fun onMapReady(map: GoogleMap?) {
        map?.apply {
            viewModel.getAvailableCars()
            isMyLocationEnabled = true
            isBuildingsEnabled = true
            googleMap = this
            moveCamera()
        }
    }

    private fun moveCamera() {
        val cameraUpdate = CameraUpdateFactory.newCameraPosition(
            CameraPosition(
                LatLng(
                    40.415511, -3.7095896
                ), 15.0f, 0f, 0f
            )
        )
        googleMap.animateCamera(cameraUpdate)
    }

    fun addMapMarker(car: Cars) {

        val location = LatLng(car.latitude, car.longitude)

        marker = googleMap.addMarker(MarkerOptions().position(location).title(getString(R.string.your_car)))

        when(car.carType)  {
            1 ->  marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            2 -> marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
            else -> marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        }

        marker.isDraggable = false
        marker.tag = car.carId.toString()
    }


    fun showDialogBookedCars(carId: String) {
        val items = arrayOf(carId)
        val selectedList = ArrayList<Int>()
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Cancelar reserva")
        builder.setMessage("¿Quieres devolver el coche " + carId + "?")

        builder.setPositiveButton("Ok") { dialogInterface, i ->
            viewModel.returnCar(carId.toInt(), currentLocation)
            viewModel.deleteBooked()
            checkBookedButton()
        }

        val dialog = builder.create()
        viewModel.getAvailableCars()
        dialog.show()
    }

}