package com.data.api

import com.data.model.base.AppResponse
import com.data.model.response.ConfigResponse
import okhttp3.ResponseBody
import retrofit2.http.GET

interface SplashApiService {

    @GET("config")
    suspend fun getAppConfigAsync(): AppResponse<ConfigResponse>

    @GET("dictionary")
    suspend fun dictionary(): ResponseBody
}