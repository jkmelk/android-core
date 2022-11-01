package com.sample.mobile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.core.navigation.presentFragment
import com.core.utils.initStatusBar
import com.sample.mobile.screen.sample.SampleFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        initStatusBar()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presentFragment<SampleFragment>(backStack = false, animate = false)
    }
}