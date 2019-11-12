package com.xxxxx.myparking.ui

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.xxxxx.myparking.R
import kotlinx.android.synthetic.main.splash_fragment.*

class SplashFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.splash_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val animation = AnimationUtils.loadAnimation(context,R.anim.rotate_animation)
        volante.startAnimation(animation)

        Handler().postDelayed({
            findNavController().navigate(SplashFragmentDirections.splashToStart())
        }, 4000)
    }

}