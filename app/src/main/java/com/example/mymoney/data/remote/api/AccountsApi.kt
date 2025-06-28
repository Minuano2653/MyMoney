package com.example.mymoney.data.remote.api

import com.example.mymoney.data.remote.dto.AccountDto
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * API-интерфейс для работы с аккаунтами.
 */
interface AccountsApi {
    @GET("accounts/{id}")
    suspend fun getAccountById(@Path("id") id: Int): AccountDto
}
