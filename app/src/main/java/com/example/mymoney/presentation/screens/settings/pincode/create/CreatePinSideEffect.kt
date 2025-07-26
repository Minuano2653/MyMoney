package com.example.mymoney.presentation.screens.settings.pincode.create

import com.example.core.ui.contract.BaseSideEffect

sealed class CreatePinSideEffect : BaseSideEffect {
    object NavigateToMain : CreatePinSideEffect()
    object NavigateBack : CreatePinSideEffect()
    data class ShowError(val message: String) : CreatePinSideEffect()
}