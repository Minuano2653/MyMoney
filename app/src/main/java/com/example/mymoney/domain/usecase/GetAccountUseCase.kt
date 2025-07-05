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
    private val repository: AccountsRepository,
    private val getAccountIdUseCase: GetAccountIdUseCase
) {
    suspend operator fun invoke(): Result<Account> {
        return getAccountIdUseCase()
            .fold(
                onSuccess = { accountId ->
                    repository.getAccountById(accountId)
                },
                onFailure = { exception ->
                    Result.failure(exception)
                }
            )
    }
}
