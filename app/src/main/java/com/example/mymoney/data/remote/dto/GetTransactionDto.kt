package com.example.mymoney.data.remote.dto

import com.example.mymoney.data.local.entity.LocalTransaction
import com.example.mymoney.domain.entity.Transaction
import java.math.BigDecimal

data class GetTransactionDto(
    val id: Int,
    val account: AccountBrief,
    val category: CategoryDto,
    val amount: String,
    val transactionDate: String,
    val comment: String?,
    val createdAt: String,
    val updatedAt: String
) {
    fun toDomain() = Transaction(
        id = id,
        category = category.toDomain(),
        comment = comment,
        amount = BigDecimal(amount),
        transactionDate = transactionDate,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    fun toLocal() = LocalTransaction(
        serverId = id,
        comment = comment,
        amount = amount,
        transactionDate = transactionDate,
        createdAt = createdAt,
        updatedAt = updatedAt,
        categoryId = category.id,
        isSynced = true
    )
}