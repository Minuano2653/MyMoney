package com.example.mymoney.presentation.screens.account

import androidx.lifecycle.viewModelScope
import com.example.core.common.utils.DateUtils
import com.example.core.domain.entity.Resource
import com.example.core.domain.usecase.GetAccountUseCase
import com.example.core.domain.usecase.ObserveAccountUseCase
import com.example.core.domain.usecase.ObserveTransactionsByPeriodUseCase
import com.example.core.ui.charts.bar.ChartDataPoint
import com.example.core.ui.viewmodel.BaseViewModel
import com.example.mymoney.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class AccountViewModel @Inject constructor(
    private val observeAccountUseCase: ObserveAccountUseCase,
    private val observeTransactionsByPeriodUseCase: ObserveTransactionsByPeriodUseCase
) : BaseViewModel<AccountUiState, AccountEvent, AccountSideEffect>(
    AccountUiState()
) {
    init {
        observeAccountChanges()
        observeTransactions()
    }

    override fun handleEvent(event: AccountEvent) {
        when (event) {
            AccountEvent.LoadAccount -> {
            }

            AccountEvent.OnEditClicked -> {
                emitEffect(AccountSideEffect.NavigateToEditAccount(uiState.value.accountId))
            }
        }
    }

    private fun observeAccountChanges() {
        viewModelScope.launch {
            observeAccountUseCase().collectLatest { account ->
                account?.let {
                    _uiState.update { currentState ->
                        currentState.copy(
                            accountId = it.id,
                            name = it.name,
                            balance = it.balance,
                            currency = it.currency
                        )
                    }
                }
            }
        }
    }

    private fun observeTransactions() {
        val days = 30
        val dateList = DateUtils.getDateListFromToday(days)
        val (startDate, endDate) = DateUtils.getStartAndEndDateRange(days)

        viewModelScope.launch {
            observeTransactionsByPeriodUseCase(
                startDate = startDate,
                endDate = endDate
            ).collectLatest { resource ->
                val chartItems = resource.data?.let { transactions ->
                    val grouped = transactions.groupBy {
                        DateUtils.formatIsoToDayMonth(it.transactionDate)
                    }

                    dateList.map { date ->
                        val dailyTransactions = grouped[date].orEmpty()
                        val income = dailyTransactions
                            .filter { it.category.isIncome }
                            .sumOf { it.amount }
                        val expense = dailyTransactions
                            .filter { !it.category.isIncome }
                            .sumOf { it.amount }
                        ChartDataPoint(
                            value = (income - expense).toFloat(),
                            date = date
                        )
                    }
                } ?: emptyList()

                when (resource) {
                    is Resource.Loading -> _uiState.update { it.copy(chartItems = chartItems) }
                    is Resource.Success -> _uiState.update { it.copy(chartItems = chartItems) }
                    is Resource.Error -> {
                        val message = mapErrorToMessage(resource.error)
                        _uiState.update { it.copy(chartItems = chartItems) }
                        emitEffect(AccountSideEffect.ShowError(message))
                    }
                }
            }
        }
    }

    private fun mapErrorToMessage(error: Throwable?): Int {
        return when (error) {
            is UnknownHostException ->  R.string.no_network_connection
            is SocketTimeoutException -> R.string.response_timeout
            is HttpException -> when (error.code()) {
                400 -> R.string.incorrect_id_or_date
                401 -> R.string.unauthorised_access
                500 -> R.string.internal_server_error
                else -> R.string.unknown_error
            }
            else -> {
                R.string.failed_to_load_data
            }
        }
    }
}
