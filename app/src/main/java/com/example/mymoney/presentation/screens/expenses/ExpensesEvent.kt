package com.example.mymoney.presentation.screens.expenses

import com.example.core.domain.entity.Transaction
import com.example.core.ui.contract.BaseEvent

/**
 * События, связанные с экраном расходов.
 * Используются для обработки действий пользователя во ViewModel.
 */
sealed class ExpensesEvent: BaseEvent {
    object LoadExpenses: ExpensesEvent()
    object OnHistoryClicked: ExpensesEvent()
    object OnAddClicked: ExpensesEvent()
    data class OnTransactionClicked(val expense: Transaction): ExpensesEvent()
}
