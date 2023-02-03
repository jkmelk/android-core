package com.core.api.interceptor

import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import com.core.HelixApp
import com.core.api.interceptor.base.PublicInterceptor
import com.core.prefrences.AppPreferences
import com.core.prefrences.PreferenceKey
import com.yt.utils.extensions.deviceName
import okhttp3.Request

class PublicHeaderInterceptor(private val preferences: AppPreferences) : PublicInterceptor(preferences) {

    override fun intercept(requestBuilder: Request.Builder): Request.Builder {

        val key = preferences.getString(PreferenceKey.REGISTRATION_KEY, "Bearer ")
        val token = preferences.getString(PreferenceKey.TOKEN)
        val mode = preferences.getInt(PreferenceKey.DISPLAY_MODE, AppCompatDelegate.MODE_NIGHT_NO)

        requestBuilder.addHeader("Content-Type", "application/json")

        if (key.isEmpty() || token.isEmpty()) {
            return requestBuilder
        }
        val config = HelixApp.context.getConfig()

        val userAgent = "Core/" + config.versionName +
                " (" + config.appId + "; " + "build: " +
                config.versionCode + "; " + "Android " + deviceName() +
                "; OS:" + Build.VERSION.SDK_INT + ") " + "okhttp/" + okhttp3.OkHttp.VERSION
        requestBuilder.addHeader("User-Agent", userAgent)
        requestBuilder.addHeader("Authorization", key + token)
        return requestBuilder
    }
}