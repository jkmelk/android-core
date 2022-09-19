package com.core.baseaplication

import com.core.HelixApp
import com.core.prefrences.PreferenceKey
import com.core.utils.extensions.deviceToken

class App : HelixApp() {

    override fun onCreate() {
        super.onCreate()
        preferences.putString(PreferenceKey.VERSION_NAME, BuildConfig.VERSION_NAME)
        preferences.putInt(PreferenceKey.VERSION_CODE, BuildConfig.VERSION_CODE)
        preferences.putString(PreferenceKey.APPLICATION_ID, deviceToken())
    }
}