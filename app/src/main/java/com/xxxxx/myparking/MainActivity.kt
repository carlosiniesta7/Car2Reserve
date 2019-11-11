package com.xxxxx.myparking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findNavController(R.id.container)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(findNavController(R.id.container), null)
    }

}
