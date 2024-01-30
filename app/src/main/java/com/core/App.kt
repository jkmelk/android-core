package com.core

import com.base.applicaton.BuildConfig
import com.base.applicaton.di.viewModelModule
import com.base.applicaton.utils.BASE_URL_DEV
import com.base.applicaton.utils.BASE_URL_LIVE
import com.base.applicaton.utils.BASE_URL_QA
import com.core.prefrences.PreferenceKey
import com.core.utils.deviceToken
import com.core.utils.getDeviceDensity
import com.data.di.dataModule
import org.koin.core.context.loadKoinModules

class App : HelixApp() {

    override fun onCreate() {
        super.onCreate()
        loadKoinModules(dataModule + viewModelModule)
        preferences.putString(PreferenceKey.TOKEN, "")
    }

    override fun getConfig() = CoreConfig.Builder()
            .baseUrlDev(BASE_URL_DEV)
            .baseUrlQa(BASE_URL_QA)
            .baseUrlLive(BASE_URL_LIVE)
            .flavor(BuildConfig.FLAVOR)
            .mainContainer(android.R.id.content)
            .versionName(BuildConfig.VERSION_NAME)
            .versionCode(BuildConfig.VERSION_CODE)
            .scale(this.getDeviceDensity())
            .appId(deviceToken())
            .build()
}