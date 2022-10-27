package com.core.api.interceptor

import com.core.exceptions.NoConnectionException
import com.core.manager.NetworkManager
import okhttp3.Interceptor
import okhttp3.Response

class ConnectionInterceptor(private val networkManager: NetworkManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!networkManager.isConnected) {
            throw NoConnectionException("No Connection")
        }
        return chain.proceed(chain.request())
    }

}