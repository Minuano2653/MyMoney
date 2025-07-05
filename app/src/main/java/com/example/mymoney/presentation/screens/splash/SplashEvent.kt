package com.example.mymoney.presentation.screens.splash

import com.example.mymoney.presentation.base.contract.BaseEvent

sealed class SplashEvent: BaseEvent {
    object LoadAccount : SplashEvent()
}