package com.example.mymoney.data.repository

import com.example.mymoney.data.local.datasource.TransactionDao
import com.example.mymoney.data.remote.datasource.TransactionsRemoteDataSource
import com.example.mymoney.data.remote.dto.TransactionRequest
import com.example.mymoney.data.repository.base.BaseRepository
import com.example.mymoney.data.utils.Resource
import com.example.mymoney.data.utils.networkBoundResource
import com.example.mymoney.domain.entity.SavedTransaction
import com.example.mymoney.domain.entity.Transaction
import com.example.mymoney.domain.repository.TransactionsRepository
import com.example.mymoney.presentation.screens.analysis.model.CategoryAnalysis
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject


class TransactionsRepositoryImpl @Inject constructor(
    private val localDataSource: TransactionDao,
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

    override suspend fun createTransaction(
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String?
    ): Result<SavedTransaction> {
        return callWithRetry {
            val request = TransactionRequest(
                accountId = accountId,
                categoryId = categoryId,
                amount = amount,
                transactionDate = transactionDate,
                comment = comment
            )
            remoteDataSource.createTransaction(request).toDomain()
        }
    }

    override suspend fun updateTransaction(
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String?,
        transactionId: Int
    ): Result<SavedTransaction> {
        return callWithRetry {
            val request = TransactionRequest(
                accountId = accountId,
                categoryId = categoryId,
                amount = amount,
                transactionDate = transactionDate,
                comment = comment
            )
            remoteDataSource.updateTransaction(transactionId, request).toDomain()
        }
    }

    override suspend fun getTransaction(transactionId: Int): Result<Transaction> {
        return callWithRetry {
            remoteDataSource.getTransaction(transactionId).toDomain()
        }
    }

    override suspend fun deleteTransaction(transactionId: Int): Result<Unit> {
        return callWithRetry {
            val response = remoteDataSource.deleteTransaction(transactionId)
            if (response.isSuccessful) {
                Unit
            } else {
                throw HttpException(response)
            }
        }
    }

    override fun observeTransactionsByTypeAndPeriod(
        accountId: Int,
        startDate: String,
        endDate: String,
        isIncome: Boolean
    ): Flow<Resource<List<Transaction>>> {
        return networkBoundResource(
            query = {
                localDataSource
                    .observeTransactionsByPeriod(isIncome, startDate, endDate)
                    .map { list -> list.map { it.toDomain() } }
            },
            fetch = {
                remoteDataSource
                    .getTransactionsByPeriod(accountId, startDate, endDate)
            },
            saveFetchResult = { remote ->
                localDataSource.upsertAll(remote.map { it.toLocal() })
            },
            shouldFetch = { true },
            onFetchFailed = { e -> e }
        )
    }

    override fun observeCategoryAnalysis(
        accountId: Int,
        isIncome: Boolean,
        startDate: String,
        endDate: String
    ): Flow<Resource<Pair<BigDecimal, List<CategoryAnalysis>>>> {
        return networkBoundResource(
            query = {
                combine(
                    localDataSource.observeTotalByPeriod(isIncome, startDate, endDate),
                    localDataSource.observeCategoryAnalysis(isIncome, startDate, endDate)
                ) { total, analysis ->
                    val categoryAnalysisList = analysis.map { localAnalysis ->
                        val percentage = if (total > BigDecimal.ZERO) {
                            (localAnalysis.totalAmount * BigDecimal(100))
                                .divide(total, 1, RoundingMode.HALF_UP)
                        } else {
                            BigDecimal.ZERO
                        }

                        CategoryAnalysis(
                            categoryId = localAnalysis.categoryId,
                            categoryName = localAnalysis.categoryName,
                            categoryEmoji = localAnalysis.categoryEmoji,
                            isIncome = localAnalysis.isIncome,
                            percentage = "${percentage}%",
                            totalAmount = localAnalysis.totalAmount.toString()
                        )
                    }
                    Pair(total, categoryAnalysisList)
                }
            },
            fetch = {
                remoteDataSource.getTransactionsByPeriod(
                    accountId = accountId,
                    startDate = startDate,
                    endDate = endDate
                )
            },
            saveFetchResult = { remote ->
                localDataSource.upsertAll(remote.map { it.toLocal() })
            },
            shouldFetch = { true },
            onFetchFailed = { e -> e }
        )
    }
}
