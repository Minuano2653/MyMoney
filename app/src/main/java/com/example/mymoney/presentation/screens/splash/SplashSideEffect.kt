package com.example.mymoney.presentation.screens.splash

import com.example.mymoney.presentation.base.contract.BaseSideEffect

sealed class SplashSideEffect: BaseSideEffect {
    object NavigateToMain : SplashSideEffect()
    data class ShowError(val message: String) : SplashSideEffect()
}
