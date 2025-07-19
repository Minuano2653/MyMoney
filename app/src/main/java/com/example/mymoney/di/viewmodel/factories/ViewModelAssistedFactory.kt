package com.example.mymoney.di.viewmodel.factories

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.mymoney.presentation.screens.add_transaction.AddTransactionViewModel
import com.example.mymoney.presentation.screens.analysis.AnalysisViewModel
import com.example.mymoney.presentation.screens.edit_account.EditAccountViewModel
import com.example.mymoney.presentation.screens.edit_transaction.EditTransactionViewModel
import com.example.mymoney.presentation.screens.history.HistoryViewModel
import dagger.assisted.AssistedFactory

interface ViewModelAssistedFactory<T : ViewModel> {
    fun create(savedStateHandle: SavedStateHandle): T
}

@AssistedFactory
interface EditAccountViewModelFactory : ViewModelAssistedFactory<EditAccountViewModel>

@AssistedFactory
interface HistoryViewModelFactory : ViewModelAssistedFactory<HistoryViewModel>

@AssistedFactory
interface EditTransactionViewModelFactory : ViewModelAssistedFactory<EditTransactionViewModel>

@AssistedFactory
interface AddTransactionViewModelFactory : ViewModelAssistedFactory<AddTransactionViewModel>

@AssistedFactory
interface AnalysisViewModelFactory : ViewModelAssistedFactory<AnalysisViewModel>