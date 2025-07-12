package com.example.mymoney.data.remote.dto

import com.example.mymoney.domain.entity.SavedTransaction
import java.math.BigDecimal

data class TransactionResponse(
    val id: Int,
    val accountId: Int,
    val categoryId: Int,
    val amount: String,
    val transactionDate: String,
    val comment: String?,
    val createdAt: String,
    val updatedAt: String
) {
    fun toDomain() = SavedTransaction(
        id = id,
        accountId = accountId,
        categoryId = categoryId,
        amount = BigDecimal(amount),
        transactionDate = transactionDate,
        comment = comment,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}