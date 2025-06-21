package com.example.mymoney.presentation.screens.account


sealed class AccountSideEffect {
    data object NavigateToEdit : AccountSideEffect()
    data object NavigateToChangeCurrency : AccountSideEffect()
    data class ShowError(val message: String) : AccountSideEffect()
}