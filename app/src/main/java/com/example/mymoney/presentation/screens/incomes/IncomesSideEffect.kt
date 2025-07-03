package com.example.mymoney.presentation.screens.incomes

import com.example.mymoney.presentation.base.contract.BaseSideEffect
import com.example.mymoney.presentation.screens.expenses.ExpensesSideEffect

/**
 * Побочные эффекты экрана доходов.
 *
 * Используются для одноразовых событий, таких как навигация и отображение ошибок.
 */
sealed class IncomesSideEffect: BaseSideEffect {
    data object NavigateToHistory : IncomesSideEffect()
    data object NavigateToAddIncome : IncomesSideEffect()
    data object NavigateToTransactionDetail: IncomesSideEffect()
    data class ShowError(val message: String) : IncomesSideEffect()
}
