package com.example.mymoney.presentation.screens.history

import com.example.mymoney.presentation.base.contract.BaseSideEffect

/**
 * Побочные эффекты экрана истории транзакций.
 *
 * Используются для однократных событий UI, таких как:
 * - Показ ошибки.
 * - Навигация назад.
 * - Переход к экрану анализа.
 * - Показ модальных диалогов выбора даты (начала и конца периода).
 */
sealed class HistorySideEffect: BaseSideEffect {
    data class ShowError(val message: String) : HistorySideEffect()
    object NavigateBack : HistorySideEffect()
    object NavigateToAnalysis : HistorySideEffect()
    object ShowStartDatePicker : HistorySideEffect()
    object ShowEndDatePicker : HistorySideEffect()
}
