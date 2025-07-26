package com.example.mymoney.presentation.screens.analysis

import com.example.core.common.utils.DateUtils
import com.example.core.domain.entity.CategoryAnalysis
import com.example.core.ui.contract.BaseUiState
import java.math.BigDecimal

data class AnalysisUiState(
    val isLoading: Boolean = false,
    val categoryAnalysis: List<CategoryAnalysis> = emptyList(),
    val total: BigDecimal = BigDecimal(0),
    val currency: String = "",
    val startDate: String = DateUtils.getFirstDayOfCurrentMonth(),
    val endDate: String = DateUtils.getTodayYearMonthDayFormatted(),
): BaseUiState
