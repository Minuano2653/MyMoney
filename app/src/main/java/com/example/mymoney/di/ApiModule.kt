package com.example.mymoney.di

import com.example.mymoney.data.remote.api.AccountsApi
import com.example.mymoney.data.remote.api.CategoriesApi
import com.example.mymoney.data.remote.api.TransactionsApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
object ApiModule {
    @Provides
    fun provideTransactionsApi(retrofit: Retrofit): TransactionsApi =
        retrofit.create(TransactionsApi::class.java)

    @Provides
    fun provideAccountsApi(retrofit: Retrofit): AccountsApi =
        retrofit.create(AccountsApi::class.java)

    @Provides
    fun provideCategoriesApi(retrofit: Retrofit): CategoriesApi =
        retrofit.create(CategoriesApi::class.java)
}