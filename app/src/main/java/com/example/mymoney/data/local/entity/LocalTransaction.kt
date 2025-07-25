package com.example.mymoney.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.core.domain.entity.Category
import com.example.core.domain.entity.Transaction
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
    @PrimaryKey(autoGenerate = true) val localId: Int = 0,
    val serverId: Int? = null,
    val categoryId: Int,
    val amount: String,
    val transactionDate: String,
    val comment: String?,
    val createdAt: String,
    val updatedAt: String,
    val isSynced: Boolean = false,
) {
    fun toDomain(category: Category): Transaction {
        return Transaction(
            id = localId,
            category = category,
            comment = comment,
            amount = BigDecimal(amount),
            transactionDate = transactionDate,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}