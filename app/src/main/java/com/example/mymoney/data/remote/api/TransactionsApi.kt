package com.example.mymoney.data.remote.api

import com.example.mymoney.data.remote.dto.TransactionDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * API-интерфейс для работы с транзакциями.
 */
interface TransactionsApi {
    @GET("transactions/account/{accountId}/period")
    suspend fun getTransactionsByPeriod(
        @Path("accountId") accountId: Int,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): List<TransactionDto>
}
