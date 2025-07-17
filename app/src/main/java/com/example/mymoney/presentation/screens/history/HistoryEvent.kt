package com.example.mymoney.presentation.screens.history

import com.example.mymoney.domain.entity.Transaction
import com.example.mymoney.presentation.base.contract.BaseEvent

sealed class HistoryEvent: BaseEvent {
    object LoadTransactions : HistoryEvent()
    object OnStartDateClicked : HistoryEvent()
    object OnEndDateClicked : HistoryEvent()
    data class OnStartDateSelected(val date: String) : HistoryEvent()
    data class OnEndDateSelected(val date: String) : HistoryEvent()
    object OnBackPressed : HistoryEvent()
    object OnAnalysisClicked : HistoryEvent()
    data class OnTransactionClicked(val transaction: Transaction): HistoryEvent()
}
