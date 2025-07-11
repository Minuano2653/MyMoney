package com.example.mymoney.presentation.add_transaction

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.mymoney.domain.usecase.CreateTransactionUseCase
import com.example.mymoney.domain.usecase.GetCategoriesByTypeUseCase
import com.example.mymoney.domain.usecase.GetCurrentAccountUseCase
import com.example.mymoney.presentation.base.viewmodel.BaseViewModel
import com.example.mymoney.presentation.navigation.TransactionDetail
import com.example.mymoney.utils.DateUtils.combineDateAndTimeToIso
import com.example.mymoney.utils.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class TransactionDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    networkMonitor: NetworkMonitor,
    private val getCurrentAccountUseCase: GetCurrentAccountUseCase,
    private val getCategoriesByTypeUseCase: GetCategoriesByTypeUseCase,
    private val createTransactionUseCase: CreateTransactionUseCase,
) : BaseViewModel<TransactionDetailUiState, TransactionDetailEvent, TransactionDetailSideEffect>(
    networkMonitor,
    TransactionDetailUiState()
) {
    private val isIncome: Boolean = savedStateHandle.toRoute<TransactionDetail>().isIncome

    init {
        observeAccount()
        loadCategories()
        _uiState.update { it.copy(isIncome = isIncome) }
    }

    override fun handleEvent(event: TransactionDetailEvent) {
        when (event) {
            is TransactionDetailEvent.ShowAmountDialog -> {
                _uiState.update { it.copy(showAmountDialog = true) }
            }

            is TransactionDetailEvent.DismissAmountDialog -> {
                _uiState.update { it.copy(showAmountDialog = false) }
            }

            is TransactionDetailEvent.OnAmountChanged -> {
                _uiState.update { it.copy(amount = event.amount) }
            }

            is TransactionDetailEvent.CancelChangesClicked -> {
                emitEffect(TransactionDetailSideEffect.NavigateBack)
            }

            is TransactionDetailEvent.ShowCategorySheet -> {
                _uiState.update { it.copy(showCategorySheet = true) }
            }

            is TransactionDetailEvent.DismissCategorySheet -> {
                _uiState.update { it.copy(showCategorySheet = false) }
            }

            is TransactionDetailEvent.OnCategorySelected -> {
                _uiState.update {
                    it.copy(selectedCategory = event.selectedCategory)
                }
            }

            is TransactionDetailEvent.OnCommentChanged -> {
                _uiState.update { it.copy(comment = event.comment) }
            }

            is TransactionDetailEvent.ShowDatePicker -> {
                _uiState.update { it.copy(showDatePicker = true) }
            }

            is TransactionDetailEvent.DismissDatePicker -> {
                _uiState.update { it.copy(showDatePicker = false) }
            }

            is TransactionDetailEvent.OnDateSelected -> {
                _uiState.update { it.copy(date = event.date) }
            }

            is TransactionDetailEvent.SaveChangesClicked -> {
                saveTransaction()
            }

            is TransactionDetailEvent.ShowTimePicker -> {
                _uiState.update { it.copy(showTimePicker = true) }
            }

            is TransactionDetailEvent.DismissTimePicker -> {
                _uiState.update { it.copy(showTimePicker = false) }
            }

            is TransactionDetailEvent.OnTimeSelected -> {
                _uiState.update { it.copy(time = event.time) }
            }

            is TransactionDetailEvent.LoadCategories -> {
                loadCategories()
            }
        }
    }

    private fun saveTransaction() {
        val currentState = _uiState.value

        if (currentState.selectedCategory == null) {
            emitEffect(TransactionDetailSideEffect.ShowSnackbar("Выберите категорию"))
            return
        }
        if (currentState.amount.toBigDecimalOrNull() == null) {
            emitEffect(TransactionDetailSideEffect.ShowSnackbar("Неверный формат суммы"))
            return
        }

        viewModelScope.launch {
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
                        emitEffect(TransactionDetailSideEffect.NavigateBack)
                    },
                    onFailure = { error ->
                        _uiState.update { it.copy(isSaving = false) }
                        val message = when (error) {
                            is UnknownHostException -> "Нет подключения к интернету"
                            is SocketTimeoutException -> "Превышено время ожидания ответа"
                            is HttpException -> when (error.code()) {
                                400 -> "Некорректные данные"
                                401 -> "Неавторизованный доступ"
                                404 -> "Счет или категория не найдены"
                                500 -> "Внутренняя ошибка сервера"
                                else -> "Ошибка сервера (${error.code()})"
                            }
                            else -> "Не удалось сохранить транзакцию"
                        }
                        emitEffect(TransactionDetailSideEffect.ShowSnackbar(message))
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
                            isLoadingCategories = false
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoadingCategories = false) }
                    val message = when (error) {
                        is UnknownHostException -> "Нет подключения к интернету"
                        is SocketTimeoutException -> "Превышено время ожидания ответа"
                        else -> "Ошибка загрузки категорий"
                    }
                    emitEffect(TransactionDetailSideEffect.ShowSnackbar(message))
                }
            )
        }
    }

    private fun observeAccount() {
        viewModelScope.launch {
            getCurrentAccountUseCase().collectLatest { account ->
                account?.let {
                    _uiState.update { currentState ->
                        currentState.copy(account = it)
                    }
                }
            }
        }
    }
}