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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class AddTransactionViewModel @Inject constructor(
    //savedStateHandle: SavedStateHandle,
    networkMonitor: NetworkMonitor,
    private val getCurrentAccountUseCase: GetCurrentAccountUseCase,
    private val getCategoriesByTypeUseCase: GetCategoriesByTypeUseCase,
    private val createTransactionUseCase: CreateTransactionUseCase,
) : BaseViewModel<AddTransactionUiState, AddTransactionEvent, AddTransactionSideEffect>(
    networkMonitor,
    AddTransactionUiState()
) {
    //private val isIncome: Boolean = savedStateHandle.toRoute<TransactionDetail>().isIncome

    init {
        observeAccount()
        //_uiState.update { it.copy(isIncome = isIncome) }
    }

    override fun handleEvent(event: AddTransactionEvent) {
        when (event) {
            is AddTransactionEvent.ShowAmountDialog -> {
                _uiState.update { it.copy(showAmountDialog = true) }
            }

            is AddTransactionEvent.DismissAmountDialog -> {
                _uiState.update { it.copy(showAmountDialog = false) }
            }

            is AddTransactionEvent.OnAmountChanged -> {
                _uiState.update { it.copy(amount = event.amount) }
            }

            is AddTransactionEvent.CancelChangesClicked -> {
                emitEffect(AddTransactionSideEffect.NavigateBack)
            }

            is AddTransactionEvent.ShowCategorySheet -> {
                _uiState.update { it.copy(showCategorySheet = true) }
            }

            is AddTransactionEvent.DismissCategorySheet -> {
                _uiState.update { it.copy(showCategorySheet = false) }
            }

            is AddTransactionEvent.OnCategorySelected -> {
                _uiState.update {
                    it.copy(selectedCategory = event.selectedCategory)
                }
            }

            is AddTransactionEvent.OnCommentChanged -> {
                _uiState.update { it.copy(comment = event.comment) }
            }

            is AddTransactionEvent.ShowDatePicker -> {
                _uiState.update { it.copy(showDatePicker = true) }
            }

            is AddTransactionEvent.DismissDatePicker -> {
                _uiState.update { it.copy(showDatePicker = false) }
            }

            is AddTransactionEvent.OnDateSelected -> {
                _uiState.update { it.copy(date = event.date) }
            }

            is AddTransactionEvent.SaveChangesClicked -> {
                saveTransaction()
            }

            is AddTransactionEvent.ShowTimePicker -> {
                _uiState.update { it.copy(showTimePicker = true) }
            }

            is AddTransactionEvent.DismissTimePicker -> {
                _uiState.update { it.copy(showTimePicker = false) }
            }

            is AddTransactionEvent.OnTimeSelected -> {
                _uiState.update { it.copy(time = event.time) }
            }

            is AddTransactionEvent.LoadCategories -> {
                loadCategories(event.isIncome)
            }
        }
    }

    private fun saveTransaction() {
        val currentState = _uiState.value

        if (currentState.selectedCategory == null) {
            emitEffect(AddTransactionSideEffect.ShowSnackbar("Выберите категорию"))
            return
        }
        if (currentState.amount.toBigDecimalOrNull() == null) {
            emitEffect(AddTransactionSideEffect.ShowSnackbar("Неверный формат суммы"))
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
                        emitEffect(AddTransactionSideEffect.NavigateBack)
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
                        emitEffect(AddTransactionSideEffect.ShowSnackbar(message))
                    }
                )
            }
        }
    }

    private fun loadCategories(isIncome: Boolean) {
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
                    emitEffect(AddTransactionSideEffect.ShowSnackbar(message))
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