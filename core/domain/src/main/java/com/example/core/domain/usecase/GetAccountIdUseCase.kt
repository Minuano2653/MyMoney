package com.example.core.domain.usecase

import com.example.core.domain.repository.AccountsRepository
import javax.inject.Inject

class GetAccountIdUseCase @Inject constructor(
    private val repository: AccountsRepository
) {
    suspend operator fun invoke(): Result<Int> {
        return repository.getAccountId()
    }
}