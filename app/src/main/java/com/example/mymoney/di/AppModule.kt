package com.example.mymoney.di

import android.content.Context
import com.example.mymoney.data.remote.datasource.AccountDataStore
import com.example.mymoney.utils.NetworkMonitor
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object AppModule {

    @Provides
    @Singleton
    fun provideAccountDataStore(context: Context): AccountDataStore =
        AccountDataStore(context)

    @Provides
    @Singleton
    fun provideNetworkMonitor(context: Context): NetworkMonitor =
        NetworkMonitor(context)
}