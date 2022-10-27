package com.core.manager

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.preference.PreferenceManager
import java.util.*

private const val LANGUAGE_KEY = "CHOOSE_LANGUAGE"

fun Context.setLocale(): Context {
    val savedLanguage = getLanguage()
    return savedLanguage?.let { updateResources(it) } ?: this
}

fun Context.setNewLocale(language: String): Context {
    persistLanguage(language)
    return updateResources(language)
}

fun Context.setNewLocale(newLocale: Locale): Context {
    persistLanguage(newLocale.toString())
    return updateResources(newLocale)
}

fun Context.getLanguage(): String? {
    val prefs = PreferenceManager.getDefaultSharedPreferences(this)
    val currentLocale = resources.getLocale()
    return if (!prefs.contains(LANGUAGE_KEY)) "hy" else prefs.getString(LANGUAGE_KEY, currentLocale.toString())
}

@SuppressLint("ApplySharedPref")
fun Context.persistLanguage(language: String) {
    val prefs = PreferenceManager.getDefaultSharedPreferences(this)
    prefs.edit().putString(LANGUAGE_KEY, language).apply()
}

fun Context.updateResources(language: String): Context {
    val locale = Locale(language)
    Locale.setDefault(locale)
    return updateResources(locale)
}

private fun Context.updateResources(locale: Locale): Context {
    var context = this
    val res = context.resources
    val config = Configuration(res.configuration)
    config.setLocale(locale)
    context = context.createConfigurationContext(config)
    res.updateConfiguration(config, res.displayMetrics)
    return context
}

fun Resources.getLocale(): Locale {
    return if (Build.VERSION.SDK_INT >= 24) configuration.locales[0] else configuration.locale
}

fun Context.getSavedLocale(): Locale {
    val savedLanguage = getLanguage()
    return if (savedLanguage == null) resources.getLocale() else Locale(getLanguage())
}
