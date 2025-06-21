package com.example.mymoney.presentation.screens.account


sealed class AccountEvent {
    object LoadAccount : AccountEvent()
    object OnEditClicked : AccountEvent()
    object OnCurrencyClicked : AccountEvent()
}