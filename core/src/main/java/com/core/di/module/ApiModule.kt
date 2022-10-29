package com.core.di.module


import androidx.viewbinding.BuildConfig
import com.core.api.ApiConstants
import com.core.api.interceptor.ConnectionInterceptor
import com.core.api.interceptor.PrivateHeaderInterceptor
import com.core.api.interceptor.PublicHeaderInterceptor
import com.google.gson.GsonBuilder
import com.core.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val apiModule
    get() = listOf(interceptorModule, retrofitModule)

private const val TIMEOUT = 60L

private val interceptorModule = module {

    single {
        PublicHeaderInterceptor(get())
    }

    single {
        PrivateHeaderInterceptor(get())
    }

    single {
        ConnectionInterceptor(get())
    }
}

private val retrofitModule = module {

    single {
        val okHttpBuilder = OkHttpClient.Builder()

        with(okHttpBuilder) {
            if (BuildConfig.DEBUG) {
                addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            }
            addInterceptor(get<PublicHeaderInterceptor>())
            addInterceptor(get<PrivateHeaderInterceptor>())
            addInterceptor(get<ConnectionInterceptor>())

            readTimeout(TIMEOUT, TimeUnit.SECONDS)
            connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            callTimeout(TIMEOUT, TimeUnit.SECONDS)
        }
        okHttpBuilder
    }

    single {
        val gsonBuilder = GsonBuilder()
                .setLenient()
        val okHttp = get<OkHttpClient.Builder>()
                .build()

        Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                .client(okHttp)
    }

    factory {
        get<Retrofit.Builder>()
                .baseUrl(ApiConstants.BASE_URL)
                .build()
    }
}
