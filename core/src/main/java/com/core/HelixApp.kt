package com.core

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.webkit.WebView
import androidx.appcompat.app.AppCompatDelegate
import com.core.di.module.apiModule
import com.core.di.module.appModule
import com.core.logger.log
import com.core.manager.setLocale
import com.core.prefrences.AppPreferences
import com.core.prefrences.PreferenceKey
import com.yt.utils.extensions.delayed
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

open class HelixApp : Application() {
    protected val preferences by inject<AppPreferences>()

    companion object {
        lateinit var context: HelixApp
    }

    init {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        if (AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_NO) {
            WebView(applicationContext)
        }
        startKoin {
            androidContext(this@HelixApp)
            modules(apiModule + appModule)
        }
        Timber.plant(object : Timber.DebugTree() {
            override fun createStackElementTag(element: StackTraceElement): String {
                return "(${element.fileName}:${element.lineNumber})" + element.methodName
            }
        })
        preferences.putString(PreferenceKey.TOKEN, " ")
        delayed(5000) {
            log { preferences.getString(PreferenceKey.VERSION_NAME) }
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base?.setLocale())
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setLocale()
    }
}