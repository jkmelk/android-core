package com.base.applicaton

import com.base.applicaton.di.viewModelModule
import com.base.applicaton.utils.initCoreConfig
import com.core.HelixApp
import com.data.di.dataModule
import org.koin.core.context.loadKoinModules

class App : HelixApp() {

    override fun onCreate() {
        super.onCreate()
        initCoreConfig()
        loadKoinModules(dataModule + viewModelModule)
    }
}