package com.example.mymoney.domain.repository

import com.example.mymoney.domain.entity.Account

interface AccountsRepository {
    suspend fun getAccountById(
        accountId: Int,
    ): Result<Account>
}