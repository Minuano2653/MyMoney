package com.example.mymoney.presentation.screens.settings.pincode.enter

import com.example.core.ui.contract.BaseUiState
import com.example.mymoney.presentation.screens.settings.pincode.create.CreatePinUiState

data class EnterPinUiState(
    val currentPin: String = "",
    val isLoading: Boolean = false
) : BaseUiState

fun EnterPinUiState.toCreatePinUiState(): CreatePinUiState {
    return CreatePinUiState(
        currentPin = this.currentPin,
        confirmPin = "",
        isConfirmationStep = true,
        isLoading = this.isLoading,
        errorMessage = null
    )
}