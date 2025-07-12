package com.example.mymoney.presentation.add_transaction

import com.example.mymoney.domain.entity.Category
import com.example.mymoney.presentation.base.contract.BaseEvent

sealed class AddTransactionEvent : BaseEvent {
    data class LoadCategories(val isIncome: Boolean) : AddTransactionEvent()
    object ShowCategorySheet : AddTransactionEvent()
    object DismissCategorySheet : AddTransactionEvent()
    object CancelChangesClicked : AddTransactionEvent()
    object SaveChangesClicked : AddTransactionEvent()
    object ShowAmountDialog : AddTransactionEvent()
    object DismissAmountDialog : AddTransactionEvent()
    object ShowDatePicker : AddTransactionEvent()
    object DismissDatePicker : AddTransactionEvent()
    object ShowTimePicker : AddTransactionEvent()
    object DismissTimePicker : AddTransactionEvent()
    data class OnAmountChanged(val amount: String) : AddTransactionEvent()
    data class OnCommentChanged(val comment: String) : AddTransactionEvent()
    data class OnDateSelected(val date: String) : AddTransactionEvent()
    data class OnTimeSelected(val time: String) : AddTransactionEvent()
    data class OnCategorySelected(val selectedCategory: Category) : AddTransactionEvent()
}