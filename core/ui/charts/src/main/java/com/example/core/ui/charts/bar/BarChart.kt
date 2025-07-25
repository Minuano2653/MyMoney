package com.example.core.ui.charts.bar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs

@Composable
fun BarChart(
    modifier: Modifier = Modifier,
    data: List<ChartDataPoint>,
    maxHeight: Dp = 200.dp,
) {
    assert(data.isNotEmpty()) { "Input data is empty" }

    val maxAbsValue = remember(data) {
        data.maxOfOrNull { abs(it.value) } ?: 1f
    }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(maxHeight),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            data.forEach { dataPoint ->
                Bar(
                    value = dataPoint.value,
                    maxAbsValue = maxAbsValue,
                    maxHeight = maxHeight,
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            data.forEachIndexed { index, dataPoint ->
                val middleIndex = data.size / 2
                val shouldShowDate = index == 0 || index == data.lastIndex || index == middleIndex

                if (shouldShowDate) {
                    Text(
                        text = dataPoint.date,
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun RowScope.Bar(
    value: Float,
    maxAbsValue: Float,
    maxHeight: Dp,
    minBarHeight: Dp = 4.dp
) {
    val positiveColor = Color(0xFF4CAF50)
    val negativeColor = Color(0xFFFF5722)

    val color = if (value > 0) positiveColor else negativeColor

    val normalizedHeight = if (value == 0f) {
        minBarHeight.value
    } else {
        minBarHeight.value + (abs(value) / maxAbsValue) * (maxHeight.value - minBarHeight.value)
    }

    Box(
        modifier = Modifier
            .padding(horizontal = 2.dp)
            .height(normalizedHeight.dp)
            .weight(1f)
            .clip(RoundedCornerShape(8.dp))
            .background(color)
    )
}