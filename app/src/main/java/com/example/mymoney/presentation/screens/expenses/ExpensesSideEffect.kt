package com.example.mymoney.presentation.screens.expenses

import com.example.mymoney.presentation.base.contract.BaseSideEffect

/**
 * Побочные эффекты экрана расходов.
 * Используются для однократных действий, таких как навигация и показ ошибок.
 */
sealed class ExpensesSideEffect: BaseSideEffect {
    data object NavigateToHistory : ExpensesSideEffect()
    data object NavigateToAddExpense : ExpensesSideEffect()
    data class ShowError(val message: String) : ExpensesSideEffect()
}
