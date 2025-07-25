package com.example.mymoney.presentation.screens.history

import com.example.core.domain.entity.Transaction
import com.example.core.ui.contract.BaseUiState
import com.example.core.common.utils.DateUtils
import java.math.BigDecimal

data class HistoryUiState(
    val isLoading: Boolean = false,
    val transactions: List<Transaction> = emptyList(),
    val total: BigDecimal = BigDecimal(0),
    val currency: String = "",
    val startDate: String = DateUtils.getFirstDayOfCurrentMonth(),
    val endDate: String = DateUtils.getTodayYearMonthDayFormatted(),
    val isNetworkAvailable: Boolean = true
): BaseUiState