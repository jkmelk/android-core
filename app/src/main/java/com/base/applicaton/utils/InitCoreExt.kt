package com.base.applicaton.utils

import android.content.Context
import com.base.applicaton.BuildConfig
import com.base.applicaton.R
import com.core.CoreConfig
import com.yt.utils.extensions.deviceToken

fun Context.initCoreConfig() {
    CoreConfig.MAIN_CONTAINER = R.id.main_container

    CoreConfig.VERSION_NAME = BuildConfig.VERSION_NAME
    CoreConfig.VERSION_CODE = BuildConfig.VERSION_CODE
    CoreConfig.APP_ID = deviceToken()

    CoreConfig.BASE_URL_DEV = "https://test-api.fastshift.am/api/"
    CoreConfig.BASE_URL_QA = ""
    CoreConfig.BASE_URL_LIVE = ""
}