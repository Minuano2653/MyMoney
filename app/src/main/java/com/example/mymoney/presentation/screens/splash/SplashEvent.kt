package com.example.mymoney.presentation.screens.splash

import com.example.core.ui.contract.BaseEvent

sealed class SplashEvent: BaseEvent {
    object LoadAccount : SplashEvent()
}