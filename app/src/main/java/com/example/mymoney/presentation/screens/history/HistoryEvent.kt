package com.example.mymoney.presentation.screens.history

import com.example.mymoney.domain.entity.Transaction
import com.example.mymoney.presentation.base.contract.BaseEvent
import com.example.mymoney.presentation.screens.expenses.ExpensesEvent

/**
 * События, которые может обрабатывать ViewModel экрана истории транзакций.
 *
 * - LoadTransactions: загрузить транзакции за выбранный период.
 * - OnStartDateClicked: пользователь нажал на выбор даты начала периода.
 * - OnEndDateClicked: пользователь нажал на выбор даты конца периода.
 * - OnStartDateSelected: пользователь выбрал дату начала периода.
 * - OnEndDateSelected: пользователь выбрал дату конца периода.
 * - OnBackPressed: пользователь нажал кнопку назад.
 * - OnAnalysisClicked: пользователь нажал кнопку перехода к анализу данных.
 */
sealed class HistoryEvent: BaseEvent {
    data class LoadTransactions(val isIncome: Boolean) : HistoryEvent()
    object OnStartDateClicked : HistoryEvent()
    object OnEndDateClicked : HistoryEvent()
    data class OnStartDateSelected(val isIncome: Boolean, val date: String) : HistoryEvent()
    data class OnEndDateSelected(val isIncome: Boolean, val date: String) : HistoryEvent()
    object OnBackPressed : HistoryEvent()
    object OnAnalysisClicked : HistoryEvent()
    data class OnTransactionClicked(val transaction: Transaction): HistoryEvent()
}
