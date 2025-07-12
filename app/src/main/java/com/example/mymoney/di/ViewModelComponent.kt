package com.example.mymoney.di

import androidx.lifecycle.ViewModelProvider
import com.example.mymoney.di.scope.ViewModelScope
import dagger.Subcomponent

@ViewModelScope
@Subcomponent(modules = [ViewModelModule::class, DomainModule::class])
interface ViewModelComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): ViewModelComponent
    }

    fun getViewModelFactory(): ViewModelProvider.Factory
}