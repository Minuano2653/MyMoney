package com.example.mymoney.data.repository

import com.example.mymoney.data.remote.datasource.TransactionsRemoteDataSource
import com.example.mymoney.domain.entity.Transaction
import com.example.mymoney.data.repository.base.BaseRepository
import com.example.mymoney.domain.repository.TransactionsRepository
import javax.inject.Inject

/**
 * Реализация репозитория транзакций, использующая удалённый источник данных.
 *
 * @param remoteDataSource Источник удалённых данных для транзакций.
 */
class TransactionsRepositoryImpl @Inject constructor(
    private val remoteDataSource: TransactionsRemoteDataSource,
): BaseRepository(), TransactionsRepository {

    override suspend fun getTransactionsByPeriod(
        accountId: Int,
        startDate: String,
        endDate: String
    ): Result<List<Transaction>> {
        return callWithRetry {
            remoteDataSource
                .getTransactionsByPeriod(accountId, startDate, endDate)
                .map { it.toDomain() }
        }
    }
}
