package com.belkanoid.secretchat2.domain.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.merge
import java.util.concurrent.CancellationException

fun Throwable.rethrowCancellationException() {
    if (this is CancellationException) {
        throw this
    }
}

fun <T> Flow<T>.mergeWith(another: Flow<T>): Flow<T> {
    return merge(this, another)
}