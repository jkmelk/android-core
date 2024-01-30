package com.core.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


fun <T> SharedFlow<T>.subscribe(owner: LifecycleOwner, state: Lifecycle.State = Lifecycle.State.CREATED, block: (T) -> Unit) {
    owner.lifecycleScope.launch {
        owner.lifecycle.repeatOnLifecycle(state) {
            collectLatest { block(it) }
        }
    }
}

fun <T> SharedFlow<T>.subscribe(scope: CoroutineScope, block: (T) -> Unit) {
    scope.launch {
        collectLatest { block(it) }
    }
}

fun <T> MutableSharedFlow<T>.posValue(owner: LifecycleOwner, data: T) {
    owner.lifecycleScope.launch {
        emit(data)
    }
}

fun <T> MutableSharedFlow<T>.posValue(scope: CoroutineScope, data: T) {
    scope.launch {
        emit(data)
    }
}