package com.example.mymoney.di

import com.example.mymoney.data.remote.api.AccountsApi
import com.example.mymoney.data.remote.api.CategoriesApi
import com.example.mymoney.data.remote.api.TransactionsApi
import com.example.mymoney.data.remote.datasource.AccountsRemoteDataSource
import com.example.mymoney.data.remote.datasource.CategoriesRemoteDataSource
import com.example.mymoney.data.remote.datasource.TransactionsRemoteDataSource
import dagger.Module
import dagger.Provides

@Module
object DataSourceModule {

    @Provides
    fun provideTransactionsRemoteDataSource(api: TransactionsApi) =
        TransactionsRemoteDataSource(api)

    @Provides
    fun provideAccountsRemoteDataSource(api: AccountsApi) =
        AccountsRemoteDataSource(api)

    @Provides
    fun provideCategoriesRemoteDataSource(api: CategoriesApi) =
        CategoriesRemoteDataSource(api)
}