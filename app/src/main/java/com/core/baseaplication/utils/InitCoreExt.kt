package com.core.baseaplication.utils

import android.content.Context
import com.core.CoreConfig
import com.core.baseaplication.BuildConfig
import com.core.baseaplication.R
import com.core.utils.extensions.deviceToken

fun Context.initCoreConfig() {
    CoreConfig.MAIN_CONTAINER = R.id.main_container

    CoreConfig.VERSION_NAME = BuildConfig.VERSION_NAME
    CoreConfig.VERSION_CODE = BuildConfig.VERSION_CODE
    CoreConfig.APP_ID = deviceToken()

    CoreConfig.BASE_URL_DEV = ""
    CoreConfig.BASE_URL_QA = ""
    CoreConfig.BASE_URL_LIVE = ""
}