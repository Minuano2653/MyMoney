package com.example.mymoney.di

import com.example.core.domain.repository.SettingsRepository
import com.example.core.domain.repository.AccountsRepository
import com.example.core.domain.repository.CategoriesRepository
import com.example.core.domain.repository.NetworkRepository
import com.example.core.domain.repository.TransactionsRepository
import com.example.mymoney.data.repository.AccountsRepositoryImpl
import com.example.mymoney.data.repository.SettingsRepositoryImpl
import com.example.mymoney.data.repository.CategoriesRepositoryImpl
import com.example.mymoney.data.repository.NetworkRepositoryImpl
import com.example.mymoney.data.repository.TransactionsRepositoryImpl
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

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        impl: SettingsRepositoryImpl
    ): SettingsRepository
}