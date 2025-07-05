package com.example.mymoney.presentation.screens.edit_account

import com.example.mymoney.presentation.base.contract.BaseSideEffect

sealed class EditAccountSideEffect: BaseSideEffect {
    object NavigateBack : EditAccountSideEffect()
    data class ShowError(val message: String) : EditAccountSideEffect()
}