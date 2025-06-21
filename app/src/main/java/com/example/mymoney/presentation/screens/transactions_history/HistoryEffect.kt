package com.example.mymoney.presentation.screens.transactions_history

sealed class HistorySideEffect {
    data class ShowError(val message: String) : HistorySideEffect()
    object NavigateBack : HistorySideEffect()
    object NavigateToAnalysis : HistorySideEffect()
    object ShowStartDatePicker : HistorySideEffect()
    object ShowEndDatePicker : HistorySideEffect()
}
