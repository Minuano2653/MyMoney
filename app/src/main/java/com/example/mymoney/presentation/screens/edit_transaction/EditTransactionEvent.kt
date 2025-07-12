package com.example.mymoney.presentation.screens.edit_transaction

import com.example.mymoney.domain.entity.Category
import com.example.mymoney.presentation.base.contract.BaseEvent

sealed class EditTransactionEvent: BaseEvent {
    object LoadCategories : EditTransactionEvent()
    object LoadTransaction : EditTransactionEvent()
    object UpdateTransaction : EditTransactionEvent()
    object DeleteTransaction : EditTransactionEvent()
    object ShowCategorySheet : EditTransactionEvent()
    object DismissCategorySheet : EditTransactionEvent()
    object CancelChanges : EditTransactionEvent()
    object ShowAmountDialog : EditTransactionEvent()
    object DismissAmountDialog : EditTransactionEvent()
    object ShowDatePicker : EditTransactionEvent()
    object DismissDatePicker : EditTransactionEvent()
    object ShowTimePicker : EditTransactionEvent()
    object DismissTimePicker : EditTransactionEvent()
    data class OnAmountChanged(val amount: String) : EditTransactionEvent()
    data class OnCommentChanged(val comment: String) : EditTransactionEvent()
    data class OnDateSelected(val date: String) : EditTransactionEvent()
    data class OnTimeSelected(val time: String) : EditTransactionEvent()
    data class OnCategorySelected(val selectedCategory: Category) : EditTransactionEvent()
    data class SetInitData(val isIncome: Boolean, val transactionId: Int): EditTransactionEvent()
}