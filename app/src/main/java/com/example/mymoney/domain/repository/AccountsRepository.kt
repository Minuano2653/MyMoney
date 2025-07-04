package com.example.mymoney.domain.repository

import com.example.mymoney.domain.entity.Account
import kotlinx.coroutines.flow.Flow

/**
 * Репозиторий для работы с аккаунтами.
 */
interface AccountsRepository {
    suspend fun getAccountById(
        accountId: Int,
    ): Result<Account>

    suspend fun updateAccount(
        accountId: Int,
        name: String,
        balance: String,
        currency: String
    ): Result<Account>
}
