package com.example.mymoney.presentation.screens.settings.pincode.enter

import com.example.core.ui.contract.BaseSideEffect

sealed class EnterPinSideEffect : BaseSideEffect {
    object PinVerified : EnterPinSideEffect()
    data class ShowError(val message: String) : EnterPinSideEffect()
}