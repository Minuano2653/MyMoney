package com.example.mymoney.presentation.screens.account

import com.example.mymoney.presentation.base.contract.BaseSideEffect

/**
 * Сайд-эффекты, связанные с экраном аккаунта.
 * Используются для навигации и отображения одноразовых событий, таких как ошибки.
 */
sealed class AccountSideEffect: BaseSideEffect {
    data object NavigateToEdit : AccountSideEffect()
    data object NavigateToChangeCurrency : AccountSideEffect()
    data class ShowError(val message: String) : AccountSideEffect()
}