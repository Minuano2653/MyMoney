package com.example.mymoney.presentation.screens.account

import com.example.mymoney.presentation.base.contract.BaseEvent

/**
 * События, связанные с экраном аккаунта.
 * Используются для обработки пользовательских действий во ViewModel.
 */
sealed class AccountEvent: BaseEvent {
    object LoadAccount : AccountEvent()
    object OnEditClicked : AccountEvent()
}
