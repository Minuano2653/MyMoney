package com.example.mymoney.presentation.screens.edit_transaction

import com.example.core.ui.contract.BaseSideEffect


sealed class EditTransactionSideEffect: BaseSideEffect {
    object NavigateBack : EditTransactionSideEffect()
    data class ShowSnackbar(val message: Int) : EditTransactionSideEffect()
}