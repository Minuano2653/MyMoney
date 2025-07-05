package com.example.mymoney.data.repository

import com.example.mymoney.data.remote.datasource.AccountDataStore
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
    private val dataStore: AccountDataStore
) : BaseRepository(), AccountsRepository {
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
            val account = remoteDataSource
                .updateAccount(accountId, request)
                .toDomain()

            dataStore.saveAccount(account)
            account
        }
    }

    override suspend fun getInitAccountInfo(): Result<Account> {
        return callWithRetry {
            val account = remoteDataSource.getAccounts()[0].toDomain()

            dataStore.saveAccount(account)
            account
        }
    }

    override suspend fun getAccountId(): Result<Int> {
        return try {
            val localAccountId = dataStore.getAccountId()

            if (localAccountId != null) {
                Result.success(localAccountId)
            } else {
                getInitAccountInfo()
                    .map { account -> account.id }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
