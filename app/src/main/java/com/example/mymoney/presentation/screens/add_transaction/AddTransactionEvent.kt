package com.example.mymoney.presentation.screens.add_transaction

import com.example.core.domain.entity.Category
import com.example.core.ui.contract.BaseEvent

sealed class AddTransactionEvent : BaseEvent {
    object LoadCategories : AddTransactionEvent()
    object CancelChangesClicked : AddTransactionEvent()
    object SaveChangesClicked : AddTransactionEvent()
    data class OnAmountChanged(val amount: String) : AddTransactionEvent()
    data class OnCommentChanged(val comment: String) : AddTransactionEvent()
    data class OnDateSelected(val date: String) : AddTransactionEvent()
    data class OnTimeSelected(val time: String) : AddTransactionEvent()
    data class OnCategorySelected(val selectedCategory: Category) : AddTransactionEvent()
}