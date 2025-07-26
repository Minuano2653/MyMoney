package com.example.mymoney.presentation.screens.settings.about

import com.example.core.ui.contract.BaseEvent

sealed class AboutEvent: BaseEvent {
    object LoadAppInfo : AboutEvent()
}