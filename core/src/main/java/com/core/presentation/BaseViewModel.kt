package com.core.presentation


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.exceptions.AppError
import com.core.exceptions.ExceptionHandler
import com.core.exceptions.GlobalError
import com.core.exceptions.NoConnectionException
import com.core.model.ErrorResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.HttpException
import java.io.InterruptedIOException
import java.io.StringReader
import java.lang.reflect.Type
import java.net.SocketTimeoutException


typealias ViewStateLiveData = MutableLiveData<ViewState>

abstract class BaseViewModel : ViewModel(), KoinComponent {
    val globalErrorLiveFlow = MutableSharedFlow<String?>()

    private val bgContext = Dispatchers.IO
    private val mainContext = Dispatchers.Main
    private val exceptionHandler by inject<ExceptionHandler>()

    protected fun <T> launchInIO(handleException: Boolean = true,
                                 cancelPreviews: Boolean = false,
                                 block: suspend CoroutineScope.() -> T): Job {
        return viewModelScope.launch {
            withIOContext(handleException, block)
        }
    }

    protected fun <T> launch(block: suspend CoroutineScope.() -> T): Job {
        return viewModelScope.launch(mainContext) {
            block()
        }
    }

    protected suspend fun withMainContext(block: () -> Unit) {
        withContext(mainContext) {
            block()
        }
    }

    protected open fun onError(appError: AppError) {
        launchInIO {

            if (appError.exception is NoConnectionException || appError.exception is SocketTimeoutException || appError.exception is InterruptedIOException) {
                bgContext.cancel()
            }

            if (appError.exception is HttpException) {
                val code = appError.exception.code()
                if (code == 401 || code == 500) {
                    globalErrorLiveFlow.emit(appError.message)
                    return@launchInIO
                }
                val fromJson = Gson().fromJson(appError.body, GlobalError::class.java)
                if (fromJson?.message != null) {
                    globalErrorLiveFlow.emit(fromJson.message)
                    return@launchInIO
                }
            } else {
                globalErrorLiveFlow.emit(appError.message)
                return@launchInIO
            }
        }
    }

    private suspend fun <T> withIOContext(handleException: Boolean = true,
                                          block: suspend CoroutineScope.() -> T) {
        try {
            withContext(bgContext, block)
        } catch (exception: Exception) {
            Log.v("Response exeption", exception.message.toString())
            if (!handleException) {
                return
            }
            if (exception !is CancellationException) {
                onError(exception)
            }
        }
    }

    private fun onError(exception: Exception) {
        val appException = exceptionHandler.handleException(exception)
        onError(appException)
    }

    inline fun <reified T> handleError(appError: AppError, crossinline block: suspend (T) -> Unit) {
        val wrapedType: Type = object : TypeToken<ErrorResponse<T>>() {}.type
        val type: Type = object : TypeToken<T>() {}.type
        if (appError.body != null) {
            val newJsonReader = Gson().newJsonReader(StringReader(appError.body))
            val fromJson = Gson().fromJson<ErrorResponse<T>>(newJsonReader, wrapedType)
            if (fromJson is ErrorResponse) {
                val error = Gson().fromJson<T>(Gson().toJson(fromJson.errors), type)
                if (error != null) {
                    viewModelScope.launch {
                        block(error)
                    }
                }
            }
        }
    }

    fun clearCall() {
        bgContext.cancel()
    }
}