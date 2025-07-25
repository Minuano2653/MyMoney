package com.example.core.domain.usecase

import com.example.core.domain.entity.CategoryAnalysis
import com.example.core.domain.entity.Resource
import com.example.core.domain.repository.TransactionsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import java.math.BigDecimal
import javax.inject.Inject

class ObserveAnalysisUseCase @Inject constructor(
    private val repository: TransactionsRepository,
    private val getAccountIdUseCase: GetAccountIdUseCase
) {
    suspend operator fun invoke(
        isIncome: Boolean,
        startDate: String,
        endDate: String
    ): Flow<Resource<Pair<BigDecimal, List<CategoryAnalysis>>>> = flow {
        getAccountIdUseCase().fold(
            onSuccess = { accountId ->
                emitAll(
                    repository.observeCategoryAnalysis(
                        accountId = accountId,
                        isIncome = isIncome,
                        startDate = startDate,
                        endDate = endDate
                    )
                )
            },
            onFailure = { error ->
                emit(Resource.Error(error, Pair(BigDecimal.ZERO, emptyList())))
            }
        )
    }
}