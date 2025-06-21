package com.example.mymoney.presentation.screens.expenses

import com.example.mymoney.domain.entity.Transaction
import java.math.BigDecimal

data class ExpensesUiState(
    val isLoading: Boolean = false,
    val expenses: List<Transaction> = emptyList(),
    val error: String? = null,
    val isNetworkAvailable: Boolean = true
) {
    val total: BigDecimal
        get() = this@ExpensesUiState.expenses.sumOf { it.amount }
}