package com.core.api

import com.core.CoreConfig
import com.core.utils.BuildTypes


object ApiConstants {

    private val BASE_URL_DEV = CoreConfig.BASE_URL_DEV
    private val BASE_URL_QA = CoreConfig.BASE_URL_QA
    private val BASE_URL_LIVE = CoreConfig.BASE_URL_LIVE

    val BASE_URL = when {
        BuildTypes.TYPE_LIVE -> BASE_URL_LIVE
        BuildTypes.TYPE_QA -> BASE_URL_QA
        else -> BASE_URL_DEV
    }
}