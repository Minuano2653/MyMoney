package com.example.core.ui.charts.pie

import androidx.compose.ui.graphics.Color

data class PieChartItem(
    val label: String,
    val value: Float,
    val color: Color = Color.Unspecified
)
