package com.example.mymoney.presentation.screens.analysis.model

data class CategoryAnalysis(
    val categoryId: Int,
    val categoryName: String,
    val categoryEmoji: String,
    val isIncome: Boolean,
    val percentage: String,
    val totalAmount: String
)