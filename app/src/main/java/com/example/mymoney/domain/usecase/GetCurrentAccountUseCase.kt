package com.example.mymoney.domain.usecase

import com.example.mymoney.data.remote.datasource.AccountDataStore
import com.example.mymoney.domain.entity.Account
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentAccountUseCase @Inject constructor(
    private val accountDataStore: AccountDataStore
) {
    operator fun invoke(): Flow<Account?> = accountDataStore.getAccount()
}