package com.example.mymoney.presentation.screens.expenses

import com.example.mymoney.domain.entity.Expense
import java.math.BigDecimal

data class ExpensesUiState(
    val expenses: List<Expense> = emptyList(),
) {
    val total: BigDecimal
        get() = expenses.sumOf { it.amount }
}