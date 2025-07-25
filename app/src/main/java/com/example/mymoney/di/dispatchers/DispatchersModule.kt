package com.example.mymoney.di.dispatchers

import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import com.example.mymoney.di.dispatchers.Dispatchers.IO
import com.example.mymoney.di.dispatchers.Dispatchers.Default
import com.example.mymoney.di.dispatchers.Dispatchers.Main
import com.example.mymoney.di.dispatchers.Dispatchers.Unconfined
import dagger.Module

@Module
object DispatchersModule {
    @Provides
    @Dispatcher(IO)
    fun providesIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Dispatcher(Default)
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @Dispatcher(Main)
    fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @Dispatcher(Unconfined)
    fun providesUnconfinedDispatcher(): CoroutineDispatcher = Dispatchers.Unconfined
}