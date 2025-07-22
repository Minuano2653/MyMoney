package com.example.mymoney.di

import com.example.mymoney.data.repository.AccountsRepositoryImpl
import com.example.mymoney.data.repository.CategoriesRepositoryImpl
import com.example.mymoney.data.repository.NetworkRepositoryImpl
import com.example.mymoney.data.repository.TransactionsRepositoryImpl
import com.example.mymoney.domain.repository.AccountsRepository
import com.example.mymoney.domain.repository.CategoriesRepository
import com.example.mymoney.domain.repository.NetworkRepository
import com.example.mymoney.domain.repository.TransactionsRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAccountsRepository(
        impl: AccountsRepositoryImpl
    ): AccountsRepository

    @Binds
    @Singleton
    abstract fun bindCategoriesRepository(
        impl: CategoriesRepositoryImpl
    ): CategoriesRepository

    @Binds
    @Singleton
    abstract fun bindTransactionsRepository(
        impl: TransactionsRepositoryImpl
    ): TransactionsRepository

    @Binds
    @Singleton
    abstract fun bindNetworkRepository(
        impl: NetworkRepositoryImpl
    ): NetworkRepository
}