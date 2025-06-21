package com.example.mymoney.domain.entity

import java.math.BigDecimal

data class Transaction(
    val id: Int,
    val category: Category,
    val comment: String? = null,
    val amount: BigDecimal,
    val transactionDate: String,
    val createdAt: String,
    val updatedAt: String,
)