package com.example.mymoney.data.remote.api

import com.example.mymoney.data.remote.dto.CategoryDto
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * API-интерфейс для получения статей доходов и расходов.
 */
interface CategoriesApi {
    @GET("categories")
    suspend fun getAllCategories(): List<CategoryDto>

    @GET("categories/type/{isIncome}")
    suspend fun getCategoriesByType(
        @Path("isIncome") isIncome: Boolean
    ): List<CategoryDto>
}
