package com.example.mymoney.presentation.screens.expenses

import com.example.mymoney.domain.entity.Transaction
import com.example.mymoney.presentation.base.contract.BaseEvent

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
