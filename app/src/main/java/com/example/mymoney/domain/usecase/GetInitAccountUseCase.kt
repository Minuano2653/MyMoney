package com.example.mymoney.domain.usecase

import com.example.mymoney.domain.entity.Account
import com.example.mymoney.domain.repository.AccountsRepository
import javax.inject.Inject

class GetInitAccountUseCase @Inject constructor(
    private val repository: AccountsRepository
) {
    suspend operator fun invoke(): Result<Account> {
        return repository.getInitAccountInfo()
    }
}