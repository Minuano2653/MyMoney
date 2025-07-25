package com.example.core.domain.repository

import com.example.core.domain.entity.Account
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

    suspend fun getInitAccountInfo(): Result<Account>

    suspend fun getAccountId(): Result<Int>

    fun observeAccount(): Flow<Account?>

    fun observeAccountById(accountId: Int): Flow<Account?>
}
