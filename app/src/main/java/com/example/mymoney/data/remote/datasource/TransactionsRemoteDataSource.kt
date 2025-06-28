package com.example.mymoney.data.remote.datasource

import com.example.mymoney.data.remote.api.TransactionsApi
import com.example.mymoney.data.remote.dto.TransactionDto
import javax.inject.Inject

/**
 * Источник удалённых данных для работы с транзакциями.
 *
 * @param api API-интерфейс для выполнения сетевых запросов к транзакциям.
 */
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
