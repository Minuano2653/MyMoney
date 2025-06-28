package com.example.mymoney.domain.repository

import com.example.mymoney.domain.entity.Account

/**
 * Репозиторий для работы с аккаунтами.
 */
interface AccountsRepository {
    suspend fun getAccountById(
        accountId: Int,
    ): Result<Account>
}
