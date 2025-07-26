package com.example.mymoney.presentation.screens.add_transaction

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.core.common.utils.DateUtils.combineDateAndTimeToIso
import com.example.core.domain.entity.Resource
import com.example.core.domain.usecase.CreateTransactionUseCase
import com.example.core.domain.usecase.GetCategoriesByTypeUseCase
import com.example.core.domain.usecase.ObserveAccountUseCase
import com.example.core.domain.usecase.ObserveCategoriesByTypeUseCase
import com.example.core.ui.viewmodel.BaseViewModel
import com.example.mymoney.R
import com.example.mymoney.presentation.navigation.TransactionDetail
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class AddTransactionViewModel @AssistedInject constructor(
    private val createTransactionUseCase: CreateTransactionUseCase,
    private val getCategoriesByTypeUseCase: GetCategoriesByTypeUseCase,
    @Assisted private val savedStateHandle: SavedStateHandle,
    observeAccountUseCase: ObserveAccountUseCase,
    observeCategoriesByTypeUseCase: ObserveCategoriesByTypeUseCase,
) : BaseViewModel<AddTransactionUiState, AddTransactionEvent, AddTransactionSideEffect>(
    AddTransactionUiState()
) {
    private val isIncome: Boolean = savedStateHandle.toRoute<TransactionDetail>().isIncome

    override val uiState: StateFlow<AddTransactionUiState> = combine(
        _uiState,
        observeAccountUseCase(),
        observeCategoriesByTypeUseCase(isIncome)
    ) { state, account, categoriesResource ->

        val newState = when (categoriesResource) {
            is Resource.Loading -> state.copy(
                account = account,
                categories = categoriesResource.data ?: emptyList(),
                isLoadingCategories = true,
                error = null
            )
            is Resource.Success -> state.copy(
                account = account,
                categories = categoriesResource.data ?: emptyList(),
                isLoadingCategories = false,
                error = null
            )
            is Resource.Error -> state.copy(
                account = account,
                categories = categoriesResource.data ?: emptyList(),
                isLoadingCategories = false,
                error = mapErrorToMessage(categoriesResource.error)
            )
        }
        newState
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        _uiState.value
    )

    init {
        _uiState.update { it.copy(isIncome = isIncome) }
    }

    override fun handleEvent(event: AddTransactionEvent) {
        when (event) {
            is AddTransactionEvent.OnAmountChanged -> {
                _uiState.update { it.copy(amount = event.amount) }
            }
            is AddTransactionEvent.CancelChangesClicked -> {
                emitEffect(AddTransactionSideEffect.NavigateBack)
            }
            is AddTransactionEvent.OnCategorySelected -> {
                _uiState.update {
                    it.copy(selectedCategory = event.selectedCategory)
                }
            }
            is AddTransactionEvent.OnCommentChanged -> {
                _uiState.update { it.copy(comment = event.comment) }
            }
            is AddTransactionEvent.OnDateSelected -> {
                _uiState.update { it.copy(date = event.date) }
            }
            is AddTransactionEvent.SaveChangesClicked -> {
                saveTransaction()
            }
            is AddTransactionEvent.OnTimeSelected -> {
                _uiState.update { it.copy(time = event.time) }
            }
            is AddTransactionEvent.LoadCategories -> {
                loadCategories()
            }
        }
    }

    private fun saveTransaction() {
        val currentState = uiState.value

        if (currentState.selectedCategory == null) {
            emitEffect(AddTransactionSideEffect.ShowSnackbar(R.string.choose_category_error))
            return
        }
        if (currentState.amount.toBigDecimalOrNull() == null) {
            emitEffect(AddTransactionSideEffect.ShowSnackbar(R.string.invalid_sum_format))
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isSaving = true, error = null) }

            val account = currentState.account
            val category = currentState.selectedCategory
            val transactionDate = combineDateAndTimeToIso(
                currentState.date,
                currentState.time
            )

            if (account != null && transactionDate != null) {
                createTransactionUseCase(
                    accountId = account.id,
                    categoryId = category.id,
                    amount = currentState.amount,
                    transactionDate = transactionDate,
                    comment = currentState.comment
                ).fold(
                    onSuccess = { createdTransaction ->
                        _uiState.update { it.copy(isSaving = false) }
                        emitEffect(AddTransactionSideEffect.NavigateBack)
                    },
                    onFailure = { error ->
                        _uiState.update { it.copy(isSaving = false) }
                        val message = mapErrorToMessage(error)
                        emitEffect(AddTransactionSideEffect.ShowSnackbar(message))
                    }
                )
            }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoadingCategories = true) }
            getCategoriesByTypeUseCase(isIncome).fold(
                onSuccess = { categories ->
                    _uiState.update {
                        it.copy(
                            categories = categories,
                            isLoadingCategories = false,
                            error = null
                        )
                    }
                },
                onFailure = { error ->
                    val message = mapErrorToMessage(error)
                    _uiState.update { it.copy(isLoadingCategories = false) }
                    emitEffect(AddTransactionSideEffect.ShowSnackbar(message))
                }
            )
        }
    }


    private fun mapErrorToMessage(error: Throwable?): Int {
        return when (error) {
            is UnknownHostException -> R.string.no_network_connection
            is SocketTimeoutException -> R.string.response_timeout
            is HttpException -> when (error.code()) {
                400 -> R.string.incorrect_data
                401 -> R.string.unauthorised_access
                404 -> R.string.data_not_found
                500 -> R.string.internal_server_error
                else -> R.string.unknown_error
            }
            else -> R.string.failed_to_save_transaction
        }
    }
}