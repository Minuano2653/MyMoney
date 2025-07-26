package com.example.core.domain.usecase

import com.example.core.domain.entity.CategoryAnalysis
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

class GetAnalysisUseCase @Inject constructor(
    private val getTransactionsByTypeAndPeriodUseCase: GetTransactionsByTypeAndPeriodUseCase
) {
    suspend operator fun invoke(
        isIncome: Boolean,
        startDate: String,
        endDate: String
    ): Result<Pair<BigDecimal, List<CategoryAnalysis>>> {
        return getTransactionsByTypeAndPeriodUseCase(isIncome, startDate, endDate).map { transactions ->
            val totalAmount = transactions.fold(BigDecimal.ZERO) { acc, tx -> acc + tx.amount }

            val grouped = transactions.groupBy { it.category }

            val analysisList = grouped.map { (category, txList) ->
                val sum = txList.fold(BigDecimal.ZERO) { acc, tx -> acc + tx.amount }
                val percentage = if (totalAmount > BigDecimal.ZERO) {
                    (sum * BigDecimal(100) / totalAmount)
                        .setScale(1, RoundingMode.HALF_UP)

                } else {
                    BigDecimal.ZERO
                }

                CategoryAnalysis(
                    categoryId = category.id,
                    categoryName = category.name,
                    categoryEmoji = category.emoji,
                    isIncome = category.isIncome,
                    percentage = percentage,
                    totalAmount = sum
                )
            }.sortedByDescending {
                it.percentage
            }
            totalAmount to analysisList
        }
    }
}