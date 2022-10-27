package com.ucom.mobile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ucom.utils.extensions.initStatusBar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        initStatusBar()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}