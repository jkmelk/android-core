package com.core.baseaplication

import com.base.applicaton.utils.initCoreConfig
import com.core.HelixApp

class App : HelixApp() {

    override fun onCreate() {
        super.onCreate()
        initCoreConfig()
    }
}