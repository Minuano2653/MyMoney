package com.example.mymoney.data.repository

import com.example.mymoney.domain.repository.NetworkRepository
import com.example.mymoney.utils.NetworkMonitor
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkRepositoryImpl @Inject constructor(
    private val networkMonitor: NetworkMonitor
) : NetworkRepository {

    override fun getCurrentConnectionState(): Flow<Boolean> {
        return networkMonitor.isConnected
    }
}