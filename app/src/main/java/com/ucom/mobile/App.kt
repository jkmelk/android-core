package com.ucom.mobile

import com.core.CoreConfig
import com.core.HelixApp
import com.data.di.dataModule
import com.ucom.mobile.di.viewModelModule
import com.ucom.mobile.utils.BASE_URL_DEV
import com.ucom.mobile.utils.BASE_URL_LIVE
import com.ucom.mobile.utils.BASE_URL_QA
import com.core.utils.deviceToken
import org.koin.core.context.loadKoinModules

class App : HelixApp() {

    override fun onCreate() {
        super.onCreate()
        loadKoinModules(dataModule + viewModelModule)
    }

    override fun getConfig() = CoreConfig.Builder()
            .baseUrlDev(BASE_URL_DEV)
            .baseUrlQa(BASE_URL_QA)
            .baseUrlLive(BASE_URL_LIVE)
            .mainContainer(R.id.main_container)
            .versionName(BuildConfig.VERSION_NAME)
            .versionCode(BuildConfig.VERSION_CODE)
            .appId(deviceToken())
            .build()
}