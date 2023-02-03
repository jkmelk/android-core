package com.core.api

import com.core.HelixApp
import com.core.utils.BuildTypes


object ApiConstants {

    private val BASE_URL_DEV = HelixApp.context.getConfig().baseUrlDev
    private val BASE_URL_QA = HelixApp.context.getConfig().baseUrlQa
    private val BASE_URL_LIVE = HelixApp.context.getConfig().baseUrlLive

    val BASE_URL = when {
        BuildTypes.TYPE_LIVE -> BASE_URL_LIVE
        BuildTypes.TYPE_QA -> BASE_URL_QA
        else -> BASE_URL_DEV
    }
}