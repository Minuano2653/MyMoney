package com.example.mymoney.presentation.screens.incomes

import com.example.mymoney.presentation.base.contract.BaseEvent
import com.example.mymoney.presentation.screens.expenses.ExpensesEvent

/**
 * События, которые могут происходить на экране доходов.
 */
sealed class IncomesEvent: BaseEvent {
    object LoadIncomes : IncomesEvent()
    object OnHistoryClicked : IncomesEvent()
    object OnAddClicked: IncomesEvent()
    object OnTransactionClicked: IncomesEvent()
}