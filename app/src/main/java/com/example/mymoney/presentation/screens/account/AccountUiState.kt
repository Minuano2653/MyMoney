package com.example.mymoney.presentation.screens.account

import com.example.core.ui.charts.bar.ChartDataPoint
import com.example.core.ui.contract.BaseUiState
import java.math.BigDecimal

data class AccountUiState(
    val isLoading: Boolean = false,
    val accountId: Int = -1,
    val name: String = "",
    val balance: BigDecimal = BigDecimal(0),
    val chartItems: List<ChartDataPoint> = emptyList(),
    val currency: String = "",
    val isNetworkAvailable: Boolean = true
): BaseUiState
