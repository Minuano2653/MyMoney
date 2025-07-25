package com.example.mymoney.presentation.screens.account

import com.example.core.ui.contract.BaseEvent

/**
 * События, связанные с экраном аккаунта.
 * Используются для обработки пользовательских действий во ViewModel.
 */
sealed class AccountEvent: BaseEvent {
    object LoadAccount : AccountEvent()
    object OnEditClicked : AccountEvent()
}
