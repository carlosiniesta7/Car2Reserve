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
import com.xxxxx.myparking.BuildConfig
import com.xxxxx.myparking.MainActivity
import com.xxxxx.myparking.R
import kotlinx.android.synthetic.main.button_group_component.*
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
        val factory = StartViewModelFactory(
            activity!!.application,
            (activity as MainActivity).getServiceInstance()
        )
        viewModel = ViewModelProvider(this, factory).get(StartViewModel::class.java)
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
        viewModel.positionLiveEvent.observe(
            viewLifecycleOwner, Observer { location ->
                if (location != null && googleMap != null) {
                    Log.d("MYPARKING", "Recibida localizacion: ${location.latitude},${location.longitude}")
                    savedLocation = location
                    addMapMarker()
                    setupRemoveMarkerOptions()
                }
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
        buttonGroup.rightButtonSetOnClickListener(View.OnClickListener {
//            if (BuildConfig.FLAVOR == "dev"){
//                Toast.makeText(context, "DEV COMPARTIR", Toast.LENGTH_LONG).show()
//            }else {
//                Toast.makeText(context, "COMPARTIR", Toast.LENGTH_LONG).show()
//            }
//            Toast.makeText(context, Constant.URL, Toast.LENGTH_LONG).show()
        })

        buttonGroup.leftButtonSetOnClickListener(View.OnClickListener{
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
        })
    }

    private fun setUnparkMode() {
        marker.remove()
        setupSaveMarkerOptions()

    }

    private fun setParkMode() {
        addMapMarker()
        setupRemoveMarkerOptions()
    }

    private fun attachButtonTo(position:Int){
        TransitionManager.beginDelayedTransition(buttonGroup)

        // DESAPARCAR Y COMPARTIR VISIBLES

        val constraintSet= ConstraintSet()
        constraintSet.clone(container)

        constraintSet.clear(R.id.buttonGroup,ConstraintSet.TOP)
        constraintSet.clear(R.id.buttonGroup,ConstraintSet.BOTTOM)

        constraintSet.connect(R.id.buttonGroup,position,ConstraintSet.PARENT_ID,position)
        constraintSet.applyTo(container)
    }

    private fun setupRemoveMarkerOptions() {
        //DESAPARCAR Y COMPARTIR VISIBLES
        attachButtonTo(ConstraintSet.BOTTOM)
        buttonGroup.leftButtonSetText(getString(R.string.unpark_label))
        buttonGroup.rightButtonSetVisibility(View.VISIBLE)
    }

    private fun setupSaveMarkerOptions() {
        // APARCAR
        attachButtonTo(ConstraintSet.TOP)
        buttonGroup.leftButtonSetText(getString(R.string.park_label))
        buttonGroup.rightButtonSetVisibility(View.GONE)
    }

    override fun onMapReady(map: GoogleMap?) {
        map?.apply {
            viewModel.getSavedLocation()
            isMyLocationEnabled = true
            isBuildingsEnabled = true
            googleMap = this
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