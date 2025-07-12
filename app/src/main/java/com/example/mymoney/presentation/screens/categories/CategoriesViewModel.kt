package com.example.mymoney.presentation.screens.categories

import androidx.lifecycle.viewModelScope
import com.example.mymoney.domain.entity.Category
import com.example.mymoney.domain.usecase.GetCategoriesUseCase
import com.example.mymoney.presentation.base.viewmodel.BaseViewModel
import com.example.mymoney.utils.NetworkMonitor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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
class CategoriesViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    networkMonitor: NetworkMonitor
): BaseViewModel<CategoriesUiState, CategoriesEvent, CategoriesSideEffect>(
    networkMonitor,
    CategoriesUiState()
) {

    private var allCategories: List<Category> = emptyList()

    override val uiState: StateFlow<CategoriesUiState> = combine(
        super.uiState,
        super.uiState.map { it.searchQuery }.distinctUntilChanged()
    ) { state, query ->
        val filtered = if (query.isBlank()) {
            allCategories
        } else {
            allCategories.filter {
                it.name.contains(query.trim(), ignoreCase = true)
            }
        }
        state.copy(categories = filtered)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), super.uiState.value)

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
                    emitEffect(CategoriesSideEffect.NavigateToCategoryDetails)
                }
            }
            is CategoriesEvent.OnSearchQueryChanged -> {
                _uiState.update { it.copy(searchQuery = event.query) }
            }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = getCategoriesUseCase()
            result
                .onSuccess { categories ->
                    allCategories = categories
                    _uiState.update {
                        it.copy(
                            /*categories = categories,*/
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
