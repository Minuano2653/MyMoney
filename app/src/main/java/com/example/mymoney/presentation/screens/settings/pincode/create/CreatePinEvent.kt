package com.example.mymoney.presentation.screens.settings.pincode.create

import com.example.core.ui.contract.BaseEvent

sealed class CreatePinEvent : BaseEvent {
    data class PinDigitAdded(val digit: String) : CreatePinEvent()
    object PinDigitRemoved : CreatePinEvent()
    object PinCleared : CreatePinEvent()
    object PinConfirmed : CreatePinEvent()
    object BackPressed : CreatePinEvent()
}