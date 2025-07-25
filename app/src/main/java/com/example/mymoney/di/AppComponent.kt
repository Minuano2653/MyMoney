package com.example.mymoney.di

import android.content.Context
import com.example.core.network.NetworkModule
import com.example.mymoney.data.local.datasource.AppDataStore
import com.example.mymoney.di.dispatchers.DispatchersModule
import com.example.mymoney.di.viewmodel.ViewModelComponent
import com.example.mymoney.presentation.MainActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        AppModule::class,
        NetworkModule::class,
        ApiModule::class,
        DataSourceModule::class,
        RepositoryModule::class,
        DatabaseModule::class,
        DaoModule::class,
        DispatchersModule::class
    ]
)
interface AppComponent {

    fun inject(activity: MainActivity)

    fun appDataStore(): AppDataStore

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun viewModelComponentFactory(): ViewModelComponent.Factory
}