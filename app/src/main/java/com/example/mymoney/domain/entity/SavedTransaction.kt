package com.example.mymoney.domain.entity

import java.math.BigDecimal

data class SavedTransaction(
    val id: Int,
    val accountId: Int,
    val categoryId: Int,
    val amount: BigDecimal,
    val transactionDate: String,
    val comment: String?,
    val createdAt: String,
    val updatedAt: String
)