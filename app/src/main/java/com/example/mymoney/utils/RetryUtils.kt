package com.example.mymoney.utils

import android.util.Log
import kotlinx.coroutines.delay

object RetryUtils {
    suspend fun <T> retryWithDelay(
        times: Int = 3,
        delayTimeMillis: Long = 2000,
        retryCondition: (Throwable) -> Boolean = { true },
        block: suspend () -> T
    ): T {
        var currentAttempt = 0
        var lastError: Throwable? = null

        while (currentAttempt <= times) {
            try {
                Log.d("RETRY", "Current Attempt: $currentAttempt")
                return block()
            } catch (e: Throwable) {
                if (!retryCondition(e)) throw e
                lastError = e
                currentAttempt++
                if (currentAttempt < times) {
                    delay(delayTimeMillis)
                }
            }
        }

        throw lastError ?: Exception("Unknown error after retrying")
    }
}