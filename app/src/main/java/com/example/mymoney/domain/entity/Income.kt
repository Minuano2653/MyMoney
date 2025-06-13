package com.example.mymoney.domain.entity

import java.math.BigDecimal

data class Income(
    val id: Int,
    val category: Category,
    val comment: String? = null,
    val amount: BigDecimal,
    val createdAt: String,
    val updatedAt: String,
)