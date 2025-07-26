package com.example.mymoney.presentation.screens.categories

import androidx.lifecycle.viewModelScope
import com.example.core.domain.entity.Category
import com.example.core.domain.entity.Resource
import com.example.core.domain.usecase.GetCategoriesUseCase
import com.example.core.domain.usecase.ObserveCategoriesUseCase
import com.example.core.ui.viewmodel.BaseViewModel
import com.example.mymoney.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject


class CategoriesViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    observeCategoriesUseCase: ObserveCategoriesUseCase,
): BaseViewModel<CategoriesUiState, CategoriesEvent, CategoriesSideEffect>(
    CategoriesUiState()
) {

    override val uiState: StateFlow<CategoriesUiState> = combine(
        observeCategoriesUseCase(),
        super.uiState.map { it.searchQuery }.distinctUntilChanged()
    ) { categoriesResource, query ->
        when (categoriesResource) {
            is Resource.Loading -> _uiState.value.copy(
                isLoading = true,
                categories = filterCategories(
                    categoriesResource.data ?: emptyList(),
                    query
                ),
                error = null
            )

            is Resource.Success -> {
                val categories = categoriesResource.data ?: emptyList()
                _uiState.value.copy(
                    isLoading = false,
                    categories = filterCategories(categories, query),
                    error = null
                )
            }

            is Resource.Error -> {
                val message = mapErrorToMessage(categoriesResource.error)
                emitEffect(CategoriesSideEffect.ShowError(message))

                _uiState.value.copy(
                    isLoading = false,
                    categories = filterCategories(
                        categoriesResource.data ?: emptyList(),
                        query
                    ),
                    error = message
                )
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CategoriesUiState()
    )

    private fun filterCategories(categories: List<Category>, query: String): List<Category> {
        return if (query.isBlank()) {
            categories
        } else {
            categories.filter {
                it.name.contains(query.trim(), ignoreCase = true)
            }
        }
    }

    override fun handleEvent(event: CategoriesEvent) {
        when (event) {
            is CategoriesEvent.LoadCategories -> {
                loadCategories()
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
                    _uiState.update {
                        it.copy(
                            categories = categories,
                            isLoading = false,
                            error = null
                        )
                    }
                }
                .onFailure { error ->
                    val message = mapErrorToMessage(error)
                    _uiState.update { it.copy(isLoading = false, error = message) }
                    emitEffect(CategoriesSideEffect.ShowError(message))
                }
        }
    }

    private fun mapErrorToMessage(error: Throwable?): Int {
        return when (error) {
            is UnknownHostException ->  R.string.no_network_connection
            is SocketTimeoutException -> R.string.response_timeout
            is HttpException -> when (error.code()) {
                401 -> R.string.unauthorised_access
                500 -> R.string.internal_server_error
                else -> R.string.unknown_error
            }
            else -> {
                R.string.failed_to_load_categories
            }
        }
    }
}
