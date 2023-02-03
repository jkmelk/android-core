package com.base.applicaton

import com.core.CoreConfig
import com.base.applicaton.di.viewModelModule
import com.base.applicaton.utils.fragment.BASE_URL_DEV
import com.base.applicaton.utils.fragment.BASE_URL_LIVE
import com.base.applicaton.utils.fragment.BASE_URL_QA
import com.core.HelixApp
import com.core.prefrences.PreferenceKey
import com.data.di.dataModule
import com.yt.utils.extensions.deviceToken
import com.yt.utils.extensions.getDeviceDensity
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
            .mainContainer(R.id.main_container)
            .versionName(BuildConfig.VERSION_NAME)
            .versionCode(BuildConfig.VERSION_CODE)
            .scale(this.getDeviceDensity())
            .appId(deviceToken())
            .build()
}