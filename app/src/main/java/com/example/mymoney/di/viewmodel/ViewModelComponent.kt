package com.example.mymoney.di.viewmodel

import androidx.lifecycle.ViewModelProvider
import com.example.core.domain.di.DomainModule
import com.example.mymoney.di.viewmodel.modules.ViewModelAssistedModule
import com.example.mymoney.di.viewmodel.modules.ViewModelModule
import com.example.mymoney.di.viewmodel.scope.ViewModelScope
import dagger.Subcomponent

@ViewModelScope
@Subcomponent(
    modules = [
        ViewModelModule::class,
        ViewModelAssistedModule::class,
        DomainModule::class
    ]
)
interface ViewModelComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): ViewModelComponent
    }

    fun getViewModelFactory(): ViewModelProvider.Factory
}