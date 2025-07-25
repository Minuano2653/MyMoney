package com.example.core.domain.entity

import java.math.BigDecimal

data class CategoryAnalysis(
    val categoryId: Int,
    val categoryName: String,
    val categoryEmoji: String,
    val isIncome: Boolean,
    val percentage: BigDecimal,
    val totalAmount: BigDecimal
)