package com.core.api.interceptor

import com.core.api.interceptor.base.PrivateInterceptor
import com.core.prefrences.AppPreferences
import com.core.prefrences.PreferenceKey
import okhttp3.Request

class PrivateHeaderInterceptor(private val preferences: AppPreferences) : PrivateInterceptor() {

    override fun intercept(requestBuilder: Request.Builder): Request.Builder {
        val key = preferences.getString(PreferenceKey.REGISTRATION_KEY, "Bearer ")
        val token = preferences.getString(PreferenceKey.TOKEN)
        if (key.isEmpty() || token.isEmpty()) {
            return requestBuilder
        }

        return requestBuilder
    }
}