package com.example.mymoney.presentation.screens.settings.pincode.enter

import com.example.core.ui.contract.BaseEvent

sealed class EnterPinEvent : BaseEvent {
    data class PinDigitAdded(val digit: String) : EnterPinEvent()
    object PinDigitRemoved : EnterPinEvent()
    object PinCleared : EnterPinEvent()
}