package com.example.mymoney.di

import com.example.mymoney.data.local.database.AppDatabase
import com.example.mymoney.data.local.datasource.AccountDao
import com.example.mymoney.data.local.datasource.CategoryDao
import com.example.mymoney.data.local.datasource.TransactionDao
import dagger.Module
import dagger.Provides

@Module
object DaoModule {

    @Provides
    fun provideCategoryDao(database: AppDatabase) : CategoryDao = database.categoryDao()

    @Provides
    fun provideTransactionDao(database: AppDatabase) : TransactionDao = database.transactionDao()

    @Provides
    fun provideAccountDao(database: AppDatabase) : AccountDao = database.accountDao()
}