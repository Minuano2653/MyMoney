package com.example.mymoney.data.repository

import com.example.core.domain.repository.NetworkRepository
import com.example.mymoney.data.utils.NetworkMonitorImpl
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkRepositoryImpl @Inject constructor(
    private val networkMonitorImpl: NetworkMonitorImpl
) : NetworkRepository {

    override fun getCurrentConnectionState(): Flow<Boolean> {
        return networkMonitorImpl.isConnected
    }
}