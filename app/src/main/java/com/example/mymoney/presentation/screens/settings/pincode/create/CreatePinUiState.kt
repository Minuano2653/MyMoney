package com.example.mymoney.presentation.screens.settings.pincode.create

import com.example.core.ui.contract.BaseUiState

data class CreatePinUiState(
    val currentPin: String = "",
    val confirmPin: String = "",
    val isConfirmationStep: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: Int? = null
) : BaseUiState