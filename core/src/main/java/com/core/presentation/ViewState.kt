package com.core.presentation

sealed class ViewState {

    object Idle : ViewState()
    object Loading : ViewState()
    object Complete : ViewState()
    class Error(val message: String) : ViewState()
}
