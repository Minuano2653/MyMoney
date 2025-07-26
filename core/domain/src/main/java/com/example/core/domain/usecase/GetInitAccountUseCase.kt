package com.example.core.domain.usecase

import com.example.core.domain.entity.Account
import com.example.core.domain.repository.AccountsRepository
import javax.inject.Inject

class GetInitAccountUseCase @Inject constructor(
    private val repository: AccountsRepository
) {
    suspend operator fun invoke(): Result<Account> {
        return repository.getInitAccountInfo()
    }
}