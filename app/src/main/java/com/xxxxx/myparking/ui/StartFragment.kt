package com.xxxxx.myparking.ui

import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.xxxxx.myparking.R
import kotlinx.android.synthetic.main.start_fragment.*

class StartFragment : Fragment(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var marker: Marker
    private lateinit var viewModel: StartViewModel
    lateinit var mapFragment: SupportMapFragment
    var savedLocation: Location? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.start_fragment, container, false)
    }

    fun showCurrentPosition(location: Location) {
        Log.d("Position", location.latitude.toString() + "  " + location.longitude.toString())
    }

    fun setupViewModel() {
        viewModel = ViewModelProvider(this).get(StartViewModel::class.java)
        viewModel.startCurrentPositionListener(requireActivity())
        viewModel.locationLiveEvent.observe(
            viewLifecycleOwner, androidx.lifecycle.Observer {
                val cameraUpdate = CameraUpdateFactory.newCameraPosition(
                    CameraPosition(
                        LatLng(
                            it.latitude,
                            it.longitude
                        ), 15.0f, 0f, 0f
                    )
                )
                googleMap.animateCamera(cameraUpdate)
                showCurrentPosition(it)
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()

        mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)

        initListeners()
    }

    private fun initListeners() {
        share_button.setOnClickListener {
            Toast.makeText(context, "COMPARTIR", Toast.LENGTH_LONG).show()
        }

        parkButton.setOnClickListener {
            if (savedLocation != null) {
                // DESAPARCAR MODE
                viewModel.removeLocation()
                savedLocation = null
                setUnparkMode()
            } else {
                // APARCAR MODE
                viewModel.saveLocation()
                setParkMode()
            }

        }
    }

    private fun setUnparkMode() {
        marker.remove()
        setupSaveMarkerOptions()

    }

    private fun setParkMode() {
        addMapMarker()
        setupRemoveMarkerOptions()
    }

    private fun setupRemoveMarkerOptions() {
        parkButton.text = getString(R.string.unpark_label)
        share_button.visibility = View.VISIBLE
    }

    private fun setupSaveMarkerOptions() {
        parkButton.text = getString(R.string.park_label)
        share_button.visibility = View.GONE
    }

    override fun onMapReady(map: GoogleMap?) {
        map?.apply {
            val retrievedLocation = viewModel.getSavedLocation()
            isMyLocationEnabled = true
            isBuildingsEnabled = true
            googleMap = this
            retrievedLocation?.apply {
                savedLocation = retrievedLocation
                addMapMarker()
                setupRemoveMarkerOptions()
            }
        }
    }

    fun addMapMarker() {
        savedLocation?.let {
            val latLng = LatLng(it.latitude, it.longitude)
            marker =
                googleMap.addMarker(MarkerOptions().position(latLng).title(getString(R.string.your_car)))
        } ?: kotlin.run {
            viewModel.locationLiveEvent.value?.let {
                val latLng = LatLng(it.latitude, it.longitude)
                marker =
                    googleMap.addMarker(MarkerOptions().position(latLng).title(getString(R.string.your_car)))
                savedLocation = it
            }
        }
        marker.isDraggable = true
        googleMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            override fun onMarkerDragEnd(p0: Marker?) {
                p0?.let {
                    viewModel.removeLocation()
                    viewModel.saveLocation(LatLng(p0.position.latitude, p0.position.longitude))
                    addMapMarker()
                    marker.remove()
                    marker = p0
                }
            }

            override fun onMarkerDragStart(p0: Marker?) {}
            override fun onMarkerDrag(p0: Marker?) {}
        })
    }

}