package com.xxxxx.myparking.ui

import android.location.Location
import android.os.Bundle
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
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.xxxxx.myparking.BuildConfig
import com.xxxxx.myparking.MainActivity
import com.xxxxx.myparking.R
import kotlinx.android.synthetic.main.start_fragment.*

class StartFragment : Fragment(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var marker: Marker
    private lateinit var viewModel: StartViewModel
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


        mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)

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
                        val cameraUpdate = CameraUpdateFactory.newCameraPosition(
                            CameraPosition(
                                LatLng(0.0, 0.0
                                ), 15.0f, 0f, 0f
                            )
                        )
                        googleMap.animateCamera(cameraUpdate)
                        //showCurrentPosition()
                    }
                    /*is StartFragmentState.PositionState -> {
                        if (googleMap != null) {
                            Log.d("MYPARKING", "Recibida localizacion: ${it.location.latitude},${it.location.longitude}")
                            savedLocation = it.location
                            val latLng = LatLng(it.location.latitude, it.location.longitude)
                            addMapMarker(latLng)

                        }
                    }*/
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
        }
    }

    fun addMapMarker(location: LatLng) {

        marker =
            googleMap.addMarker(MarkerOptions().position(location).title(getString(R.string.your_car)))
        marker.isDraggable = false
        googleMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            override fun onMarkerDragEnd(p0: Marker?) {
                p0?.let {
                    //viewModel.saveLocation(LatLng(p0.position.latitude, p0.position.longitude))
                    addMapMarker(p0.position)
                    marker.remove()
                    marker = p0
                }
            }

            override fun onMarkerDragStart(p0: Marker?) {}
            override fun onMarkerDrag(p0: Marker?) {}
        })
    }

}