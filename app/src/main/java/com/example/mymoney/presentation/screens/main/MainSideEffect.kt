package com.example.mymoney.presentation.screens.main

import com.example.core.ui.contract.BaseSideEffect

sealed class MainSideEffect: BaseSideEffect {
    data class ShowNetworkStatus(val messageRes: Int) : MainSideEffect()
}