package com.example.core.domain.usecase

import com.example.core.domain.entity.Transaction
import com.example.core.domain.repository.TransactionsRepository
import javax.inject.Inject

class GetTransactionUseCase @Inject constructor(
    private val transactionsRepository: TransactionsRepository
) {
    suspend operator fun invoke(transactionId: Int): Result<Transaction> {
        return transactionsRepository.getTransaction(transactionId)
    }
}