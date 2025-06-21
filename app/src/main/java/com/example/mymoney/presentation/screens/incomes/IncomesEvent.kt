package com.example.mymoney.presentation.screens.incomes

sealed class IncomesEvent {
    object LoadIncomes : IncomesEvent()
    object OnHistoryClicked : IncomesEvent()
    object OnAddClicked : IncomesEvent()
}