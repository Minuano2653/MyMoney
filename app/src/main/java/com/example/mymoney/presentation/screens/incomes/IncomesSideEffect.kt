package com.example.mymoney.presentation.screens.incomes

import com.example.mymoney.presentation.base.contract.BaseSideEffect

/**
 * Побочные эффекты экрана доходов.
 *
 * Используются для одноразовых событий, таких как навигация и отображение ошибок.
 */
sealed class IncomesSideEffect: BaseSideEffect {
    data object NavigateToHistory : IncomesSideEffect()
    data object NavigateToAddExpense : IncomesSideEffect()
    data class ShowError(val message: String) : IncomesSideEffect()
}
