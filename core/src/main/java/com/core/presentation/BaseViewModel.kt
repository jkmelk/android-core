package com.core.presentation


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.exceptions.*
import com.core.model.ErrorResponse
import com.core.prefrences.AppPreferences
import com.core.prefrences.PreferenceKey
import com.core.utils.posValue
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.HttpException
import java.io.InterruptedIOException
import java.io.StringReader
import java.lang.reflect.Type
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


typealias ViewStateLiveData = MutableLiveData<ViewState>

val globalErrorLiveFlow = MutableSharedFlow<String?>()

val noInternetFlow = MutableSharedFlow<Boolean>()
val forceLogoutErrorLiveFlow = MutableSharedFlow<String?>()
var viewState = ViewStateLiveData(ViewState.Complete)

abstract class BaseViewModel : ViewModel(), KoinComponent {

    private val calls = mutableSetOf<suspend CoroutineScope.() -> Any>()
    private val mutex = Mutex()
    private val bgContext = Dispatchers.IO
    private val mainContext = Dispatchers.Main
    private val exceptionHandler by inject<ExceptionHandler>()
    protected val preferences by inject<AppPreferences>()
    protected val flow = MutableSharedFlow<suspend CoroutineScope.() -> Any>()

    protected fun <T> launchInIO(handleException: Boolean = true,
                                 cancelPreviews: Boolean = false,
                                 job: CompletableJob = Job(),
                                 block: suspend CoroutineScope.() -> T): Job {
        return viewModelScope.launch(job) {
            withIOContext(handleException, block)
        }
    }

    protected fun <T, B> launchInIO(handleException: Boolean = true,
                                    cancelPreviews: Boolean = false,
                                    job: CompletableJob = Job(),
                                    requestBody: B,
                                    block: suspend CoroutineScope.() -> T): Job {
        return viewModelScope.launch(job) {
            withIOContext(handleException, requestBody, block)
        }
    }

    open suspend fun retryCalls() {
        val copy = calls.toSet()
        calls.clear()
        copy.forEach {
            withIOContext(true, it)
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
        //  viewState.postValue(ViewState.Complete)
        if (appError.exception is HttpException) {
            val code = appError.exception.code()
            if (code == 401) {
                forceLogoutErrorLiveFlow.posValue(viewModelScope, appError.message)
                return
            } else if (code == 500 || code == 502) {
                globalErrorLiveFlow.posValue(viewModelScope, appError.message)
                return
            }
            val fromJson = Gson().fromJson(appError.body, GlobalError::class.java)
            if (fromJson?.message != null) {
                globalErrorLiveFlow.posValue(viewModelScope, fromJson.message)
                return
            }

        } else if (appError.exception is NoConnectionException
                || appError.exception is UnknownHostException
                || appError.exception is InterruptedIOException
                || appError.exception is SocketException
                || appError.exception is SocketTimeoutException) {
            noInternetFlow.posValue(viewModelScope, true)
        } else {
            globalErrorLiveFlow.posValue(viewModelScope, appError.message)
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
                onError(exception, null, block)
            }
        }
    }

    private suspend fun <T, B> withIOContext(handleException: Boolean = true,
                                             requestBody: B,
                                             block: suspend CoroutineScope.() -> T) {
        try {
            withContext(bgContext, block)
        } catch (exception: Exception) {
            Log.v("Response exeption", exception.message.toString())
            if (!handleException) {
                return
            }
            if (exception !is CancellationException) {
                onError(exception, requestBody, block)
            }
        }
    }

    private suspend fun <T, B> onError(exception: Exception, requestBody: B? = null, block: suspend CoroutineScope.() -> T) {
        val appError = exceptionHandler.handleException(exception, requestBody)
        if (appError.exception is UnknownHostException || appError.exception is InterruptedIOException || appError.exception is NoConnectionException) {
            mutex.withLock {
                calls.add(block as suspend CoroutineScope.() -> Any)
            }
        }
        onError(appError)
    }

    protected suspend inline fun <reified T> asyncDeferred(deferred: Deferred<T>, defaultValue: T) = try {
        deferred.await()
    } catch (e: Exception) {
        println("Handle $e in try/catch")
        defaultValue
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

    fun deleteJwtToken() = preferences.run {
        putByteArray(PreferenceKey.PIN_IV, byteArrayOf())
        putByteArray(PreferenceKey.USER_PIN, byteArrayOf())
        putBoolean(PreferenceKey.PIN_CODE_ATTACHED, false)
        putByteArray(PreferenceKey.USER_IV, byteArrayOf())
        putByteArray(PreferenceKey.USER_BIO, byteArrayOf())
        putBoolean(PreferenceKey.BIOMETRIC_ATTACHED_KEY, false)
        putString(PreferenceKey.TOKEN, "")
    }

    fun clearCall() {
        bgContext.cancel()
    }
}