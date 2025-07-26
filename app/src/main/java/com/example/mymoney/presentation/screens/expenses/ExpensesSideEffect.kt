package com.example.mymoney.presentation.screens.expenses

import com.example.core.ui.contract.BaseSideEffect


sealed class ExpensesSideEffect: BaseSideEffect {
    data object NavigateToHistory : ExpensesSideEffect()
    data object NavigateToAddExpense : ExpensesSideEffect()
    data class NavigateToTransactionDetail(val transactionId: Int): ExpensesSideEffect()
    data class ShowError(val message: Int) : ExpensesSideEffect()
}
