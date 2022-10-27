package com.data.repository

import com.data.api.SplashApiService
import com.data.model.response.ConfigResponse
import kotlinx.coroutines.flow.MutableSharedFlow

class ConfigRepo(private val splashApiService: SplashApiService) {

    private val _getConfigFlow = MutableSharedFlow<ConfigResponse>()
    val getConfigFlow = _getConfigFlow

    suspend fun getConfig() {
        val result = splashApiService.getAppConfigAsync()
        result.result?.let { _getConfigFlow.emit(it) }
    }
}