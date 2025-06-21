package com.example.mymoney.domain.repository

import com.example.mymoney.domain.entity.Transaction

interface TransactionsRepository {
    suspend fun getTransactionsByPeriod(
        accountId: Int,
        startDate: String,
        endDate: String
    ): Result<List<Transaction>>
}