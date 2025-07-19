package com.example.mymoney.presentation.screens.analysis

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.mymoney.data.utils.Resource
import com.example.mymoney.domain.usecase.GetAnalysisUseCase
import com.example.mymoney.domain.usecase.ObserveAccountUseCase
import com.example.mymoney.domain.usecase.ObserveAnalysisUseCase
import com.example.mymoney.presentation.base.viewmodel.BaseViewModel
import com.example.mymoney.presentation.navigation.Analysis
import com.example.mymoney.utils.NetworkMonitor
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.math.BigDecimal
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class AnalysisViewModel @AssistedInject constructor(
    private val getAnalysisUseCase: GetAnalysisUseCase,
    @Assisted savedStateHandle: SavedStateHandle,
    observeCategoryAnalysisUseCase: ObserveAnalysisUseCase,
    observeAccountUseCase: ObserveAccountUseCase,
    networkMonitor: NetworkMonitor
) : BaseViewModel<AnalysisUiState, AnalysisEvent, AnalysisSideEffect>(
    networkMonitor,
    AnalysisUiState()
) {
    private val isIncome: Boolean = savedStateHandle.toRoute<Analysis>().isIncome

    private var loadAnalysisJob: Job? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    override val uiState: StateFlow<AnalysisUiState> =
        combine(
            _uiState
                .map { state -> state.startDate to state.endDate }
                .distinctUntilChanged()
                .flatMapLatest { (startDate, endDate) ->
                    observeCategoryAnalysisUseCase(
                        isIncome = isIncome,
                        startDate = startDate,
                        endDate = endDate
                    )
                },
            observeAccountUseCase(),
            _uiState
        ) { analysisResource, account, currentState ->
            when (analysisResource) {
                is Resource.Loading -> currentState.copy(
                    isLoading = true,
                    total = analysisResource.data?.first ?: BigDecimal.ZERO,
                    categoryAnalysis = analysisResource.data?.second ?: emptyList(),
                    currency = account?.currency ?: currentState.currency,
                )

                is Resource.Success -> {
                    val (total, categoryAnalysis) = analysisResource.data
                        ?: (BigDecimal.ZERO to emptyList())
                    currentState.copy(
                        isLoading = false,
                        total = total,
                        categoryAnalysis = categoryAnalysis,
                        currency = account?.currency ?: currentState.currency
                    )
                }

                is Resource.Error -> {
                    val message = mapErrorToMessage(analysisResource.error)
                    emitEffect(AnalysisSideEffect.ShowError(message))
                    currentState.copy(
                        isLoading = false,
                        total = analysisResource.data?.first ?: BigDecimal.ZERO,
                        categoryAnalysis = analysisResource.data?.second ?: emptyList(),
                        currency = account?.currency ?: currentState.currency
                    )
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AnalysisUiState()
        )

    override fun handleEvent(event: AnalysisEvent) {
        when (event) {
            is AnalysisEvent.LoadAnalysis -> {
                loadAnalysis()
            }

            is AnalysisEvent.OnBackPressed -> {
                cancelLoadingAndNavigateBack()
            }

            is AnalysisEvent.OnStartDateSelected -> {
                _uiState.update { it.copy(startDate = event.date) }
                handleEvent(AnalysisEvent.LoadAnalysis)
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
                val message = mapErrorToMessage(error)
                _sideEffect.emit(AnalysisSideEffect.ShowError(message))
            }
        }
    }

    private fun mapErrorToMessage(error: Throwable?): String {
        return when (error) {
            is UnknownHostException -> "Нет подключения к интернету"
            is SocketTimeoutException -> "Превышено время ожидания ответа"
            is HttpException -> when (error.code()) {
                400 -> "Неверный формат ID счета или некорректный формат дат"
                401 -> "Неавторизованный доступ"
                500 -> "Внутренняя ошибка сервера"
                else -> "Ошибка сервера (${error.code()})"
            }

            else -> {
                "Не удалось загрузить данные"
            }
        }
    }

    private fun cancelLoadingAndNavigateBack() {
        loadAnalysisJob?.cancel()
        emitEffect(AnalysisSideEffect.NavigateBack)
    }

    override fun onCleared() {
        super.onCleared()
        loadAnalysisJob?.cancel()
    }
}