package com.example.mymoney.presentation.screens.main

import com.example.mymoney.presentation.base.contract.BaseSideEffect

sealed class MainSideEffect: BaseSideEffect {
    data class ShowNetworkStatus(val messageRes: Int) : MainSideEffect()
}