package com.example.mymoney.di.viewmodel.modules

import androidx.lifecycle.ViewModel
import com.example.mymoney.di.viewmodel.factories.AddTransactionViewModelFactory
import com.example.mymoney.di.viewmodel.factories.AnalysisViewModelFactory
import com.example.mymoney.di.viewmodel.factories.EditAccountViewModelFactory
import com.example.mymoney.di.viewmodel.factories.EditTransactionViewModelFactory
import com.example.mymoney.di.viewmodel.factories.HistoryViewModelFactory
import com.example.mymoney.di.viewmodel.factories.ViewModelAssistedFactory
import com.example.mymoney.di.viewmodel.keys.ViewModelAssistedKey
import com.example.mymoney.presentation.screens.add_transaction.AddTransactionViewModel
import com.example.mymoney.presentation.screens.analysis.AnalysisViewModel
import com.example.mymoney.presentation.screens.edit_account.EditAccountViewModel
import com.example.mymoney.presentation.screens.edit_transaction.EditTransactionViewModel
import com.example.mymoney.presentation.screens.history.HistoryViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelAssistedModule {
    @Binds
    @IntoMap
    @ViewModelAssistedKey(EditAccountViewModel::class)
    abstract fun bindEditAccountViewModelFactory(factory: EditAccountViewModelFactory): ViewModelAssistedFactory<out ViewModel>

    @Binds
    @IntoMap
    @ViewModelAssistedKey(HistoryViewModel::class)
    abstract fun bindHistoryViewModelFactory(factory: HistoryViewModelFactory): ViewModelAssistedFactory<out ViewModel>

    @Binds
    @IntoMap
    @ViewModelAssistedKey(EditTransactionViewModel::class)
    abstract fun bindEditTransactionViewModelFactory(factory: EditTransactionViewModelFactory): ViewModelAssistedFactory<out ViewModel>

    @Binds
    @IntoMap
    @ViewModelAssistedKey(AddTransactionViewModel::class)
    abstract fun bindAddTransactionViewModelFactory(factory: AddTransactionViewModelFactory): ViewModelAssistedFactory<out ViewModel>

    @Binds
    @IntoMap
    @ViewModelAssistedKey(AnalysisViewModel::class)
    abstract fun bindAnalysisViewModelFactory(factory: AnalysisViewModelFactory): ViewModelAssistedFactory<out ViewModel>
}