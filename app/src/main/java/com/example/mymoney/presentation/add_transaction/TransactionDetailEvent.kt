package com.example.mymoney.presentation.add_transaction

import com.example.mymoney.domain.entity.Category
import com.example.mymoney.presentation.base.contract.BaseEvent

sealed class TransactionDetailEvent : BaseEvent {
    object LoadCategories : TransactionDetailEvent()
    object ShowCategorySheet : TransactionDetailEvent()
    object DismissCategorySheet : TransactionDetailEvent()
    object CancelChangesClicked : TransactionDetailEvent()
    object SaveChangesClicked : TransactionDetailEvent()
    object ShowAmountDialog : TransactionDetailEvent()
    object DismissAmountDialog : TransactionDetailEvent()
    object ShowDatePicker : TransactionDetailEvent()
    object DismissDatePicker : TransactionDetailEvent()
    object ShowTimePicker : TransactionDetailEvent()
    object DismissTimePicker : TransactionDetailEvent()
    data class OnAmountChanged(val amount: String) : TransactionDetailEvent()
    data class OnCommentChanged(val comment: String) : TransactionDetailEvent()
    data class OnDateSelected(val date: String) : TransactionDetailEvent()
    data class OnTimeSelected(val time: String) : TransactionDetailEvent()
    data class OnCategorySelected(val selectedCategory: Category) : TransactionDetailEvent()
}