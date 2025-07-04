package com.example.mymoney.presentation.screens.edit_account

import com.example.mymoney.presentation.base.contract.BaseEvent

sealed class EditAccountEvent: BaseEvent {
    object LoadAccount : EditAccountEvent()
    object OnCancelChangesClicked : EditAccountEvent()
    object OnSaveChangesClicked : EditAccountEvent()
    data class OnNameChanged(val name: String) : EditAccountEvent()
    data class OnBalanceChanged(val balance: String) : EditAccountEvent()
    data class OnCurrencyChanged(val currency: String) : EditAccountEvent()
    object OnCurrencyClicked : EditAccountEvent()
    object OnBottomSheetDismissed : EditAccountEvent()
}