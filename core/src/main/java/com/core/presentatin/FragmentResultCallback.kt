package com.core.presentatin

import android.os.Bundle

interface FragmentResultCallback {
    fun onFragmentResult(key: String, result: Bundle)
}