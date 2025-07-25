package com.example.core.domain.di

import com.example.core.domain.repository.AccountsRepository
import com.example.core.domain.repository.CategoriesRepository
import com.example.core.domain.repository.TransactionsRepository
import com.example.core.domain.usecase.CreateTransactionUseCase
import com.example.core.domain.usecase.DeleteTransactionUseCase
import com.example.core.domain.usecase.GetAccountIdUseCase
import com.example.core.domain.usecase.GetAccountUseCase
import com.example.core.domain.usecase.GetAnalysisUseCase
import com.example.core.domain.usecase.GetCategoriesByTypeUseCase
import com.example.core.domain.usecase.GetCategoriesUseCase
import com.example.core.domain.usecase.GetInitAccountUseCase
import com.example.core.domain.usecase.GetTransactionUseCase
import com.example.core.domain.usecase.GetTransactionsByTypeAndPeriodUseCase
import com.example.core.domain.usecase.ObserveAccountUseCase
import com.example.core.domain.usecase.UpdateAccountUseCase
import com.example.core.domain.usecase.UpdateTransactionUseCase
import dagger.Module
import dagger.Provides

@Module
class DomainModule {

    @Provides
    fun provideCreateTransactionUseCase(
        repository: TransactionsRepository
    ): CreateTransactionUseCase = CreateTransactionUseCase(repository)

    @Provides
    fun provideDeleteTransactionUseCase(
        repository: TransactionsRepository
    ): DeleteTransactionUseCase = DeleteTransactionUseCase(repository)

    @Provides
    fun provideGetAccountIdUseCase(
        repository: AccountsRepository
    ): GetAccountIdUseCase = GetAccountIdUseCase(repository)

    @Provides
    fun provideGetAccountUseCase(
        repo: AccountsRepository,
        getAccountIdUseCase: GetAccountIdUseCase
    ): GetAccountUseCase = GetAccountUseCase(repo, getAccountIdUseCase)

    @Provides
    fun provideGetCategoriesByTypeUseCase(
        repository: CategoriesRepository,
    ): GetCategoriesByTypeUseCase = GetCategoriesByTypeUseCase(repository)

    @Provides
    fun provideGetCategoriesUseCase(
        repository: CategoriesRepository,
    ): GetCategoriesUseCase = GetCategoriesUseCase(repository)

    @Provides
    fun provideObserveAccountUseCase(
        accountsRepository: AccountsRepository,
    ): ObserveAccountUseCase = ObserveAccountUseCase(accountsRepository)

    @Provides
    fun provideGetInitAccountUseCase(
        repository: AccountsRepository
    ): GetInitAccountUseCase = GetInitAccountUseCase(repository)

    @Provides
    fun provideGetTransactionsByPeriodUseCase(
        repository: TransactionsRepository,
        getAccountIdUseCase: GetAccountIdUseCase,
    ): GetTransactionsByTypeAndPeriodUseCase =
        GetTransactionsByTypeAndPeriodUseCase(repository, getAccountIdUseCase)

    @Provides
    fun provideGetTransactionUseCase(
        repository: TransactionsRepository,
    ): GetTransactionUseCase = GetTransactionUseCase(repository)

    @Provides
    fun provideUpdateAccountUseCase(
        repository: AccountsRepository,
    ): UpdateAccountUseCase = UpdateAccountUseCase(repository)

    @Provides
    fun provideUpdateTransactionUseCase(
        repository: TransactionsRepository,
    ): UpdateTransactionUseCase = UpdateTransactionUseCase(repository)

    @Provides
    fun provideGetAnalysisUseCase(
        getTransactionsByTypeAndPeriodUseCase: GetTransactionsByTypeAndPeriodUseCase
    ): GetAnalysisUseCase = GetAnalysisUseCase(getTransactionsByTypeAndPeriodUseCase)
}