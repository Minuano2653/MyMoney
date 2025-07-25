package com.example.core.domain.usecase

import com.example.core.domain.entity.SavedTransaction
import com.example.core.domain.repository.TransactionsRepository
import javax.inject.Inject

class CreateTransactionUseCase @Inject constructor(
    private val transactionsRepository: TransactionsRepository
) {
    suspend operator fun invoke(
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String?
    ): Result<SavedTransaction> {
        return transactionsRepository.createTransaction(
            accountId,
            categoryId,
            amount,
            transactionDate,
            comment
        )
    }
}