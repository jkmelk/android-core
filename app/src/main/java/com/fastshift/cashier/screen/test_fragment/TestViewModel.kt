package com.fastshift.cashier.screen.test_fragment

import androidx.lifecycle.viewModelScope
import com.core.presentation.BaseViewModel
import com.core.utils.posValue
import com.core.utils.subscribe
import com.data.model.response.ConfigResponse
import com.data.repository.ConfigRepo
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class TestViewModel(private val repo: ConfigRepo) : BaseViewModel() {

    private val configFlow = repo.getConfigFlow
    private val _configForUi = MutableSharedFlow<ConfigResponse>()
    val configForUi = _configForUi.asSharedFlow()
    private val _configForUi2 = MutableSharedFlow<ConfigResponse>()
    val configForUi2 = _configForUi2.asSharedFlow()

    init {
        modifyConfigs()
    }

    fun getConfigs() = launchInIO {
        repo.getConfig()
    }

    /*Use if needed to manage data from repo*/
    private fun modifyConfigs() = configFlow.subscribe(viewModelScope) {
        if (it.dictionaryUpdated) {
            it.dictionaryUpdated = false
        } else {
            it.dictionaryUpdated = true
        }
        _configForUi.posValue(viewModelScope, it)
    }
}