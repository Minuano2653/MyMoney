package com.example.mymoney.presentation.add_transaction

import com.example.mymoney.presentation.base.contract.BaseSideEffect

sealed class TransactionDetailSideEffect : BaseSideEffect {
    object NavigateBack : TransactionDetailSideEffect()
    data class ShowSnackbar(val message: String) : TransactionDetailSideEffect()
}