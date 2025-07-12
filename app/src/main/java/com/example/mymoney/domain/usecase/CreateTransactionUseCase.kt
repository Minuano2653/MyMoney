package com.example.mymoney.domain.usecase

import com.example.mymoney.domain.entity.SavedTransaction
import com.example.mymoney.domain.repository.TransactionsRepository
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