package com.core.presentation

import android.os.Bundle

interface FragmentResultCallback {
    fun onFragmentResult(key: String, result: Bundle)
}