package com.example.mymoney.presentation.screens.history

import com.example.mymoney.presentation.base.contract.BaseSideEffect

sealed class HistorySideEffect: BaseSideEffect {
    data class ShowError(val message: String) : HistorySideEffect()
    object NavigateBack : HistorySideEffect()
    data class NavigateToAnalysis(val isIncome: Boolean) : HistorySideEffect()
    data class NavigateToEditTransaction(val transactionId: Int) : HistorySideEffect()
}
