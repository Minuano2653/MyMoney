package com.example.mymoney.presentation.screens.transactions_history

sealed class HistoryEvent {
    object LoadTransactions : HistoryEvent()
    object OnStartDateClicked : HistoryEvent()
    object OnEndDateClicked : HistoryEvent()
    data class OnStartDateSelected(val date: String) : HistoryEvent()
    data class OnEndDateSelected(val date: String) : HistoryEvent()
    object OnBackPressed : HistoryEvent()
    object OnAnalysisClicked : HistoryEvent()
}