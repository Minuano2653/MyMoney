package com.example.mymoney.presentation.screens.incomes

import com.example.core.domain.entity.Transaction
import com.example.core.ui.contract.BaseEvent

sealed class IncomesEvent: BaseEvent {
    object LoadIncomes : IncomesEvent()
    object OnHistoryClicked : IncomesEvent()
    object OnAddClicked: IncomesEvent()
    data class OnTransactionClicked(val income: Transaction): IncomesEvent()
}