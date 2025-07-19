package com.example.mymoney.domain.usecase

import com.example.mymoney.data.remote.datasource.AccountDataStore
import com.example.mymoney.domain.entity.Account
import com.example.mymoney.domain.repository.AccountsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveAccountUseCase @Inject constructor(
    private val repository: AccountsRepository,
) {
    operator fun invoke(): Flow<Account?> = repository.observeAccount()
}