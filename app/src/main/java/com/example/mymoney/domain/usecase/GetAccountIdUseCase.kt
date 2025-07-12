package com.example.mymoney.domain.usecase

import com.example.mymoney.domain.repository.AccountsRepository
import javax.inject.Inject

class GetAccountIdUseCase @Inject constructor(
    private val repository: AccountsRepository
) {
    suspend operator fun invoke(): Result<Int> {
        return repository.getAccountId()
    }
}