package com.example.mymoney.data.repository

import com.example.mymoney.data.remote.datasource.AccountsRemoteDataSource
import com.example.mymoney.domain.entity.Account
import com.example.mymoney.domain.repository.AccountsRepository
import javax.inject.Inject

class AccountsRepositoryImpl @Inject constructor(
    private val remoteDataSource: AccountsRemoteDataSource
): AccountsRepository {
    override suspend fun getAccountById(accountId: Int): Result<Account> {
        return runCatching {
            remoteDataSource
                .getAccountById(accountId)
                .toDomain()
        }
    }
}