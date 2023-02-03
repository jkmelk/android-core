package com.core.api.interceptor.base

import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import com.core.HelixApp
import com.core.api.ApiConstants
import com.core.manager.getLanguage
import com.core.prefrences.AppPreferences
import com.core.prefrences.PreferenceKey
import com.core.utils.LIGHT_MODE
import com.core.utils.NIGHT_MODE
import com.yt.utils.extensions.deviceName
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

abstract class PublicInterceptor(private val preferences: AppPreferences) : Interceptor {

    final override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()

        val newBuilder = intercept(request.newBuilder())
        val segments = mutableListOf<String>()
        segments.addAll(request.url.pathSegments)
        segments.removeAt(0)
        segments.add(0, HelixApp.context.getLanguage().toString())
        val joinToString = segments.joinToString("/")
        val osVersion = Build.VERSION.SDK_INT.toString()
        val mode = preferences.getInt(PreferenceKey.DISPLAY_MODE, AppCompatDelegate.MODE_NIGHT_NO)
        val modeStr = if (mode == AppCompatDelegate.MODE_NIGHT_NO) LIGHT_MODE else NIGHT_MODE
        val paramsUrl = request.url.newBuilder(ApiConstants.BASE_URL + joinToString)
        val config = HelixApp.context.getConfig()
        paramsUrl?.addQueryParameter("mode", modeStr)
        paramsUrl?.addQueryParameter("applicationVersion", config.versionName)
        paramsUrl?.addQueryParameter("applicationId", config.appId)
        paramsUrl?.addQueryParameter("versionNumber", config.versionCode.toString())
        paramsUrl?.addQueryParameter("os_version", osVersion)
        paramsUrl?.addQueryParameter("deviceScale", config.scale)
        paramsUrl?.addQueryParameter("deviceType", "android")
        paramsUrl?.addQueryParameter("deviceModel", deviceName())
        request.url.queryParameterNames.forEach {
            paramsUrl?.addQueryParameter(it, request.url.queryParameter(it))
        }
        paramsUrl?.let {
            newBuilder.url(it.build())
            return chain.proceed(newBuilder.build())
        }
        return chain.proceed(request.newBuilder().build())
    }

    abstract fun intercept(requestBuilder: Request.Builder): Request.Builder
}