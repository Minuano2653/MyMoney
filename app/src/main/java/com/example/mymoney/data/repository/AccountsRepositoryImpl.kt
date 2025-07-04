package com.example.mymoney.data.repository

import com.example.mymoney.data.remote.datasource.AccountsRemoteDataSource
import com.example.mymoney.data.remote.dto.UpdateAccountRequest
import com.example.mymoney.domain.entity.Account
import com.example.mymoney.domain.repository.AccountsRepository
import com.example.mymoney.data.repository.base.BaseRepository
import javax.inject.Inject

/**
 * Реализация репозитория аккаунтов, использующая удалённый источник данных.
 *
 * @param remoteDataSource Источник удалённых данных для аккаунтов.
 */
class AccountsRepositoryImpl @Inject constructor(
    private val remoteDataSource: AccountsRemoteDataSource,
): BaseRepository(), AccountsRepository {
    override suspend fun getAccountById(accountId: Int): Result<Account> {
        return callWithRetry {
            remoteDataSource
                .getAccountById(accountId)
                .toDomain()

        }
    }

    override suspend fun updateAccount(
        accountId: Int,
        name: String,
        balance: String,
        currency: String
    ): Result<Account> {
        return callWithRetry {
            val request = UpdateAccountRequest(
                name = name,
                balance = balance,
                currency = currency
            )
            remoteDataSource
                .updateAccount(accountId, request)
                .toDomain()
        }
    }
}
