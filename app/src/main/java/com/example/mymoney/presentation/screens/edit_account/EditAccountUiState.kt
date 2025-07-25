package com.example.mymoney.presentation.screens.edit_account

import com.example.core.ui.contract.BaseUiState

data class EditAccountUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val name: String = "",
    val balance: String = "",
    val showBottomSheet: Boolean = false,
    val currency: String = "",
    val hasChanges: Boolean = false
): BaseUiState