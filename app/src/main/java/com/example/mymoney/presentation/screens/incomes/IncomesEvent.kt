package com.example.mymoney.presentation.screens.incomes

import com.example.mymoney.domain.entity.Transaction
import com.example.mymoney.presentation.base.contract.BaseEvent
import com.example.mymoney.presentation.screens.expenses.ExpensesEvent

/**
 * События, которые могут происходить на экране доходов.
 */
sealed class IncomesEvent: BaseEvent {
    object LoadIncomes : IncomesEvent()
    object OnHistoryClicked : IncomesEvent()
    object OnAddClicked: IncomesEvent()
    data class OnTransactionClicked(val income: Transaction): IncomesEvent()
}