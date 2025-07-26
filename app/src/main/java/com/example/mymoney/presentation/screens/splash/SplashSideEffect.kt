package com.example.mymoney.presentation.screens.splash

import com.example.core.ui.contract.BaseSideEffect

sealed class SplashSideEffect : BaseSideEffect {
    object NavigateToMain : SplashSideEffect()
    object NavigateToEnterPin : SplashSideEffect()
}