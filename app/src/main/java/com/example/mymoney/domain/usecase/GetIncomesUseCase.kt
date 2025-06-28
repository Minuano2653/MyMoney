package com.example.mymoney.domain.usecase

import com.example.mymoney.domain.entity.Transaction
import com.example.mymoney.domain.repository.TransactionsRepository
import com.example.mymoney.utils.DateUtils
import retrofit2.HttpException
import javax.inject.Inject

/**
 * Юзкейс для получения списка доходов счёта за указанный период.
 *
 * @param repository Репозиторий транзакций.
 */
class GetIncomesUseCase @Inject constructor(
    private val repository: TransactionsRepository
) {
    suspend operator fun invoke(
        accountId: Int = 38,
        startDate: String = DateUtils.getTodayFormatted(),
        endDate: String = DateUtils.getTodayFormatted()
    ): Result<List<Transaction>> {
        return repository
            .getTransactionsByPeriod(accountId, startDate, endDate)
            .map { it.filter { it.category.isIncome } }
    }
}
