package com.core.baseaplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.core.navigation.showFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showFragment<BlankFragment>(container = R.id.main_container)
    }
}