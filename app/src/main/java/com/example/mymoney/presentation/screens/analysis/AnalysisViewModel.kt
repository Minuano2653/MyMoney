package com.example.mymoney.presentation.screens.analysis

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.mymoney.domain.usecase.GetAnalysisUseCase
import com.example.mymoney.domain.usecase.GetCurrentAccountUseCase
import com.example.mymoney.presentation.base.viewmodel.BaseViewModel
import com.example.mymoney.presentation.navigation.Analysis
import com.example.mymoney.presentation.screens.history.HistoryEvent
import com.example.mymoney.presentation.screens.history.HistorySideEffect
import com.example.mymoney.utils.NetworkMonitor
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class AnalysisViewModel @AssistedInject constructor(
    private val getCurrentAccountUseCase: GetCurrentAccountUseCase,
    private val getAnalysisUseCase: GetAnalysisUseCase,
    @Assisted savedStateHandle: SavedStateHandle,
    networkMonitor: NetworkMonitor
): BaseViewModel<AnalysisUiState, AnalysisEvent, AnalysisSideEffect>(
    networkMonitor,
    AnalysisUiState()
) {
    private val isIncome: Boolean = savedStateHandle.toRoute<Analysis>().isIncome

    private var loadAnalysisJob: Job? = null

    init {
        handleEvent(AnalysisEvent.LoadAnalysis)
        observeAccountChanges()
    }

    override fun handleEvent(event: AnalysisEvent) {
        when(event) {
            is AnalysisEvent.LoadAnalysis -> {
                loadAnalysis()
            }
            is AnalysisEvent.OnBackPressed -> {
                cancelLoadingAndNavigateBack()
            }
            is AnalysisEvent.OnStartDateClicked -> {
                _uiState.update { it.copy(showStartDatePicker = !it.showStartDatePicker) }
            }
            is AnalysisEvent.OnStartDateSelected -> {
                _uiState.update { it.copy(startDate = event.date) }
                handleEvent(AnalysisEvent.LoadAnalysis)
            }
            is AnalysisEvent.OnEndDateClicked -> {
                _uiState.update { it.copy(showEndDatePicker = !it.showEndDatePicker) }
            }
            is AnalysisEvent.OnEndDateSelected -> {
                _uiState.update { it.copy(endDate = event.date) }
                handleEvent(AnalysisEvent.LoadAnalysis)
            }
        }
    }

    private fun loadAnalysis() {
        loadAnalysisJob?.cancel()
        loadAnalysisJob = viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true) }

            val result = getAnalysisUseCase(
                isIncome = isIncome,
                startDate = _uiState.value.startDate,
                endDate = _uiState.value.endDate
            )

            result.onSuccess { (totalAmount, analysisList) ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        total = totalAmount,
                        categoryAnalysis = analysisList
                    )
                }
            }.onFailure { error ->
                _uiState.update { it.copy(isLoading = false) }
                val message = when (error) {
                    is UnknownHostException -> "Нет подключения к интернету"
                    is SocketTimeoutException -> "Превышено время ожидания ответа"
                    is HttpException -> when (error.code()) {
                        400 -> "Неверный формат ID счета или некорректный формат дат"
                        401 -> "Неавторизованный доступ"
                        500 -> "Внутренняя ошибка сервера"
                        else -> "Ошибка сервера (${error.code()})"
                    }
                    else -> "Не удалось загрузить данные"
                }
                _sideEffect.emit(AnalysisSideEffect.ShowError(message))
            }
        }
    }

    private fun cancelLoadingAndNavigateBack() {
        loadAnalysisJob?.cancel()
        emitEffect(AnalysisSideEffect.NavigateBack)
    }

    private fun observeAccountChanges() {
        viewModelScope.launch {
            getCurrentAccountUseCase().collectLatest { account ->
                account?.let {
                    _uiState.update { currentState ->
                        currentState.copy(currency = it.currency)
                    }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        loadAnalysisJob?.cancel()
    }
}