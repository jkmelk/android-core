package com.core.api

import com.core.utils.BuildTypes


object ApiConstants {

    private const val BASE_URL_DEV = "https://test-api.fastshift.am/api/"
    private const val BASE_URL_QA = "https://test-api.fastshift.am/api/"
    private const val BASE_URL_LIVE = "https://test-api.fastshift.am/api/"

    val BASE_URL = when {
        BuildTypes.TYPE_LIVE -> BASE_URL_LIVE
        BuildTypes.TYPE_QA -> BASE_URL_QA
        else -> BASE_URL_DEV
    }
}

/**
 * Urls
 */
const val USER_NAME = "user_name"
const val PASSWORD = "password"
const val LOGIN = "login"
const val REGISTER = "register"
const val REGISTER_FACEBOOK = "signin/facebook"
const val REGISTER_VERIFY = "register/verify"
const val LOGIN_VERIFY = "login/verify"
const val COUNTRIES = "countries"
const val COMMUNITIES = "communities"
