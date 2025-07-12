package com.example.mymoney.presentation.screens.edit_transaction

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.mymoney.domain.usecase.DeleteTransactionUseCase
import com.example.mymoney.domain.usecase.GetCategoriesByTypeUseCase
import com.example.mymoney.domain.usecase.GetCurrentAccountUseCase
import com.example.mymoney.domain.usecase.GetTransactionUseCase
import com.example.mymoney.domain.usecase.UpdateTransactionUseCase
import com.example.mymoney.presentation.base.viewmodel.BaseViewModel
import com.example.mymoney.utils.DateUtils
import com.example.mymoney.utils.NetworkMonitor
import com.example.mymoney.utils.formatAmount
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class EditTransactionViewModel @Inject constructor(
    networkMonitor: NetworkMonitor,
    private val getCurrentAccountUseCase: GetCurrentAccountUseCase,
    private val getCategoriesByTypeUseCase: GetCategoriesByTypeUseCase,
    private val updateTransactionUseCase: UpdateTransactionUseCase,
    private val getTransactionUseCase: GetTransactionUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
) : BaseViewModel<EditTransactionUiState, EditTransactionEvent, EditTransactionSideEffect>(
    networkMonitor,
    EditTransactionUiState()
) {
    private var isIncome: Boolean? = null
    private var transactionId: Int? = null
    init {
        observeAccount()
    }

    override fun handleEvent(event: EditTransactionEvent) {
        when (event) {
            is EditTransactionEvent.ShowAmountDialog -> {
                _uiState.update { it.copy(showAmountDialog = true) }
            }

            is EditTransactionEvent.DismissAmountDialog -> {
                _uiState.update { it.copy(showAmountDialog = false) }
            }

            is EditTransactionEvent.OnAmountChanged -> {
                _uiState.update { it.copy(amount = event.amount) }
            }

            is EditTransactionEvent.CancelChanges -> {
                emitEffect(EditTransactionSideEffect.NavigateBack)
            }

            is EditTransactionEvent.ShowCategorySheet -> {
                _uiState.update { it.copy(showCategorySheet = true) }
            }

            is EditTransactionEvent.DismissCategorySheet -> {
                _uiState.update { it.copy(showCategorySheet = false) }
            }

            is EditTransactionEvent.OnCategorySelected -> {
                _uiState.update {
                    it.copy(selectedCategory = event.selectedCategory)
                }
            }

            is EditTransactionEvent.OnCommentChanged -> {
                _uiState.update { it.copy(comment = event.comment) }
            }

            is EditTransactionEvent.ShowDatePicker -> {
                _uiState.update { it.copy(showDatePicker = true) }
            }

            is EditTransactionEvent.DismissDatePicker -> {
                _uiState.update { it.copy(showDatePicker = false) }
            }

            is EditTransactionEvent.OnDateSelected -> {
                _uiState.update { it.copy(date = event.date) }
            }

            is EditTransactionEvent.ShowTimePicker -> {
                _uiState.update { it.copy(showTimePicker = true) }
            }

            is EditTransactionEvent.DismissTimePicker -> {
                _uiState.update { it.copy(showTimePicker = false) }
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

            is EditTransactionEvent.SetInitData -> {
                isIncome = event.isIncome
                _uiState.update { it.copy(event.isIncome) }
                transactionId = event.transactionId
                loadCategories()
                loadTransaction()
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
                Log.d("UPDATE_TRANSACTION", currentState.toString())
                updateTransactionUseCase(
                    accountId = account.id,
                    categoryId = category.id,
                    amount = currentState.amount.replace(" ", ""),
                    transactionDate = transactionDate,
                    comment = currentState.comment,
                    transactionId = transactionId!!
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
                getTransactionUseCase(transactionId!!).fold(
                    onSuccess = { transaction ->
                        val (date, time) = DateUtils
                            .splitIsoToDateAndTime(transaction.transactionDate)
                            ?: (DateUtils.getTodayDayMonthYearFormatted() to DateUtils.getCurrentTime())
                        Log.d("TRANSACTION_DETAIL_VIEW_MODEL", "$date $time")
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
                        Log.d("TRANSACTION_VIEW_MODEL", error.message.toString())

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
            getCategoriesByTypeUseCase(isIncome!!).fold(
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