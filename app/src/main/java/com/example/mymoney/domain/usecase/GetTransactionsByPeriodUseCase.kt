package com.example.mymoney.domain.usecase

import com.example.mymoney.domain.entity.Transaction
import com.example.mymoney.domain.repository.TransactionsRepository
import com.example.mymoney.utils.DateUtils
import javax.inject.Inject

class GetTransactionsByPeriodUseCase @Inject constructor(
    private val repository: TransactionsRepository,
    private val getAccountIdUseCase: GetAccountIdUseCase
) {
    suspend operator fun invoke(
        isIncome: Boolean,
        startDate: String = DateUtils.getTodayYearMonthDayFormatted(),
        endDate: String = DateUtils.getTodayYearMonthDayFormatted()
    ): Result<List<Transaction>> {
        return runCatching {
            val accountId = getAccountIdUseCase().getOrThrow()
            val transactions =
                repository.getTransactionsByPeriod(accountId, startDate, endDate).getOrThrow()
            transactions.filter { it.category.isIncome == isIncome }
        }
    }
}