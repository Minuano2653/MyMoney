package com.example.mymoney.di

import com.example.mymoney.data.remote.api.AccountsApi
import com.example.mymoney.data.remote.api.CategoriesApi
import com.example.mymoney.data.remote.api.TransactionsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideTransactionsApi(retrofit: Retrofit): TransactionsApi {
        return retrofit.create(TransactionsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAccountsApi(retrofit: Retrofit): AccountsApi {
        return retrofit.create(AccountsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCategoriesApi(retrofit: Retrofit): CategoriesApi {
        return retrofit.create(CategoriesApi::class.java)
    }
}