package com.example.mymoney.di

import com.example.mymoney.data.repository.AccountsRepositoryImpl
import com.example.mymoney.data.repository.CategoriesRepositoryImpl
import com.example.mymoney.data.repository.TransactionsRepositoryImpl
import com.example.mymoney.domain.repository.AccountsRepository
import com.example.mymoney.domain.repository.CategoriesRepository
import com.example.mymoney.domain.repository.TransactionsRepository
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindAccountsRepository(
        impl: AccountsRepositoryImpl
    ): AccountsRepository

    @Binds
    abstract fun bindCategoriesRepository(
        impl: CategoriesRepositoryImpl
    ): CategoriesRepository

    @Binds
    abstract fun bindTransactionsRepository(
        impl: TransactionsRepositoryImpl
    ): TransactionsRepository
}