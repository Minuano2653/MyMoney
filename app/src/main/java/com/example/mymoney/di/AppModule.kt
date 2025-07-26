package com.example.mymoney.di

import android.content.Context
import com.example.mymoney.data.local.datasource.AppDataStore
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object AppModule {

    @Provides
    @Singleton
    fun provideAppDataStore(context: Context): AppDataStore =
        AppDataStore(context)
}