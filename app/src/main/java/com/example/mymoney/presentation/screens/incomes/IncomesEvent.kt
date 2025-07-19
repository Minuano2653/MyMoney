package com.example.mymoney.presentation.screens.incomes

import com.example.mymoney.domain.entity.Transaction
import com.example.mymoney.presentation.base.contract.BaseEvent

sealed class IncomesEvent: BaseEvent {
    object LoadIncomes : IncomesEvent()
    object OnHistoryClicked : IncomesEvent()
    object OnAddClicked: IncomesEvent()
    data class OnTransactionClicked(val income: Transaction): IncomesEvent()
}