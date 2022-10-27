package com.core.exceptions

import com.core.logger.log
import com.fastshift.fast_and_shift.data.exceptions.NotKeyStoreException
import com.google.gson.Gson
import java.io.InterruptedIOException
import java.lang.reflect.UndeclaredThrowableException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ExceptionHandler {


    private val defaultMessage = "App.context.getString(R.string.defoult_error_message)"
    private val defaultException = AppError(defaultMessage)

    fun handleException(exception: Exception?): AppError {
        val type = when (exception) {
            is ApiException -> //handled exception from api
                "Api"
            is ApiJsonDataException -> //Wrong json structure from api
                "Json structure"
            is SocketTimeoutException, is InterruptedIOException -> //Timeout
                "Time out"
            is retrofit2.HttpException -> {
                "Http Exception ${exception.code()}"
            }
            is NoConnectionException, is UnknownHostException ->
                "No internet"
            is NotKeyStoreException -> //Timeout
                "Not KeyStore"
            else -> //Global exceptions
                "Global $exception"
        }

        log { "$type Message - ${exception?.message}" }

        val message = when (exception) {
            is retrofit2.HttpException -> {

                val appException = defaultException.copy(exception = exception)
                val errorResponseBody = exception.response()?.errorBody() ?: return appException
                val errorResponseString = errorResponseBody.string()
                val fromJson = Gson().fromJson(errorResponseString, AppError::class.java)
                if (fromJson.message != null) {
                    return defaultException.copy(
                        message = fromJson.message,
                        exception = exception,
                        body = errorResponseString
                    )
                }
                return defaultException.copy(message = "Http error message", exception = exception, errorResponseString)
            }
            is ApiException -> exception.message
            is NotKeyStoreException -> exception.message
            is ApiJsonDataException ->" App.context.getString(R.string.server_error)"
            is NoConnectionException, is UnknownHostException -> "App.context.getString(R.string.no_internet_conntection)"
            is SocketTimeoutException, is InterruptedIOException -> "App.context.getString(R.string.time_out_error)"
            is UndeclaredThrowableException -> handleException(exception.cause as? Exception).message
            else -> defaultMessage
        }
        return defaultException.copy(message = message, exception = exception)
    }
}