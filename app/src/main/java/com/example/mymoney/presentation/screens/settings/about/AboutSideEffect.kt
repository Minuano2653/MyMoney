package com.example.mymoney.presentation.screens.settings.about

import com.example.core.ui.contract.BaseSideEffect

sealed class AboutSideEffect: BaseSideEffect {
    data class ShowError(val message: Int) : AboutSideEffect()
}