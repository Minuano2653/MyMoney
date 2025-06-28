package com.example.mymoney.domain.repository

import com.example.mymoney.domain.entity.Transaction

/**
 * Репозиторий для работы с транзакциями.
 */
interface TransactionsRepository {
    suspend fun getTransactionsByPeriod(
        accountId: Int,
        startDate: String,
        endDate: String
    ): Result<List<Transaction>>
}
