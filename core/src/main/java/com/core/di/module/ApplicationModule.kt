package com.core.di.module

import com.core.exceptions.ExceptionHandler
import com.core.manager.NetworkManager
import com.core.prefrences.AppPreferences
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val appModule = module {

    single {
        ExceptionHandler()
    }

    single {
        AppPreferences(androidApplication())
    }

    single {
        NetworkManager(androidApplication())
    }

}

