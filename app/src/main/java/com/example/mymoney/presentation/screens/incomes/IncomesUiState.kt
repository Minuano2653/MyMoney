package com.example.mymoney.presentation.screens.incomes

import com.example.mymoney.domain.entity.Income
import java.math.BigDecimal

data class IncomesUiState(
    val incomes: List<Income> = emptyList(),
) {
    val total: BigDecimal
        get() = incomes.sumOf { it.amount }
}