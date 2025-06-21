package com.example.mymoney.presentation.screens.expenses

sealed class ExpensesSideEffect {
    data object NavigateToHistory : ExpensesSideEffect()
    data object NavigateToAddExpense : ExpensesSideEffect()
    data class ShowError(val message: String) : ExpensesSideEffect()
}