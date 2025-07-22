package com.example.mymoney.domain.repository

import kotlinx.coroutines.flow.Flow

interface NetworkRepository {
    fun getCurrentConnectionState(): Flow<Boolean>
}