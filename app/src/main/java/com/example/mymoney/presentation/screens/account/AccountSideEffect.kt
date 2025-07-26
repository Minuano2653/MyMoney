package com.example.mymoney.presentation.screens.account

import com.example.core.ui.contract.BaseSideEffect

/**
 * Сайд-эффекты, связанные с экраном аккаунта.
 * Используются для навигации и отображения одноразовых событий, таких как ошибки.
 */
sealed class AccountSideEffect: BaseSideEffect {
    data class NavigateToEditAccount(val accountId: Int): AccountSideEffect()
    data class ShowError(val message: Int): AccountSideEffect()
}