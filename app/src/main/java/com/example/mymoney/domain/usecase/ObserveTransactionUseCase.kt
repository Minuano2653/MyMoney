package com.example.mymoney.domain.usecase

import com.example.mymoney.data.utils.Resource
import com.example.mymoney.domain.entity.Transaction
import com.example.mymoney.domain.repository.TransactionsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveTransactionUseCase @Inject constructor(
    private val transactionsRepository: TransactionsRepository
) {
    operator fun invoke(transactionId: Int): Flow<Resource<Transaction?>> {
        return transactionsRepository.observeTransaction(transactionId)
    }
}
