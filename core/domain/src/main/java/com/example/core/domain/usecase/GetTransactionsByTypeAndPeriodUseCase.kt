package com.example.core.domain.usecase

import com.example.core.domain.repository.TransactionsRepository
import com.example.core.domain.entity.Transaction
import javax.inject.Inject
import kotlin.collections.filter

class GetTransactionsByTypeAndPeriodUseCase @Inject constructor(
    private val repository: TransactionsRepository,
    private val getAccountIdUseCase: GetAccountIdUseCase
) {
    suspend operator fun invoke(
        isIncome: Boolean,
        startDate: String,
        endDate: String
    ): Result<List<Transaction>> {
        return runCatching {
            val accountId = getAccountIdUseCase().getOrThrow()
            val transactions =
                repository.getTransactionsByPeriod(accountId, startDate, endDate).getOrThrow()
            transactions.filter { it.category.isIncome == isIncome }
        }
    }
}