package com.example.mymoney.presentation.screens.edit_transaction

import com.example.core.domain.entity.Account
import com.example.core.domain.entity.Category
import com.example.core.ui.contract.BaseUiState

data class EditTransactionUiState(
    val isLoadingCategories: Boolean = false,
    val isLoadingTransaction: Boolean = false,
    val isDeleting: Boolean = false,
    val isSaving: Boolean = false,
    val categories: List<Category> = emptyList(),
    val account: Account? = null,
    val selectedCategory: Category? = null,
    val amount: String = "",
    val date: String = "",
    val time: String = "",
    val comment: String? = null,
    val showAmountDialog: Boolean = false,
    val showCategorySheet: Boolean = false,
    val showDatePicker: Boolean = false,
    val showTimePicker: Boolean = false,
    val isIncome: Boolean = false,
): BaseUiState