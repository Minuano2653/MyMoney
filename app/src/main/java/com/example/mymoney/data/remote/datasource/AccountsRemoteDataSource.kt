package com.example.mymoney.data.remote.datasource

import com.example.mymoney.data.remote.api.AccountsApi
import com.example.mymoney.data.remote.dto.AccountDto
import javax.inject.Inject

class AccountsRemoteDataSource @Inject constructor(
    private val api: AccountsApi
)  {
    suspend fun getAccountById(id: Int): AccountDto {
        return api.getAccountById(id)
    }
}