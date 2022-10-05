package com.data.model.base

import com.google.gson.annotations.SerializedName

data class AppResponse<T>(
        @SerializedName("data")
        val result: T? = null,
        @SerializedName("status")
        var status: String) : CompletableResponse()

open class CompletableResponse

data class ErrorResponse<T>(val status: String, val errors: T?=null)