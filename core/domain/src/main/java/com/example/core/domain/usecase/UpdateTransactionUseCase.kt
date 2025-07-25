package com.example.core.domain.usecase

import com.example.core.domain.entity.SavedTransaction
import com.example.core.domain.repository.TransactionsRepository
import javax.inject.Inject

class UpdateTransactionUseCase @Inject constructor(
    private val transactionsRepository: TransactionsRepository
) {
    suspend operator fun invoke(
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String?,
        transactionId: Int
    ): Result<SavedTransaction> {
        return transactionsRepository.updateTransaction(
            accountId,
            categoryId,
            amount,
            transactionDate,
            comment,
            transactionId
        )
    }
}