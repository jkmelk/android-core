package com.core.api.interceptor.base

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import retrofit2.Invocation

abstract class PrivateInterceptor : Interceptor {

    final override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val invocation = request.tag(Invocation::class.java)
                ?: return chain.proceed(request)
        val isPrivate = invocation.method().getAnnotation(PublicRequest::class.java) == null
        if (isPrivate) {
            val newBuilder = intercept(request.newBuilder())
            val uriBuilder = request.url.newBuilder()
            uriBuilder.addQueryParameter("deviceType", "android")
            return chain.proceed(newBuilder.build())
        }
        return chain.proceed(request)
    }

    abstract fun intercept(requestBuilder: Request.Builder): Request.Builder
}