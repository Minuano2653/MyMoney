package com.example.mymoney.presentation.screens.transactions_history

import com.example.mymoney.domain.entity.Transaction
import com.example.mymoney.utils.DateUtils
import java.math.BigDecimal

data class HistoryUiState(
    val isLoading: Boolean = false,
    val transactions: List<Transaction> = emptyList(),
    val startDate: String = DateUtils.getFirstDayOfCurrentMonth(),
    val endDate: String = DateUtils.getTodayFormatted(),
    val error: String? = null,
    val isNetworkAvailable: Boolean = true
) {
    val total: BigDecimal
        get() = transactions.sumOf { it.amount }
}