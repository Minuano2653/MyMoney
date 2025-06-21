package com.example.mymoney.presentation.screens.expenses

sealed class ExpensesEvent {
    object LoadExpenses : ExpensesEvent()
    object OnHistoryClicked : ExpensesEvent()
    object OnAddClicked : ExpensesEvent()
}