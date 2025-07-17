package com.example.mymoney.di.viewmodel.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import javax.inject.Inject
import javax.inject.Provider

@Suppress("UNCHECKED_CAST")
class ViewModelFactory @Inject constructor(
    private val viewModels: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>,
    private val assistedViewModels: Map<Class<out ViewModel>, @JvmSuppressWildcards ViewModelAssistedFactory<out ViewModel>>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val savedStateHandle = extras.createSavedStateHandle()

        val assistedFactory = assistedViewModels[modelClass]
        if (assistedFactory != null) {
            return assistedFactory.create(savedStateHandle) as T
        }

        val viewModelProvider = viewModels[modelClass]
            ?: throw IllegalArgumentException("ViewModel class $modelClass not found")

        return viewModelProvider.get() as T
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        throw UnsupportedOperationException("Use create(Class<T>, CreationExtras) instead")
    }
}