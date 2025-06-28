package com.example.mymoney.di

import com.example.mymoney.data.remote.api.AccountsApi
import com.example.mymoney.data.remote.api.CategoriesApi
import com.example.mymoney.data.remote.api.TransactionsApi
import com.example.mymoney.data.remote.datasource.AccountsRemoteDataSource
import com.example.mymoney.data.remote.datasource.CategoriesRemoteDataSource
import com.example.mymoney.data.remote.datasource.TransactionsRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Модуль Dagger-Hilt для предоставления удалённых источников данных (Remote Data Sources).
 */
@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideTransactionsRemoteDataSource(
        api: TransactionsApi
    ): TransactionsRemoteDataSource = TransactionsRemoteDataSource(api)

    @Provides
    @Singleton
    fun provideAccountsRemoteDataSource(
        api: AccountsApi
    ): AccountsRemoteDataSource = AccountsRemoteDataSource(api)

    @Provides
    @Singleton
    fun provideCategoriesRemoteDataSource(
        api: CategoriesApi
    ): CategoriesRemoteDataSource = CategoriesRemoteDataSource(api)
}
