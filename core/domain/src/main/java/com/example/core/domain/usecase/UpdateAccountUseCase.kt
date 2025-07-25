package com.example.core.domain.usecase

import com.example.core.domain.entity.Account
import com.example.core.domain.repository.AccountsRepository
import javax.inject.Inject

class UpdateAccountUseCase @Inject constructor(
    private val accountsRepository: AccountsRepository
) {
    suspend operator fun invoke(accountId: Int, name: String, balance: String, currency: String): Result<Account> {
        return accountsRepository.updateAccount(accountId, name, balance, currency)
    }
}