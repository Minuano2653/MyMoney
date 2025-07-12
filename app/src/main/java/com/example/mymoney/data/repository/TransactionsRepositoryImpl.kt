package com.example.mymoney.data.repository

import com.example.mymoney.data.remote.datasource.TransactionsRemoteDataSource
import com.example.mymoney.data.remote.dto.TransactionRequest
import com.example.mymoney.domain.entity.Transaction
import com.example.mymoney.data.repository.base.BaseRepository
import com.example.mymoney.domain.entity.SavedTransaction
import com.example.mymoney.domain.repository.TransactionsRepository
import retrofit2.HttpException
import javax.inject.Inject


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
}
