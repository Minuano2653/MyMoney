package com.example.mymoney.presentation.screens.splash

import com.example.mymoney.presentation.base.contract.BaseSideEffect

sealed class SplashSideEffect: BaseSideEffect {
    object NavigateToMain : SplashSideEffect()
}
