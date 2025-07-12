package com.example.mymoney.presentation.screens.edit_transaction

import com.example.mymoney.presentation.base.contract.BaseSideEffect


sealed class EditTransactionSideEffect: BaseSideEffect {
    object NavigateBack : EditTransactionSideEffect()
    data class ShowSnackbar(val message: String) : EditTransactionSideEffect()
}