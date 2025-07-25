package com.example.core.network

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * Интерсептор для добавления заголовка авторизации с Bearer токеном к каждому HTTP-запросу.
 *
 * Токен берётся из [com.example.core.network.BuildConfig.AUTH_TOKEN].
 */
class AuthInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        requestBuilder.addHeader("Authorization", "Bearer ${BuildConfig.AUTH_TOKEN}")

        return chain.proceed(requestBuilder.build())
    }
}