package com.core

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.core.application.R
import com.core.screen.test_fragment.BlankFragment
import com.core.navigation.presentFragment
import com.core.utils.initStatusBar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        initStatusBar()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presentFragment<BlankFragment>()
    }
}