package com.example.mymoney.data.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

inline fun <LocalType, RemoteType> networkBoundResource(
    crossinline query: () -> Flow<LocalType>,
    crossinline fetch: suspend () -> RemoteType,
    crossinline saveFetchResult: suspend (RemoteType) -> Unit,
    crossinline shouldFetch: (LocalType) -> Boolean = { true },
    crossinline onFetchFailed: (Throwable) -> Unit = {}
): Flow<Resource<LocalType>> = flow {
    emit(Resource.Loading())

    val localData = query().first()
    emit(Resource.Loading(localData))

    if (shouldFetch(localData)) {
        try {
            val remoteData = fetch()
            saveFetchResult(remoteData)
            emitAll(query().map { Resource.Success(it) })
        } catch (e: Throwable) {
            onFetchFailed(e)
            emitAll(query().map { Resource.Error(e, it) })
        }
    } else {
        emitAll(query().map { Resource.Success(it) })
    }
}

sealed class Resource<T>(
    val data: T? = null,
    val error: Throwable? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(error: Throwable, data: T? = null) : Resource<T>(data, error)
}
