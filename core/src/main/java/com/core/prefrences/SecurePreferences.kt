package com.core.prefrences

import android.app.Application
import com.orhanobut.hawk.Hawk
import java.io.Serializable

abstract class SecurePreferences(application: Application) {

    init {
        Hawk.init(application).build()
    }

    protected fun getString(key: String, defValue: String): String {
        return Hawk.get(key) ?: defValue
    }

    protected fun getInt(key: String, defValue: Int): Int {
        return Hawk.get(key) ?: defValue
    }

    protected fun putString(key: String, value: String) {
        Hawk.put(key, value)
    }

    protected fun putInt(key: String, value: Int) {
        Hawk.put(key, value)
    }

    protected fun putByteArray(key: String, value: ByteArray?) {
        Hawk.put(key, value)
    }

    protected fun getByteArray(key: String, defValue: ByteArray): ByteArray {
        return Hawk.get(key) ?: defValue
    }

    fun getLong(key: String, defValue: Long): Long {
        return Hawk.get(key) ?: defValue
    }

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return Hawk.get(key) ?: defValue
    }

    fun putLong(key: String, value: Long) {
        Hawk.put(key, value)
    }

    fun putBoolean(key: String, value: Boolean) {
        Hawk.put(key, value)
    }

    inline fun <reified T : Serializable> getSerializable(key: String) = Hawk.get(key) as? T

    inline fun <reified T : Serializable> putSerializable(key: String, value: T?) {
        Hawk.put(key, value)
    }
}