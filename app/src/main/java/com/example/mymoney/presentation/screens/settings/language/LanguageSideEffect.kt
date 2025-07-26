package com.example.mymoney.presentation.screens.settings.language

import com.example.core.ui.contract.BaseSideEffect

sealed class LanguageSideEffect : BaseSideEffect {
    data class ShowError(val message: Int) : LanguageSideEffect()
}