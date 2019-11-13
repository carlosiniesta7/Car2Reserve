package com.xxxxx.myparking.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.xxxxx.myparking.R
import kotlinx.android.synthetic.main.share_fragment.*

class InfoCarFragment: BottomSheetDialogFragment() {

    private val args: InfoCarFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.share_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sms_text.text = args.carId

        book_button.setOnClickListener {
            findNavController().navigate(InfoCarFragmentDirections.actionInfoCarFragmentToStartFragment())
            //TODO: llamar al servicio bookCar
        }
    }

}