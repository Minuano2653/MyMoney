package com.example.core.domain.repository

import kotlinx.coroutines.flow.Flow

interface NetworkRepository {
    fun getCurrentConnectionState(): Flow<Boolean>
}