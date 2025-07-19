package com.example.mymoney.data.repository

import com.example.mymoney.data.local.datasource.AccountDao
import com.example.mymoney.data.remote.datasource.AccountsRemoteDataSource
import com.example.mymoney.data.remote.dto.UpdateAccountRequest
import com.example.mymoney.data.repository.base.BaseRepository
import com.example.mymoney.domain.entity.Account
import com.example.mymoney.domain.repository.AccountsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class AccountsRepositoryImpl @Inject constructor(
    private val localDataSource: AccountDao,
    private val remoteDataSource: AccountsRemoteDataSource,
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

            localDataSource.upsert(account.toLocal())
            account.toDomain()
        }
    }

    override suspend fun getInitAccountInfo(): Result<Account> {
        return callWithRetry {
            val account = remoteDataSource.getAccounts()[0]
            localDataSource.upsert(account.toLocal())
            account.toDomain()
        }
    }

    override suspend fun getAccountId(): Result<Int> {
        return try {
            val localAccountId = localDataSource.getAccountId()

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

    override fun observeAccount(): Flow<Account?> {
        return localDataSource.observeAccount().map { it?.toDomain() }
    }

    override fun observeAccountById(accountId: Int): Flow<Account?> {
        return localDataSource.observeAccountById(accountId).map { it?.toDomain() }
    }
}
