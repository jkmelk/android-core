package com.data.di

import com.data.api.SplashApiService
import com.data.repository.ConfigRepo
import org.koin.dsl.module
import retrofit2.Retrofit

val dataModule
    get() = repoModule + apiModule

private val repoModule = module {
    single { ConfigRepo(get()) }
}

private val apiModule = module {
    single { get<Retrofit>().create(SplashApiService::class.java) }
}
