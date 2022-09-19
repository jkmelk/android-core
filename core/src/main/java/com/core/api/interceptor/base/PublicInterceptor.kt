package com.core.api.interceptor.base

import com.core.HelixApp
import com.core.api.ApiConstants
import com.core.manager.getLanguage
import com.core.prefrences.AppPreferences
import com.core.prefrences.PreferenceKey
import com.core.utils.extensions.deviceToken
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import retrofit2.Invocation

abstract class PublicInterceptor(private val preferences: AppPreferences) : Interceptor {

    final override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        val invocation = request.tag(Invocation::class.java)
                ?: return chain.proceed(request)
        val isPublic = invocation.method().getAnnotation(PublicRequest::class.java) != null

        val newBuilder = intercept(request.newBuilder())
        val segments = mutableListOf<String>()
        segments.addAll(request.url.pathSegments)
        segments.removeAt(0)
        segments.add(0, HelixApp.context.getLanguage().toString())
        val joinToString = segments.joinToString("/")

        val paramsUrl = request.url.newBuilder(ApiConstants.BASE_URL + joinToString)

        paramsUrl?.addQueryParameter("applicationVersion", preferences.getString(PreferenceKey.VERSION_NAME))
        paramsUrl?.addQueryParameter("applicationId", HelixApp.context.deviceToken())
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