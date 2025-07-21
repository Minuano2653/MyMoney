package com.example.mymoney.presentation.screens.edit_transaction

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.mymoney.domain.usecase.DeleteTransactionUseCase
import com.example.mymoney.domain.usecase.GetCategoriesByTypeUseCase
import com.example.mymoney.domain.usecase.ObserveAccountUseCase
import com.example.mymoney.domain.usecase.GetTransactionUseCase
import com.example.mymoney.domain.usecase.UpdateTransactionUseCase
import com.example.mymoney.presentation.base.viewmodel.BaseViewModel
import com.example.mymoney.presentation.navigation.EditTransaction
import com.example.mymoney.utils.DateUtils
import com.example.mymoney.utils.NetworkMonitor
import com.example.mymoney.utils.formatAmount
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class EditTransactionViewModel @AssistedInject constructor(
    networkMonitor: NetworkMonitor,
    private val observeAccountUseCase: ObserveAccountUseCase,
    private val getCategoriesByTypeUseCase: GetCategoriesByTypeUseCase,
    private val updateTransactionUseCase: UpdateTransactionUseCase,
    private val getTransactionUseCase: GetTransactionUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    @Assisted private val savedStateHandle: SavedStateHandle
) : BaseViewModel<EditTransactionUiState, EditTransactionEvent, EditTransactionSideEffect>(
    networkMonitor,
    EditTransactionUiState()
) {
    private val isIncome: Boolean = savedStateHandle.toRoute<EditTransaction>().isIncome
    private val transactionId: Int? = savedStateHandle.toRoute<EditTransaction>().transactionId

    init {
        observeAccount()
        loadCategories()
        loadTransaction()
        _uiState.update { it.copy(isIncome = isIncome) }
    }

    override fun handleEvent(event: EditTransactionEvent) {
        when (event) {
            is EditTransactionEvent.OnAmountChanged -> {
                _uiState.update { it.copy(amount = event.amount) }
            }
            is EditTransactionEvent.CancelChanges -> {
                emitEffect(EditTransactionSideEffect.NavigateBack)
            }
            is EditTransactionEvent.OnCategorySelected -> {
                _uiState.update {
                    it.copy(selectedCategory = event.selectedCategory)
                }
            }
            is EditTransactionEvent.OnCommentChanged -> {
                _uiState.update { it.copy(comment = event.comment) }
            }
            is EditTransactionEvent.OnDateSelected -> {
                _uiState.update { it.copy(date = event.date) }
            }
            is EditTransactionEvent.OnTimeSelected -> {
                _uiState.update { it.copy(time = event.time) }
            }
            is EditTransactionEvent.LoadCategories -> {
                loadCategories()
            }
            is EditTransactionEvent.LoadTransaction -> {
                loadTransaction()
            }
            is EditTransactionEvent.DeleteTransaction -> {
                deleteTransaction()
            }
            is EditTransactionEvent.UpdateTransaction -> {
                updateTransaction()
            }
        }
    }

    private fun updateTransaction() {
        val currentState = _uiState.value

        if (currentState.selectedCategory == null) {
            emitEffect(EditTransactionSideEffect.ShowSnackbar("Выберите категорию"))
            return
        }
        if (currentState.amount.replace(" ", "").toBigDecimalOrNull() == null) {
            emitEffect(EditTransactionSideEffect.ShowSnackbar("Неверный формат суммы"))
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null) }

            val account = currentState.account
            val category = currentState.selectedCategory
            val transactionDate = DateUtils.combineDateAndTimeToIso(
                currentState.date,
                currentState.time
            )

            if (account != null && transactionDate != null && transactionId != null) {
                updateTransactionUseCase(
                    accountId = account.id,
                    categoryId = category.id,
                    amount = currentState.amount.replace(" ", ""),
                    transactionDate = transactionDate,
                    comment = currentState.comment,
                    transactionId = transactionId
                ).fold(
                    onSuccess = { createdTransaction ->
                        _uiState.update { it.copy(isSaving = false) }
                        emitEffect(EditTransactionSideEffect.NavigateBack)
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
                        emitEffect(EditTransactionSideEffect.ShowSnackbar(message))
                    }
                )
            }
        }
    }

    private fun loadTransaction() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoadingTransaction = true) }

            transactionId?.let {
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
                        val message = when (error) {
                            is UnknownHostException -> "Нет подключения к интернету"
                            is SocketTimeoutException -> "Превышено время ожидания ответа"
                            is HttpException -> when (error.code()) {
                                400 -> "Неверный формат ID"
                                401 -> "Неавторизованный доступ"
                                404 -> "Транзакция не найдена"
                                500 -> "Внутренняя ошибка сервера"
                                else -> "Ошибка сервера (${error.code()})"
                            }
                            else -> "Не удалось сохранить транзакцию"
                        }
                        emitEffect(EditTransactionSideEffect.ShowSnackbar(message))
                    }
                )
            }
        }
    }

    private fun deleteTransaction() {
        viewModelScope.launch {
            _uiState.update { it.copy(isDeleting = true) }

            transactionId?.let { id ->
                deleteTransactionUseCase(id).fold(
                    onSuccess = {
                        _uiState.update { it.copy(isDeleting = false) }
                        emitEffect(EditTransactionSideEffect.NavigateBack)
                    },
                    onFailure = { error ->
                        _uiState.update { it.copy(isDeleting = false) }
                        val message = when (error) {
                            is UnknownHostException -> "Нет подключения к интернету"
                            is SocketTimeoutException -> "Превышено время ожидания ответа"
                            is HttpException -> when (error.code()) {
                                204 -> "Транзакция удалена"
                                400 -> "Неверный формат ID"
                                401 -> "Неавторизованный доступ"
                                404 -> "Транзакция не найдена"
                                500 -> "Внутренняя ошибка сервера"
                                else -> "Ошибка сервера (${error.code()})"
                            }
                            else -> "Не удалось удалить транзакцию"
                        }
                        emitEffect(EditTransactionSideEffect.ShowSnackbar(message))
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
                    emitEffect(EditTransactionSideEffect.ShowSnackbar(message))
                }
            )
        }
    }

    private fun observeAccount() {
        viewModelScope.launch {
            observeAccountUseCase().collectLatest { account ->
                account?.let {
                    _uiState.update { currentState ->
                        currentState.copy(account = it)
                    }
                }
            }
        }
    }
}