package com.example.mymoney.presentation.screens.categories

import com.example.core.ui.contract.BaseSideEffect

/**
 * Сайд-эффекты, связанные с экраном категорий.
 * Используются для навигации и отображения одноразовых событий, таких как ошибки.
 */
sealed class CategoriesSideEffect: BaseSideEffect {
    data class ShowError(val message: Int) : CategoriesSideEffect()
}
