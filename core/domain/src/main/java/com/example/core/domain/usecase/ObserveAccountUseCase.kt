package com.example.core.domain.usecase

import com.example.core.domain.entity.Account
import com.example.core.domain.repository.AccountsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveAccountUseCase @Inject constructor(
    private val repository: AccountsRepository,
) {
    operator fun invoke(): Flow<Account?> = repository.observeAccount()
}