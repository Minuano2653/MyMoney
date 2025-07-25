package com.example.core.domain.repository

import com.example.core.domain.entity.CategoryAnalysis
import com.example.core.domain.entity.Resource
import com.example.core.domain.entity.SavedTransaction
import com.example.core.domain.entity.Transaction
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

/**
 * Репозиторий для работы с транзакциями.
 */
interface TransactionsRepository {
    suspend fun getTransactionsByPeriod(
        accountId: Int,
        startDate: String,
        endDate: String
    ): Result<List<Transaction>>

    suspend fun createTransaction(
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String?
    ): Result<SavedTransaction>

    suspend fun updateTransaction(
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String?,
        transactionId: Int
    ): Result<SavedTransaction>

    suspend fun getTransaction(
        transactionId: Int
    ): Result<Transaction>

    suspend fun deleteTransaction(transactionId: Int): Result<Unit>

    fun observeTransactionsByTypeAndPeriod(
        accountId: Int,
        startDate: String,
        endDate: String,
        isIncome: Boolean
    ): Flow<Resource<List<Transaction>>>

    fun observeTransactionsByPeriod(
        accountId: Int,
        startDate: String,
        endDate: String,
    ): Flow<Resource<List<Transaction>>>

    fun observeCategoryAnalysis(
        accountId: Int,
        isIncome: Boolean,
        startDate: String,
        endDate: String
    ): Flow<Resource<Pair<BigDecimal, List<CategoryAnalysis>>>>

    fun observeTransaction(transactionId: Int): Flow<Resource<Transaction?>>

}
