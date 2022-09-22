package com.core.baseaplication

import com.core.HelixApp
import com.core.baseaplication.utils.initCoreConfig

class App : HelixApp() {

    override fun onCreate() {
        super.onCreate()
        initCoreConfig()
    }
}