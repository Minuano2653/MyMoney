package com.example.mymoney.presentation.screens.edit_transaction

import com.example.core.domain.entity.Category
import com.example.core.ui.contract.BaseEvent

sealed class EditTransactionEvent: BaseEvent {
    object LoadCategories : EditTransactionEvent()
    object LoadTransaction : EditTransactionEvent()
    object UpdateTransaction : EditTransactionEvent()
    object DeleteTransaction : EditTransactionEvent()
    object CancelChanges : EditTransactionEvent()
    data class OnAmountChanged(val amount: String) : EditTransactionEvent()
    data class OnCommentChanged(val comment: String) : EditTransactionEvent()
    data class OnDateSelected(val date: String) : EditTransactionEvent()
    data class OnTimeSelected(val time: String) : EditTransactionEvent()
    data class OnCategorySelected(val selectedCategory: Category) : EditTransactionEvent()
}