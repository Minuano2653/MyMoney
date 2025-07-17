package com.example.mymoney.presentation.screens.analysis

import com.example.mymoney.presentation.base.contract.BaseUiState
import com.example.mymoney.presentation.screens.analysis.model.CategoryAnalysis
import com.example.mymoney.utils.DateUtils
import java.math.BigDecimal

data class AnalysisUiState(
    val isLoading: Boolean = false,
    val categoryAnalysis: List<CategoryAnalysis> = emptyList(),
    val total: BigDecimal = BigDecimal(0),
    val currency: String = "",
    val showStartDatePicker: Boolean = false,
    val showEndDatePicker: Boolean = false,
    val startDate: String = DateUtils.getFirstDayOfCurrentMonth(),
    val endDate: String = DateUtils.getTodayYearMonthDayFormatted(),
): BaseUiState
