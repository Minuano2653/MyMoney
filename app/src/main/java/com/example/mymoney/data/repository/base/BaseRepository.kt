package com.example.mymoney.data.repository.base

import android.util.Log
import kotlinx.coroutines.delay
import retrofit2.HttpException

/**
 * Базовый репозиторий с универсальной функцией повторных попыток выполнения запроса.
 */
abstract class BaseRepository {
    suspend fun <T> callWithRetry(
        times: Int = 3,
        delayMillis: Long = 2000,
        retryCondition: (Throwable) -> Boolean = {it is HttpException && it.code() == 500},
        block: suspend () -> T
    ): Result <T> {
        var currentAttempt = 0
        var lastError: Throwable? = null

        while (currentAttempt <= times) {
            try {
                Log.d("RETRY", "Current Attempt: $currentAttempt")
                val result = block()
                return Result.success(result)
            } catch (e: Throwable) {
                if (!retryCondition(e)) return Result.failure(e)
                lastError = e
                currentAttempt++
                if (currentAttempt < times) {
                    delay(delayMillis)
                }
            }
        }
        return Result.failure(lastError ?: IllegalStateException("Unknown error after $times retries"))
    }
}
