package com.example.mymoney.domain.usecase

import com.example.mymoney.domain.entity.Transaction
import com.example.mymoney.domain.repository.TransactionsRepository
import javax.inject.Inject

class GetTransactionUseCase @Inject constructor(
    private val transactionsRepository: TransactionsRepository
) {
    suspend operator fun invoke(transactionId: Int): Result<Transaction> {
        return transactionsRepository.getTransaction(transactionId)
    }
}