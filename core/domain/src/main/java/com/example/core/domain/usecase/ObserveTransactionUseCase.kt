package com.example.core.domain.usecase

import com.example.core.domain.entity.Resource
import com.example.core.domain.entity.Transaction
import com.example.core.domain.repository.TransactionsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveTransactionUseCase @Inject constructor(
    private val transactionsRepository: TransactionsRepository
) {
    operator fun invoke(transactionId: Int): Flow<Resource<Transaction?>> {
        return transactionsRepository.observeTransaction(transactionId)
    }
}
