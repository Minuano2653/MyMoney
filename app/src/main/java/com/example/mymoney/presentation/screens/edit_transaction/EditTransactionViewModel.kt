package com.example.mymoney.presentation.screens.edit_transaction

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.core.common.utils.DateUtils
import com.example.core.common.utils.formatAmount
import com.example.core.domain.entity.Resource
import com.example.core.domain.usecase.DeleteTransactionUseCase
import com.example.core.domain.usecase.GetCategoriesByTypeUseCase
import com.example.core.domain.usecase.GetTransactionUseCase
import com.example.core.domain.usecase.ObserveAccountUseCase
import com.example.core.domain.usecase.ObserveCategoriesByTypeUseCase
import com.example.core.domain.usecase.ObserveTransactionUseCase
import com.example.core.domain.usecase.UpdateTransactionUseCase
import com.example.core.ui.viewmodel.BaseViewModel
import com.example.mymoney.R
import com.example.mymoney.presentation.navigation.EditTransaction
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class EditTransactionViewModel @AssistedInject constructor(
    private val getCategoriesByTypeUseCase: GetCategoriesByTypeUseCase,
    private val updateTransactionUseCase: UpdateTransactionUseCase,
    private val getTransactionUseCase: GetTransactionUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    private val observeTransactionUseCase: ObserveTransactionUseCase,
    private val observeAccountUseCase: ObserveAccountUseCase,
    private val observeCategoriesByTypeUseCase: ObserveCategoriesByTypeUseCase,
    @Assisted private val savedStateHandle: SavedStateHandle,
) : BaseViewModel<EditTransactionUiState, EditTransactionEvent, EditTransactionSideEffect>(
    EditTransactionUiState()
) {
    private val isIncome: Boolean = savedStateHandle.toRoute<EditTransaction>().isIncome
    private val transactionId: Int = savedStateHandle.toRoute<EditTransaction>().transactionId

    init {
        observeTransaction()
        observeAccount()
        observeCategories()
        _uiState.update { it.copy(isIncome = isIncome) }
    }

    override fun handleEvent(event: EditTransactionEvent) {
        when (event) {
            is EditTransactionEvent.OnAmountChanged -> _uiState.update { it.copy(amount = event.amount) }
            is EditTransactionEvent.CancelChanges -> emitEffect(EditTransactionSideEffect.NavigateBack)
            is EditTransactionEvent.OnCategorySelected -> _uiState.update { it.copy(selectedCategory = event.selectedCategory) }
            is EditTransactionEvent.OnCommentChanged -> _uiState.update { it.copy(comment = event.comment) }
            is EditTransactionEvent.OnDateSelected -> _uiState.update { it.copy(date = event.date) }
            is EditTransactionEvent.OnTimeSelected -> _uiState.update { it.copy(time = event.time) }
            is EditTransactionEvent.LoadCategories -> loadCategories()
            is EditTransactionEvent.LoadTransaction -> loadTransaction()
            is EditTransactionEvent.DeleteTransaction -> deleteTransaction()
            is EditTransactionEvent.UpdateTransaction -> updateTransaction()
        }
    }

    private fun updateTransaction() {
        val currentState = _uiState.value

        if (currentState.selectedCategory == null) {
            emitEffect(EditTransactionSideEffect.ShowSnackbar(R.string.choose_category_error))
            return
        }

        if (currentState.amount.replace(" ", "").toBigDecimalOrNull() == null) {
            emitEffect(EditTransactionSideEffect.ShowSnackbar(R.string.invalid_sum_format))
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }

            val account = currentState.account
            val category = currentState.selectedCategory
            val transactionDate = DateUtils.combineDateAndTimeToIso(
                currentState.date,
                currentState.time
            )

            if (account != null && transactionDate != null) {
                updateTransactionUseCase(
                    accountId = account.id,
                    categoryId = category.id,
                    amount = currentState.amount.replace(" ", ""),
                    transactionDate = transactionDate,
                    comment = currentState.comment,
                    transactionId = transactionId
                ).fold(
                    onSuccess = {
                        _uiState.update { it.copy(isSaving = false) }
                        emitEffect(EditTransactionSideEffect.NavigateBack)
                    },
                    onFailure = { error ->
                        _uiState.update { it.copy(isSaving = false) }
                        val msgRes = mapErrorToStringRes(error, ErrorContext.UPDATE_TRANSACTION)
                        emitEffect(EditTransactionSideEffect.ShowSnackbar(msgRes))
                    }
                )
            }
        }
    }

    private fun loadTransaction() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoadingTransaction = true) }

            getTransactionUseCase(transactionId).fold(
                onSuccess = { transaction ->
                    val (date, time) = DateUtils
                        .splitIsoToDateAndTime(transaction.transactionDate)
                        ?: (DateUtils.getTodayDayMonthYearFormatted() to DateUtils.getCurrentTime())

                    _uiState.update {
                        it.copy(
                            amount = transaction.amount.formatAmount(),
                            comment = transaction.comment,
                            date = date,
                            time = time,
                            selectedCategory = transaction.category,
                            isLoadingTransaction = false,
                        )
                    }

                },
                onFailure = { error ->
                    _uiState.update { it.copy(isSaving = false) }
                    val msgRes = mapErrorToStringRes(error, ErrorContext.LOAD_TRANSACTION)
                    emitEffect(EditTransactionSideEffect.ShowSnackbar(msgRes))
                }
            )
        }
    }

    private fun deleteTransaction() {
        viewModelScope.launch {
            _uiState.update { it.copy(isDeleting = true) }

            deleteTransactionUseCase(transactionId).fold(
                onSuccess = {
                    _uiState.update { it.copy(isDeleting = false) }
                    emitEffect(EditTransactionSideEffect.NavigateBack)
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isDeleting = false) }
                    val msgRes = mapErrorToStringRes(error, ErrorContext.DELETE_TRANSACTION)
                    emitEffect(EditTransactionSideEffect.ShowSnackbar(msgRes))
                }
            )
        }
    }

    private fun loadCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoadingCategories = true) }

            getCategoriesByTypeUseCase(isIncome).fold(
                onSuccess = { categories ->
                    _uiState.update {
                        it.copy(categories = categories, isLoadingCategories = false)
                    }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoadingCategories = false) }
                    val msgRes = mapErrorToStringRes(error, ErrorContext.LOAD_CATEGORIES)
                    emitEffect(EditTransactionSideEffect.ShowSnackbar(msgRes))
                }
            )
        }
    }

    private fun observeTransaction() {
        viewModelScope.launch {
            observeTransactionUseCase(transactionId).collect { resource ->
                when (resource) {
                    is Resource.Loading -> _uiState.update { it.copy(isLoadingTransaction = true) }
                    is Resource.Success -> {
                        resource.data?.let { transaction ->
                            val (date, time) = DateUtils
                                .splitIsoToDateAndTime(transaction.transactionDate)
                                ?: (DateUtils.getTodayDayMonthYearFormatted() to DateUtils.getCurrentTime())

                            _uiState.update {
                                it.copy(
                                    amount = transaction.amount.formatAmount(),
                                    comment = transaction.comment,
                                    selectedCategory = transaction.category,
                                    date = date,
                                    time = time,
                                    isLoadingTransaction = false
                                )
                            }
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoadingTransaction = false,
                            )
                        }
                    }
                }
            }
        }
    }

    private fun observeAccount() {
        viewModelScope.launch {
            observeAccountUseCase().collect { account ->
                _uiState.update { it.copy(account = account) }
            }
        }
    }

    private fun observeCategories() {
        viewModelScope.launch {
            observeCategoriesByTypeUseCase(isIncome).collect { resource ->
                when (resource) {
                    is Resource.Loading -> _uiState.update {
                        it.copy(
                            isLoadingCategories = true,
                            categories = resource.data ?: emptyList(),
                        )
                    }
                    is Resource.Success -> _uiState.update {
                        it.copy(
                            isLoadingCategories = false,
                            categories = resource.data ?: emptyList(),
                        )
                    }
                    is Resource.Error -> _uiState.update {
                        it.copy(
                            isLoadingCategories = false,
                            categories = resource.data ?: emptyList(),
                        )
                    }
                }
            }
        }
    }

    fun mapErrorToStringRes(error: Throwable?, context: ErrorContext): Int {
        return when (error) {
            is UnknownHostException -> R.string.no_network_connection
            is SocketTimeoutException -> R.string.response_timeout
            is HttpException -> when (context) {
                ErrorContext.UPDATE_TRANSACTION -> when (error.code()) {
                    400 -> R.string.incorrect_data_or_id
                    401 -> R.string.unauthorised_access
                    404 -> R.string.trs_account_ctg_not_found
                    500 -> R.string.internal_server_error
                    else -> R.string.unknown_error
                }
                ErrorContext.DELETE_TRANSACTION -> when (error.code()) {
                    400 -> R.string.incorrect_data_or_id
                    401 -> R.string.unauthorised_access
                    404 -> R.string.transaction_not_found
                    500 -> R.string.internal_server_error
                    else -> R.string.unknown_error
                }
                ErrorContext.LOAD_TRANSACTION -> when (error.code()) {
                    400 -> R.string.incorrect_id
                    401 -> R.string.unauthorised_access
                    404 -> R.string.transaction_not_found
                    500 -> R.string.internal_server_error
                    else -> R.string.unknown_error
                }
                ErrorContext.LOAD_CATEGORIES -> when (error.code()) {
                    401 -> R.string.unauthorised_access
                    500 -> R.string.internal_server_error
                    else -> R.string.failed_to_load_categories
                }
            }
            else -> when (context) {
                ErrorContext.UPDATE_TRANSACTION -> R.string.failed_to_save_transaction
                ErrorContext.DELETE_TRANSACTION -> R.string.unknown_error
                ErrorContext.LOAD_TRANSACTION -> R.string.failed_to_load_data
                ErrorContext.LOAD_CATEGORIES -> R.string.failed_to_load_categories
            }
        }
    }
}

enum class ErrorContext {
    UPDATE_TRANSACTION,
    DELETE_TRANSACTION,
    LOAD_TRANSACTION,
    LOAD_CATEGORIES
}