package com.fastshift.cashier

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fastshift.cashier.screen.test_fragment.BlankFragment
import com.core.navigation.presentFragment
import com.yt.utils.extensions.initStatusBar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        initStatusBar()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presentFragment<BlankFragment>()
    }
}