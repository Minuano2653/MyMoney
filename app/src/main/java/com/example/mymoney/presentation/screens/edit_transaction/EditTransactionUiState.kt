package com.example.mymoney.presentation.screens.edit_transaction

import com.example.mymoney.domain.entity.Account
import com.example.mymoney.domain.entity.Category
import com.example.mymoney.presentation.base.contract.BaseUiState
import com.example.mymoney.utils.DateUtils

data class EditTransactionUiState(
    val isLoadingCategories: Boolean = false,
    val isLoadingTransaction: Boolean = false,
    val isDeleting: Boolean = false,
    val isSaving: Boolean = false,
    val categories: List<Category> = emptyList(),
    val account: Account? = null,
    val selectedCategory: Category? = null,
    val amount: String = "",
    val date: String = DateUtils.getTodayDayMonthYearFormatted(),
    val time: String = DateUtils.getCurrentTime(),
    val comment: String? = null,
    val showAmountDialog: Boolean = false,
    val showCategorySheet: Boolean = false,
    val showDatePicker: Boolean = false,
    val showTimePicker: Boolean = false,
    val error: String? = null,
    val isIncome: Boolean = false,
    val transactionId: Int = -1,
): BaseUiState