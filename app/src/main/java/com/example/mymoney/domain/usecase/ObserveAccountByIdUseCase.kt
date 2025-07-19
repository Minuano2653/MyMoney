package com.example.mymoney.domain.usecase

import com.example.mymoney.data.utils.Resource
import com.example.mymoney.data.utils.networkBoundResource
import com.example.mymoney.domain.entity.Account
import com.example.mymoney.domain.repository.AccountsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveAccountByIdUseCase @Inject constructor(
    private val accountsRepository: AccountsRepository
) {
    operator fun invoke(accountId: Int): Flow<Resource<Account?>> {
        return networkBoundResource(
            query = { accountsRepository.observeAccountById(accountId) },
            fetch = { accountsRepository.getAccountById(accountId).getOrThrow() },
            saveFetchResult = { account ->
            },
            shouldFetch = { localAccount ->
                true
            }
        )
    }
}