package com.core.api

import com.core.exceptions.ApiJsonDataException
import com.google.gson.annotations.SerializedName

data class AppResponse<T>(
        @SerializedName("data")
        val result: T? = null,
        @SerializedName("status")
        var status: String) : CompletableResponse()

open class CompletableResponse

fun <T> AppResponse<T>.getOrThrow(): T {
    return result ?: throw ApiJsonDataException("Result expected not to be null")
}

data class ErrorResponse<T>(val status: String, val errors: T?=null)