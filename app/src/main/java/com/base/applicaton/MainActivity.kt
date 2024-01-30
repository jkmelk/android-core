package com.base.applicaton

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.core.utils.initStatusBar


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        initStatusBar()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}