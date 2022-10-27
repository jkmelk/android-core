package com.core.logger

import timber.log.Timber

inline fun log(tag: String = "", message: () -> Any?) {
    val timber = Timber
    timber.tag(tag)
    timber.e(message().toString())
}
