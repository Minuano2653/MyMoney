package com.example.mymoney.presentation.screens.incomes

import com.example.core.ui.contract.BaseSideEffect

sealed class IncomesSideEffect: BaseSideEffect {
    data object NavigateToHistory : IncomesSideEffect()
    data object NavigateToAddIncome : IncomesSideEffect()
    data class NavigateToTransactionDetail(val transactionId: Int): IncomesSideEffect()
    data class ShowError(val message: Int) : IncomesSideEffect()
}
