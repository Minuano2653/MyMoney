package com.example.mymoney.domain.usecase

import com.example.mymoney.domain.entity.Account
import com.example.mymoney.domain.repository.AccountsRepository
import com.example.mymoney.utils.RetryUtils.retryWithDelay
import retrofit2.HttpException
import javax.inject.Inject

class GetAccountUseCase @Inject constructor(
    private val repository: AccountsRepository
) {
    suspend operator fun invoke(
        accountId: Int = 38,
    ): Result<Account> {

        return runCatching {
            val account = retryWithDelay(
                times = 3,
                delayTimeMillis = 2000,
                retryCondition = { it is HttpException && it.code() == 500 }
            ) {
                repository.getAccountById(accountId).getOrThrow()
            }
            account
        }
    }
}