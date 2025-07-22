package com.example.mymoney.domain.usecase

import com.example.mymoney.domain.repository.NetworkRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveNetworkUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {
    operator fun invoke(): Flow<Boolean> = networkRepository.getCurrentConnectionState()
}