package com.core.prefrences

import android.app.Application
import com.core.prefrences.PreferenceKey
import com.core.prefrences.SecurePreferences
import com.orhanobut.hawk.Hawk

class AppPreferences(application: Application) : SecurePreferences(application) {

    fun getString(preferenceKey: PreferenceKey, defValue: String = ""): String {
        return getString(preferenceKey.keyName, defValue)
    }

    fun putString(preferenceKey: PreferenceKey, value: String) {
        putString(preferenceKey.keyName, value)
    }

    fun getByteArray(preferenceKey: PreferenceKey, defValue: ByteArray = byteArrayOf()): ByteArray {
        return getByteArray(preferenceKey.keyName, defValue)
    }

    fun putByteArray(preferenceKey: PreferenceKey, value: ByteArray) {
        putByteArray(preferenceKey.keyName, value)
    }

    fun getInt(preferenceKey: PreferenceKey, defValue: Int): Int {
        return getInt(preferenceKey.keyName, defValue)
    }

    fun putInt(preferenceKey: PreferenceKey, value: Int) {
        putInt(preferenceKey.keyName, value)
    }

    fun getBoolean(preferenceKey: PreferenceKey): Boolean {
        return getBoolean(preferenceKey.keyName, false)
    }

    fun putBoolean(preferenceKey: PreferenceKey, value: Boolean) {
        putBoolean(preferenceKey.keyName, value)
    }

    fun clear() {
        Hawk.deleteAll()
    }
}