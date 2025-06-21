package com.example.mymoney.presentation.screens.categories

sealed class CategoriesSideEffect {
    data class ShowError(val message: String) : CategoriesSideEffect()
    data object NavigateToCategoryDetails : CategoriesSideEffect()
}