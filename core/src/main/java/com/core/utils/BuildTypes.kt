package com.core.utils

import androidx.viewbinding.BuildConfig


object BuildTypes {

    const val DEV = "dev"
    const val QA = "qa"
    const val LIVE = "live"

    // TODO: migrate to flower
    val TYPE_DEV get() = BuildConfig.BUILD_TYPE == DEV
    val TYPE_QA get() = BuildConfig.BUILD_TYPE == QA
    val TYPE_LIVE get() = BuildConfig.BUILD_TYPE == LIVE
}
