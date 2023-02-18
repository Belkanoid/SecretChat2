package com.belkanoid.secretchat2.domain.util

import android.util.Log
import java.util.concurrent.CancellationException

fun Throwable.rethrowCancellationException() {
    if (this is CancellationException) {
        throw this
    }
}