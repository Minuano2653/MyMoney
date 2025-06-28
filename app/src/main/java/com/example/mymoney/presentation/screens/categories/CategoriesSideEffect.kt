package com.example.mymoney.presentation.screens.categories

import com.example.mymoney.presentation.base.contract.BaseSideEffect

/**
 * Сайд-эффекты, связанные с экраном категорий.
 * Используются для навигации и отображения одноразовых событий, таких как ошибки.
 */
sealed class CategoriesSideEffect: BaseSideEffect {
    data class ShowError(val message: String) : CategoriesSideEffect()
    data object NavigateToCategoryDetails : CategoriesSideEffect()
}
