package com.example.mymoney.utils

import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

fun BigDecimal.formatAmountWithCurrency(currency: String = ""): String {
    val symbols = DecimalFormatSymbols(Locale("ru")).apply {
        groupingSeparator = ' '
        decimalSeparator = ','
    }

    val hasFractionalPart = this.stripTrailingZeros().scale() > 0

    val pattern = if (hasFractionalPart) "#,##0.##" else "#,##0"
    val formatter = DecimalFormat(pattern, symbols)

    return "${formatter.format(this)} $currency"
}

fun BigDecimal.formatAmount(): String {
    val symbols = DecimalFormatSymbols(Locale("ru")).apply {
        groupingSeparator = ' '
        decimalSeparator = '.'
    }

    val hasFractionalPart = this.stripTrailingZeros().scale() > 0

    val pattern = if (hasFractionalPart) "#,##0.##" else "#,##0"
    val formatter = DecimalFormat(pattern, symbols)

    return formatter.format(this)
}
