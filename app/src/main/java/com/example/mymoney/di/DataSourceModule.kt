package com.example.mymoney.di

import com.example.mymoney.data.remote.api.AccountsApi
import com.example.mymoney.data.remote.api.CategoriesApi
import com.example.mymoney.data.remote.api.TransactionsApi
import com.example.mymoney.data.remote.datasource.AccountsRemoteDataSource
import com.example.mymoney.data.remote.datasource.CategoriesRemoteDataSource
import com.example.mymoney.data.remote.datasource.TransactionsRemoteDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DataSourceModule {

    @Provides
    @Singleton
    fun provideTransactionsRemoteDataSource(api: TransactionsApi) =
        TransactionsRemoteDataSource(api)

    @Provides
    @Singleton
    fun provideAccountsRemoteDataSource(api: AccountsApi) =
        AccountsRemoteDataSource(api)

    @Provides
    @Singleton
    fun provideCategoriesRemoteDataSource(api: CategoriesApi) =
        CategoriesRemoteDataSource(api)
}