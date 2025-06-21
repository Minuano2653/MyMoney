package com.example.mymoney.presentation.screens.incomes

import com.example.mymoney.domain.entity.Transaction
import java.math.BigDecimal

data class IncomesUiState(
    val isLoading: Boolean = false,
    val incomes: List<Transaction> = emptyList(),
    val error: String? = null,
    val isNetworkAvailable: Boolean = true
) {
    val total: BigDecimal
        get() = incomes.sumOf { it.amount }
}