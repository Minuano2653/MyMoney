package com.example.mymoney.data.remote.datasource

import com.example.mymoney.data.remote.api.CategoriesApi
import com.example.mymoney.data.remote.dto.CategoryDto
import javax.inject.Inject

/**
 * Источник удалённых данных для получения статей доходов и расходов.
 *
 * @param api API-интерфейс для выполнения сетевых запросов к статьям.
 */
class CategoriesRemoteDataSource @Inject constructor(
    private val api: CategoriesApi
)  {
    suspend fun getAllCategories(): List<CategoryDto> {
        return api.getAllCategories()
    }
}
