package com.example.mymoney.domain.repository

import com.example.mymoney.data.utils.Resource
import com.example.mymoney.domain.entity.SavedTransaction
import com.example.mymoney.domain.entity.Transaction
import com.example.mymoney.presentation.screens.analysis.model.CategoryAnalysis
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

    fun observeCategoryAnalysis(
        accountId: Int,
        isIncome: Boolean,
        startDate: String,
        endDate: String
    ): Flow<Resource<Pair<BigDecimal, List<CategoryAnalysis>>>>
}
