package com.core.manager

import android.app.Application
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import kotlinx.coroutines.flow.MutableStateFlow

class NetworkManager(app: Application) {

    private val networkCallback by lazy { networkCallBack() }

    val connectionStateFlow = MutableStateFlow(NetworkState.AVAILABLE)

    val isConnected get() = connectionStateFlow.value == NetworkState.AVAILABLE

    init {
        val manager = app.getSystemService(ConnectivityManager::class.java)
        val isConnected = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            manager?.activeNetwork != null
        } else {
            val activeNetworkInfo = manager?.activeNetworkInfo
            activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
        val initialNetworkState = if (isConnected) {
            NetworkState.AVAILABLE
        } else {
            NetworkState.UNAVAILABLE
        }
        connectionStateFlow.value = initialNetworkState

        val networkRequest = NetworkRequest.Builder().build()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            manager?.registerDefaultNetworkCallback(networkCallback)
        } else {
            manager?.registerNetworkCallback(networkRequest, networkCallback)
        }
    }

    private fun networkCallBack(): ConnectivityManager.NetworkCallback {
        return object : ConnectivityManager.NetworkCallback() {

            override fun onLost(network: Network) {
                if (connectionStateFlow.value != NetworkState.UNAVAILABLE) {
                    connectionStateFlow.value = NetworkState.UNAVAILABLE
                }
            }

            override fun onUnavailable() {
                if (connectionStateFlow.value != NetworkState.UNAVAILABLE) {
                    connectionStateFlow.value = NetworkState.UNAVAILABLE
                }
            }

            override fun onAvailable(network: Network) {
                if (connectionStateFlow.value != NetworkState.AVAILABLE) {
                    connectionStateFlow.value = NetworkState.AVAILABLE
                }
            }
        }
    }
}

enum class NetworkState {
    AVAILABLE,
    UNAVAILABLE,
}