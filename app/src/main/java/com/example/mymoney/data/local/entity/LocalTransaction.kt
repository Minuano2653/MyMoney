package com.example.mymoney.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.mymoney.domain.entity.Category
import com.example.mymoney.domain.entity.Transaction
import java.math.BigDecimal

@Entity(
    tableName = "transaction",
    foreignKeys = [
        ForeignKey(
            entity = LocalCategory::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class LocalTransaction(
    @PrimaryKey val id: Int,
    val categoryId: Int,
    val amount: String,
    val transactionDate: String,
    val comment: String?,
    val createdAt: String,
    val updatedAt: String,
    val isSynced: Boolean = true
) {
    fun toDomain(category: Category): Transaction {
        return Transaction(
            id = id,
            category = category,
            comment = comment,
            amount = BigDecimal(amount),
            transactionDate = transactionDate,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}