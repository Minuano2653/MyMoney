package com.example.mymoney.presentation.screens.categories

import androidx.lifecycle.viewModelScope
import com.example.mymoney.domain.usecase.GetCategoriesUseCase
import com.example.mymoney.presentation.base.viewmodel.BaseViewModel
import com.example.mymoney.utils.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel для экрана статей.
 *
 * Загружает список статей, обрабатывает события UI и управляет сайд-эффектами.
 *
 * @property getCategoriesUseCase UseCase для получения списка категорий.
 * @property networkMonitor Мониторинг состояния сети.
 */
@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    networkMonitor: NetworkMonitor
): BaseViewModel<CategoriesUiState, CategoriesEvent, CategoriesSideEffect>(
    networkMonitor,
    CategoriesUiState()
) {

    init {
        handleEvent(CategoriesEvent.LoadCategories)
    }

    override fun handleEvent(event: CategoriesEvent) {
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

    override fun onNetworkStateChanged(isConnected: Boolean) {
        val wasDisconnected = !_uiState.value.isNetworkAvailable
        _uiState.update { it.copy(isNetworkAvailable = isConnected) }

        if (!isConnected) {
            emitEffect(CategoriesSideEffect.ShowError("Нет подключения к интернету"))
        } else if (wasDisconnected && (_uiState.value.categories.isEmpty() || _uiState.value.error != null)) {
            handleEvent(CategoriesEvent.LoadCategories)
        }
    }
}
