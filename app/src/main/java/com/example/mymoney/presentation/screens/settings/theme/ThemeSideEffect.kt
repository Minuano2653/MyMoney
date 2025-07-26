package com.example.mymoney.presentation.screens.settings.theme

import com.example.core.ui.contract.BaseSideEffect

sealed class ThemeSideEffect : BaseSideEffect {
    data class ShowError(val message: Int) : ThemeSideEffect()
}