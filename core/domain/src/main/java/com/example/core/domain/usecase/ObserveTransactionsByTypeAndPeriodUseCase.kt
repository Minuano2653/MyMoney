package com.example.core.domain.usecase

import com.example.core.domain.entity.Resource
import com.example.core.domain.entity.Transaction
import com.example.core.domain.repository.TransactionsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ObserveTransactionsByTypeAndPeriodUseCase @Inject constructor(
    private val repository: TransactionsRepository,
    private val getAccountIdUseCase: GetAccountIdUseCase
) {
    operator fun invoke(
        isIncome: Boolean,
        startDate: String,
        endDate: String
    ): Flow<Resource<List<Transaction>>> = flow {
        getAccountIdUseCase().fold(
            onSuccess = { accountId ->
                emitAll(
                    repository.observeTransactionsByTypeAndPeriod(
                        accountId = accountId,
                        startDate = startDate,
                        endDate = endDate,
                        isIncome = isIncome
                    )
                )
            },
            onFailure = { error ->
                emit(Resource.Error(error, emptyList()))
            }
        )
    }
}