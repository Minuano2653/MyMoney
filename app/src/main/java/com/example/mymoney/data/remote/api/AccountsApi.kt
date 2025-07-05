package com.example.mymoney.data.remote.api

import com.example.mymoney.data.remote.dto.AccountDto
import com.example.mymoney.data.remote.dto.ShortAccountDto
import com.example.mymoney.data.remote.dto.UpdateAccountRequest
import com.example.mymoney.data.remote.dto.UpdateAccountResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * API-интерфейс для работы с аккаунтами.
 */
interface AccountsApi {
    @GET("accounts/{id}")
    suspend fun getAccountById(@Path("id") id: Int): AccountDto

    @GET("accounts")
    suspend fun getAccounts(): List<ShortAccountDto>

    @PUT("accounts/{id}")
    suspend fun updateAccount(@Path("id") id: Int, @Body request: UpdateAccountRequest): UpdateAccountResponse
}
