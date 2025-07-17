package com.example.mymoney.domain.usecase

import com.example.mymoney.presentation.screens.analysis.model.CategoryAnalysis
import com.example.mymoney.utils.formatAmount
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

class GetAnalysisUseCase @Inject constructor(
    private val getTransactionsByPeriodUseCase: GetTransactionsByPeriodUseCase
) {
    suspend operator fun invoke(
        isIncome: Boolean,
        startDate: String,
        endDate: String
    ): Result<Pair<BigDecimal, List<CategoryAnalysis>>> {
        return getTransactionsByPeriodUseCase(isIncome, startDate, endDate).map { transactions ->
            val totalAmount = transactions.fold(BigDecimal.ZERO) { acc, tx -> acc + tx.amount }

            val grouped = transactions.groupBy { it.category }

            val analysisList = grouped.map { (category, txList) ->
                val sum = txList.fold(BigDecimal.ZERO) { acc, tx -> acc + tx.amount }
                val percentage = if (totalAmount > BigDecimal.ZERO) {
                    (sum * BigDecimal(100) / totalAmount)
                        .setScale(1, RoundingMode.HALF_UP)
                        .toPlainString() + "%"
                } else {
                    "0%"
                }

                CategoryAnalysis(
                    categoryId = category.id,
                    categoryName = category.name,
                    categoryEmoji = category.emoji,
                    isIncome = category.isIncome,
                    percentage = percentage,
                    totalAmount = sum.formatAmount()
                )
            }.sortedByDescending {
                it.percentage.trimEnd('%').toBigDecimalOrNull() ?: BigDecimal.ZERO
            }

            totalAmount to analysisList
        }
    }
}