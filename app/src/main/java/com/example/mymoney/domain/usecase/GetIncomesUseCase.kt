package com.example.mymoney.domain.usecase

import com.example.mymoney.domain.entity.Transaction
import com.example.mymoney.domain.repository.TransactionsRepository
import com.example.mymoney.utils.DateUtils
import com.example.mymoney.utils.RetryUtils.retryWithDelay
import retrofit2.HttpException
import javax.inject.Inject

class GetIncomesUseCase @Inject constructor(
    private val repository: TransactionsRepository
) {
    suspend operator fun invoke(
        accountId: Int = 38,
        startDate: String = DateUtils.getTodayFormatted(),
        endDate: String = DateUtils.getTodayFormatted()
    ): Result<List<Transaction>> {

        return runCatching {
            val transactions = retryWithDelay(
                times = 3,
                delayTimeMillis = 2000,
                retryCondition = { it is HttpException && it.code() == 500 }
            ) {
                repository.getTransactionsByPeriod(accountId, startDate, endDate).getOrThrow()
            }
            transactions.filter { it.category.isIncome }
        }
    }
}