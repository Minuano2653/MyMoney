package com.example.mymoney.presentation.screens.incomes

import com.example.mymoney.presentation.base.contract.BaseEvent

/**
 * События, которые могут происходить на экране доходов.
 */
sealed class IncomesEvent: BaseEvent {
    object LoadIncomes : IncomesEvent()
    object OnHistoryClicked : IncomesEvent()
    object OnAddClicked : IncomesEvent()
}