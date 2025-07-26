package com.example.mymoney.data.local.datasource

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.core.domain.entity.Category
import com.example.core.domain.entity.Transaction
import com.example.mymoney.data.local.entity.LocalTransaction
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

@Dao
interface TransactionDao {

    @Query("""
    SELECT t.*, c.name as categoryName, c.emoji as categoryEmoji, c.isIncome as categoryIsIncome 
    FROM `transaction` as t 
    INNER JOIN category as c ON t.categoryId = c.id 
    WHERE c.isIncome = :isIncome 
    AND DATE(t.transactionDate) BETWEEN DATE(:startDate) AND DATE(:endDate)
    ORDER BY t.transactionDate DESC, t.createdAt DESC
""")
    fun observeTransactionsByTypeAndPeriod(
        isIncome: Boolean,
        startDate: String,
        endDate: String
    ): Flow<List<TransactionWithCategory>>

    @Query("""
    SELECT t.*, c.name as categoryName, c.emoji as categoryEmoji, c.isIncome as categoryIsIncome 
    FROM `transaction` as t 
    INNER JOIN category as c ON t.categoryId = c.id 
    AND DATE(t.transactionDate) BETWEEN DATE(:startDate) AND DATE(:endDate)
    ORDER BY t.transactionDate DESC, t.createdAt DESC
""")
    fun observeTransactionsByPeriod(
        startDate: String,
        endDate: String
    ): Flow<List<TransactionWithCategory>>

    @Query("""
        SELECT t.*, c.name as categoryName, c.emoji as categoryEmoji, c.isIncome as categoryIsIncome 
        FROM `transaction` as t 
        INNER JOIN category as c ON t.categoryId = c.id 
        WHERE t.localId = :localId
    """)
    fun observeTransactionById(localId: Int): Flow<TransactionWithCategory?>


    @Query("""
        SELECT 
            c.id as categoryId,
            c.name as categoryName,
            c.emoji as categoryEmoji,
            c.isIncome as isIncome,
            SUM(t.amount) as totalAmount,
            COUNT(t.localId) as transactionCount
        FROM `transaction` t
        INNER JOIN category c ON t.categoryId = c.id
        WHERE isIncome = :isIncome
        AND DATE(t.transactionDate) BETWEEN DATE(:startDate) AND DATE(:endDate)
        GROUP BY c.id, c.name, c.emoji, c.isIncome
        ORDER BY totalAmount DESC
    """)
    fun observeCategoryAnalysis(
        isIncome: Boolean,
        startDate: String,
        endDate: String
    ): Flow<List<LocalCategoryAnalysis>>

    @Query("""
        SELECT COALESCE(SUM(amount), 0) as total
        FROM `transaction` t
        INNER JOIN category c ON t.categoryId = c.id
        WHERE c.isIncome = :isIncome 
        AND DATE(t.transactionDate) BETWEEN DATE(:startDate) AND DATE(:endDate)
    """)
    fun observeTotalByPeriod(
        isIncome: Boolean,
        startDate: String,
        endDate: String
    ): Flow<BigDecimal>

    @Query("""
        SELECT * FROM `transaction` 
        WHERE isSynced = 0
    """)
    suspend fun getUnsyncedTransactions(): List<LocalTransaction>

    @Upsert
    suspend fun upsert(task: LocalTransaction)

    @Upsert
    suspend fun upsertAll(tasks: List<LocalTransaction>)

}

data class TransactionWithCategory(
    val localId: Int,
    val serverId: Int?,
    val categoryId: Int,
    val amount: String,
    val transactionDate: String,
    val comment: String?,
    val createdAt: String,
    val updatedAt: String,
    val categoryName: String,
    val categoryEmoji: String,
    val categoryIsIncome: Boolean
) {
    fun toDomain() = Transaction(
        id = localId,
        category = Category(
            id = categoryId,
            name = categoryName,
            emoji = categoryEmoji,
            isIncome = categoryIsIncome
        ),
        amount = BigDecimal(amount),
        transactionDate = transactionDate,
        comment = comment,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

data class LocalCategoryAnalysis(
    val categoryId: Int,
    val categoryName: String,
    val categoryEmoji: String,
    val isIncome: Boolean,
    val totalAmount: BigDecimal,
    val transactionCount: Int
)