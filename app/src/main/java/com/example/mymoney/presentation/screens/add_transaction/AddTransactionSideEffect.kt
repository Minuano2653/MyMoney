package com.example.mymoney.presentation.screens.add_transaction

import com.example.core.ui.contract.BaseSideEffect

sealed class AddTransactionSideEffect : BaseSideEffect {
    object NavigateBack : AddTransactionSideEffect()
    data class ShowSnackbar(val message: Int) : AddTransactionSideEffect()
}