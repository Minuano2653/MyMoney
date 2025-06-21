package com.example.mymoney.data.remote.dto

import com.example.mymoney.domain.entity.Transaction
import com.example.mymoney.utils.DateUtils
import java.math.BigDecimal

data class TransactionDto(
    val id: Int,
    val account: AccountDto,
    val category: CategoryDto,
    val amount: String,
    val transactionDate: String,
    val comment: String?,
    val createdAt: String,
    val updatedAt: String
) {
    fun toDomain(): Transaction {
        return Transaction(
            id = id,
            category = category.toDomain(),
            comment = comment,
            amount = BigDecimal(amount),
            createdAt = createdAt,
            transactionDate = transactionDate,
            updatedAt = updatedAt
        )
    }
}