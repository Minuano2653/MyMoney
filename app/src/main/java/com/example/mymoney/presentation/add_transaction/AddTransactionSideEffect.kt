package com.example.mymoney.presentation.add_transaction

import com.example.mymoney.presentation.base.contract.BaseSideEffect

sealed class AddTransactionSideEffect : BaseSideEffect {
    object NavigateBack : AddTransactionSideEffect()
    data class ShowSnackbar(val message: String) : AddTransactionSideEffect()
}