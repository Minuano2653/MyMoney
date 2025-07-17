package com.example.mymoney.di

import com.example.mymoney.data.remote.datasource.AccountDataStore
import com.example.mymoney.di.viewmodel.scope.ViewModelScope
import com.example.mymoney.domain.repository.AccountsRepository
import com.example.mymoney.domain.repository.CategoriesRepository
import com.example.mymoney.domain.repository.TransactionsRepository
import com.example.mymoney.domain.usecase.CreateTransactionUseCase
import com.example.mymoney.domain.usecase.DeleteTransactionUseCase
import com.example.mymoney.domain.usecase.GetAccountIdUseCase
import com.example.mymoney.domain.usecase.GetAccountUseCase
import com.example.mymoney.domain.usecase.GetAnalysisUseCase
import com.example.mymoney.domain.usecase.GetCategoriesByTypeUseCase
import com.example.mymoney.domain.usecase.GetCategoriesUseCase
import com.example.mymoney.domain.usecase.GetCurrentAccountUseCase
import com.example.mymoney.domain.usecase.GetInitAccountUseCase
import com.example.mymoney.domain.usecase.GetTransactionUseCase
import com.example.mymoney.domain.usecase.GetTransactionsByPeriodUseCase
import com.example.mymoney.domain.usecase.UpdateAccountUseCase
import com.example.mymoney.domain.usecase.UpdateTransactionUseCase
import dagger.Module
import dagger.Provides

@Module
class DomainModule {

    @Provides
    @ViewModelScope
    fun provideCreateTransactionUseCase(
        repository: TransactionsRepository
    ): CreateTransactionUseCase = CreateTransactionUseCase(repository)

    @Provides
    @ViewModelScope
    fun provideDeleteTransactionUseCase(
        repository: TransactionsRepository
    ): DeleteTransactionUseCase = DeleteTransactionUseCase(repository)

    @Provides
    @ViewModelScope
    fun provideGetAccountIdUseCase(
        repository: AccountsRepository
    ): GetAccountIdUseCase = GetAccountIdUseCase(repository)

    @Provides
    @ViewModelScope
    fun provideGetAccountUseCase(
        repo: AccountsRepository,
        getAccountIdUseCase: GetAccountIdUseCase
    ): GetAccountUseCase = GetAccountUseCase(repo, getAccountIdUseCase)

    @Provides
    @ViewModelScope
    fun provideGetCategoriesByTypeUseCase(
        repository: CategoriesRepository,
    ): GetCategoriesByTypeUseCase = GetCategoriesByTypeUseCase(repository)

    @Provides
    @ViewModelScope
    fun provideGetCategoriesUseCase(
        repository: CategoriesRepository,
    ): GetCategoriesUseCase = GetCategoriesUseCase(repository)

    @Provides
    @ViewModelScope
    fun provideGetCurrentAccountUseCase(
        accountDataStore: AccountDataStore,
    ): GetCurrentAccountUseCase = GetCurrentAccountUseCase(accountDataStore)

    @Provides
    @ViewModelScope
    fun provideGetInitAccountUseCase(
        repository: AccountsRepository
    ): GetInitAccountUseCase = GetInitAccountUseCase(repository)

    @Provides
    @ViewModelScope
    fun provideGetTransactionsByPeriodUseCase(
        repository: TransactionsRepository,
        getAccountIdUseCase: GetAccountIdUseCase,
    ): GetTransactionsByPeriodUseCase = GetTransactionsByPeriodUseCase(repository, getAccountIdUseCase)

    @Provides
    @ViewModelScope
    fun provideGetTransactionUseCase(
        repository: TransactionsRepository,
    ): GetTransactionUseCase = GetTransactionUseCase(repository)

    @Provides
    @ViewModelScope
    fun provideUpdateAccountUseCase(
        repository: AccountsRepository,
    ): UpdateAccountUseCase = UpdateAccountUseCase(repository)

    @Provides
    @ViewModelScope
    fun provideUpdateTransactionUseCase(
        repository: TransactionsRepository,
    ): UpdateTransactionUseCase = UpdateTransactionUseCase(repository)

    @Provides
    @ViewModelScope
    fun provideGetAnalysisUseCase(
        getTransactionsByPeriodUseCase: GetTransactionsByPeriodUseCase
    ): GetAnalysisUseCase = GetAnalysisUseCase(getTransactionsByPeriodUseCase)
}