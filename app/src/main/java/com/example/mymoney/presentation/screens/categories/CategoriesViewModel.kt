package com.example.mymoney.presentation.screens.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymoney.domain.usecase.GetCategoriesUseCase
import com.example.mymoney.utils.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoriesUiState())
    val uiState: StateFlow<CategoriesUiState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<CategoriesSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    init {
        observeNetworkConnectivity()
        handleEvent(CategoriesEvent.LoadCategories)
    }

    fun handleEvent(event: CategoriesEvent) {
        when (event) {
            is CategoriesEvent.LoadCategories -> {
                loadCategories()
            }
            is CategoriesEvent.OnCategoryClicked -> {
                viewModelScope.launch {
                    _sideEffect.emit(CategoriesSideEffect.NavigateToCategoryDetails)
                }
            }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = getCategoriesUseCase()
            result
                .onSuccess { categories ->
                    _uiState.update {
                        it.copy(
                            categories = categories,
                            isLoading = false,
                            error = null
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(isLoading = false, error = e.message)
                    }
                    _sideEffect.emit(CategoriesSideEffect.ShowError(e.message ?: "Неизвестная ошибка"))
                }
        }
    }

    private fun observeNetworkConnectivity() {
        viewModelScope.launch {
            networkMonitor.isConnected.collect { isConnected ->
                _uiState.update { it.copy(isNetworkAvailable = isConnected) }
                if (!isConnected) {
                    _sideEffect.emit(
                        CategoriesSideEffect.ShowError("Нет подключения к интернету")
                    )
                }
            }
        }
    }
}