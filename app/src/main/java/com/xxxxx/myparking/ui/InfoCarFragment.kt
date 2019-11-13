package com.xxxxx.myparking.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.xxxxx.myparking.MainActivity
import com.xxxxx.myparking.R
import com.xxxxx.myparking.repositories.LocalRepository
import kotlinx.android.synthetic.main.info_car_fragment.*

class InfoCarFragment: BottomSheetDialogFragment() {

    private val args: InfoCarFragmentArgs by navArgs()
    private lateinit var viewModel: InfoCarViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.info_car_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()

        car_text.text = args.carId
        book_button.setOnClickListener {
            viewModel.bookCar(args.carId.toInt())
        }
    }

    fun setupViewModel() {
        val factory = StartViewModelFactory(
            activity!!.application,
            (activity as MainActivity).getServiceInstance()
        )
        viewModel = ViewModelProvider(this, factory).get(InfoCarViewModel::class.java)

        viewModel.stateLiveEvent.observe(
            viewLifecycleOwner, Observer {
                when(it) {
                    is InfoCarViewModel.InfoCarFragmentState.OkState -> {
                        Toast.makeText(context, it.ok, Toast.LENGTH_LONG).show()
                        viewModel.saveBookCar(args.carId.toInt())
                        findNavController().navigate(InfoCarFragmentDirections.actionInfoCarFragmentToStartFragment())
                        //super.dismiss()
                    }
                    is InfoCarViewModel.InfoCarFragmentState.ErrorState -> {
                        Toast.makeText(context, it.error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        )

    }

}