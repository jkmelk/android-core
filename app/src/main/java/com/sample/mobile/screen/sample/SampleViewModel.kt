package com.sample.mobile.screen.sample

import androidx.lifecycle.viewModelScope
import com.core.exceptions.AppError
import com.core.logger.log
import com.core.presentation.BaseViewModel
import com.core.utils.subscribe
import com.data.model.response.ConfigResponse
import com.data.repository.ConfigRepo

class SampleViewModel(private val repo: ConfigRepo) : BaseViewModel() {

    val configFlow = repo.getConfigFlow

    init {
        getConfigs()
        initObservers()
    }

    /*Handle data if need to manage info in ViewModel*/
    private fun initObservers() {
        configFlow.subscribe(viewModelScope) { handleConfigResult(it) }
    }

    private fun handleConfigResult(it: ConfigResponse) {
        log { it }
    }

    private fun getConfigs() = launchInIO {
        repo.getConfig()
    }

    override fun onError(appError: AppError) {
        super.onError(appError)
        log { appError.message }
    }
}