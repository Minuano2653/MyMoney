package com.example.mymoney.domain.usecase

import com.example.mymoney.domain.repository.TransactionsRepository
import javax.inject.Inject

class DeleteTransactionUseCase @Inject constructor(
    private val repository: TransactionsRepository
) {
    suspend operator fun invoke(transactionId: Int): Result<Unit> {
        return repository.deleteTransaction(transactionId)
    }
}