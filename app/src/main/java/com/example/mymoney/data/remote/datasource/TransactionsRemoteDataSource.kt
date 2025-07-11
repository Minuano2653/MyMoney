package com.example.mymoney.data.remote.datasource

import com.example.mymoney.data.remote.api.TransactionsApi
import com.example.mymoney.data.remote.dto.GetTransactionDto
import com.example.mymoney.data.remote.dto.TransactionRequest
import com.example.mymoney.data.remote.dto.TransactionResponse
import com.example.mymoney.data.remote.dto.TransactionDto
import retrofit2.Response
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

    suspend fun createTransaction(
        transactionRequest: TransactionRequest
    ): TransactionResponse {
        return api.createTransaction(transactionRequest)
    }

    suspend fun updateTransaction(
        transactionId: Int,
        transactionRequest: TransactionRequest,
    ): TransactionResponse {
        return api.updateTransaction(transactionId, transactionRequest)
    }

    suspend fun getTransaction(
        transactionId: Int,
    ): GetTransactionDto {
        return api.getTransaction(transactionId)
    }

    suspend fun deleteTransaction(transactionId: Int): Response<Unit> {
        return api.deleteTransaction(transactionId)
    }
}
