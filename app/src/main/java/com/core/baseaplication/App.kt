package com.core.baseaplication

import com.core.HelixApp
import com.core.baseaplication.utils.initCoreConfig
import com.core.logger.log

class App : HelixApp() {

    override fun onCreate() {
        super.onCreate()
        initCoreConfig()
    log { "added logger functionality" }}
}