package com.belkanoid.secretchat2.data.remote


import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class ChatInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequest = request.newBuilder()
            .url(request.url())
            .header("Content-Type", "application/json")
            .header("Accept-Encoding", "identity")
            .build()

        return chain.proceed(newRequest)
    }
}