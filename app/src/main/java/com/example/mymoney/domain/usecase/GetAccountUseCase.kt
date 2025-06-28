package com.example.mymoney.domain.usecase

import com.example.mymoney.domain.entity.Account
import com.example.mymoney.domain.repository.AccountsRepository
import retrofit2.HttpException
import javax.inject.Inject

/**
 * Юзкейс для получения аккаунта по идентификатору.
 *
 * @param repository Репозиторий аккаунтов.
 */
class GetAccountUseCase @Inject constructor(
    private val repository: AccountsRepository
) {
    suspend operator fun invoke(
        accountId: Int = 38,
    ): Result<Account> {
        return repository.getAccountById(accountId)
    }
}
