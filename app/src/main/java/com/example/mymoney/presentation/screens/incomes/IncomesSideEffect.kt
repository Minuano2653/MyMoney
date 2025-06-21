package com.example.mymoney.presentation.screens.incomes

sealed class IncomesSideEffect {
    data object NavigateToHistory : IncomesSideEffect()
    data object NavigateToAddExpense : IncomesSideEffect()
    data class ShowError(val message: String) : IncomesSideEffect()
}