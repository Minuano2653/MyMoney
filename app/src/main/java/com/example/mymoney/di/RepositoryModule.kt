package com.example.mymoney.di

import com.example.mymoney.data.remote.datasource.AccountsRemoteDataSource
import com.example.mymoney.data.remote.datasource.CategoriesRemoteDataSource
import com.example.mymoney.data.remote.datasource.TransactionsRemoteDataSource
import com.example.mymoney.data.repository.AccountsRepositoryImpl
import com.example.mymoney.data.repository.CategoriesRepositoryImpl
import com.example.mymoney.data.repository.TransactionsRepositoryImpl
import com.example.mymoney.domain.repository.AccountsRepository
import com.example.mymoney.domain.repository.CategoriesRepository
import com.example.mymoney.domain.repository.TransactionsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Модуль Dagger-Hilt для предоставления реализаций репозиториев.
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideTransactionsRepository(
        remoteDataSource: TransactionsRemoteDataSource
    ): TransactionsRepository = TransactionsRepositoryImpl(remoteDataSource)

    @Provides
    @Singleton
    fun provideAccountsRepository(
        remoteDataSource: AccountsRemoteDataSource
    ): AccountsRepository = AccountsRepositoryImpl(remoteDataSource)

    @Provides
    @Singleton
    fun provideCategoriesRepository(
        remoteDataSource: CategoriesRemoteDataSource
    ): CategoriesRepository = CategoriesRepositoryImpl(remoteDataSource)
}
