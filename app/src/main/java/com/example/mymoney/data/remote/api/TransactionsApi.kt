package com.example.mymoney.data.remote.api

import com.example.mymoney.data.remote.dto.GetTransactionDto
import com.example.mymoney.data.remote.dto.TransactionRequest
import com.example.mymoney.data.remote.dto.TransactionResponse
import com.example.mymoney.data.remote.dto.TransactionDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
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

    @POST("transactions")
    suspend fun createTransaction(
        @Body transactionRequest: TransactionRequest
    ): TransactionResponse

    @PUT("transactions/{id}")
    suspend fun updateTransaction(
        @Path("id") transactionId: Int,
        @Body transactionRequest: TransactionRequest
    ): TransactionResponse

    @GET("transactions/{id}")
    suspend fun getTransaction(
        @Path("id") transactionId: Int,
    ): GetTransactionDto

    @DELETE("transactions/{id}")
    suspend fun deleteTransaction(
        @Path("id") transactionId: Int,
    ) : Response<Unit>
}
