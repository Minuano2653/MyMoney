package com.example.mymoney.data.remote.datasource

import com.example.mymoney.data.remote.api.TransactionsApi
import com.example.mymoney.data.remote.dto.TransactionDto
import javax.inject.Inject

class TransactionsRemoteDataSource @Inject constructor(
    private val api: TransactionsApi
) {
    suspend fun getTransactionsByPeriod(
        accountId: Int,
        startDate: String,
        endDate: String
    ): List<TransactionDto> {
        return api.getTransactionsByPeriod(accountId, startDate, endDate)
    }
}