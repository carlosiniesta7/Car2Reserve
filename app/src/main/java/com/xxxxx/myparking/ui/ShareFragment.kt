package com.xxxxx.myparking.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.xxxxx.myparking.R
import kotlinx.android.synthetic.main.share_fragment.*

class ShareFragment: BottomSheetDialogFragment() {

    private val args: ShareFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.share_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sms_text.setOnClickListener {
            findNavController().navigate(ShareFragmentDirections
                .actionShareFragmentToContactsFragment(args.shareText))
        }

        apps_text.setOnClickListener {
            share (args.shareText)
            findNavController().popBackStack()
        }
    }

    private fun share (text: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }

        context!!.startActivity(Intent.createChooser(intent, "Compartir"))

    }
}