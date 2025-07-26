package com.example.core.ui.charts.pie

import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun PieChart(
    items: List<PieChartItem>,
    modifier: Modifier = Modifier,
    size: Dp = 300.dp,
    strokeWidth: Float = 40f,
    animationDuration: Int = 1000,
    contentTextSize: Float = 14f,
    contentTextColor: Color = MaterialTheme.colorScheme.onBackground,
    showInnerLegend: Boolean = true
) {
    val innerLegendSizeRatio = 0.65f

    val totalValue = remember(items) {
        items.sumOf { it.value.toDouble() }.toFloat()
    }

    val coloredItems = remember(items) {
        items.mapIndexed { index, item ->
            if (item.color == Color.Unspecified) {
                item.copy(color = generateDefaultColor(index))
            } else item
        }
    }
    var animationStarted by remember { mutableStateOf(false) }

    val animatedProgress by animateFloatAsState(
        targetValue = if (animationStarted) 1f else 0f,
        animationSpec = tween(
            durationMillis = animationDuration,
            easing = EaseOutCubic
        ),
        label = "pie_chart_animation"
    )

    LaunchedEffect(Unit) {
        animationStarted = true
    }

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        PieChartCanvas(
            items = coloredItems,
            totalValue = totalValue,
            progress = animatedProgress,
            strokeWidth = strokeWidth,
            modifier = Modifier.fillMaxSize()
        )

        if (showInnerLegend) {
            PieChartInnerLegend(
                legendItems = coloredItems,
                totalValue = totalValue,
                contentTextSize = contentTextSize,
                contentTextColor = contentTextColor,
                modifier = Modifier.size(size * innerLegendSizeRatio)
            )
        }
    }
}

@Composable
private fun PieChartCanvas(
    items: List<PieChartItem>,
    totalValue: Float,
    progress: Float,
    strokeWidth: Float,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = (size.minDimension - strokeWidth) / 2

        var currentAngle = -90f

        items.forEach { item ->
            val normalizedValue = (item.value / totalValue) * 360f
            val sweepAngle = normalizedValue * progress

            if (sweepAngle > 0) {
                drawArc(
                    color = item.color,
                    startAngle = currentAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
                )
            }

            currentAngle += normalizedValue
        }
    }
}

@Composable
private fun PieChartInnerLegend(
    legendItems: List<PieChartItem>,
    totalValue: Float,
    contentTextSize: Float,
    contentTextColor: Color,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.padding(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(vertical = 2.dp)
    ) {
        items(items = legendItems) { item ->
            val percentage = if (totalValue > 0f) {
                String.format("%.1f", (item.value / totalValue) * 100)
            } else "0"

            PieChartInnerLegendItem(
                item = item,
                percentage = percentage,
                textSize = contentTextSize,
                textColor = contentTextColor
            )
        }
    }
}

@Composable
private fun PieChartInnerLegendItem(
    item: PieChartItem,
    percentage: String,
    textSize: Float,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(modifier = Modifier.size(12.dp)) {
            drawCircle(
                color = item.color,
                radius = size.minDimension / 2
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "$percentage% ${item.label}",
            fontSize = textSize.sp,
            color = textColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}



private fun generateDefaultColor(index: Int): Color {
    val defaultColors = listOf(
        Color(0xFF9C27B0), // Deep Purple (хорошо на светлом и тёмном)
        Color(0xFFE91E63), // Hot Pink (ярко, но не кричаще)
        Color(0xFFFF5722), // Deep Orange (выделяется)
        Color(0xFFFFC107), // Amber (отличный жёлтый)
        Color(0xFF4CAF50), // Green (классический, контрастный)
        Color(0xFF2196F3), // Blue (отлично на обоих темах)
        Color(0xFF00BCD4), // Cyan (свежий, не сливается)
        Color(0xFF607D8B), // Blue Grey (нейтральный, подходит для тёмной темы)
        Color(0xFF795548), // Brown (тёплый, отлично на тёмном фоне)
        Color(0xFF009688), // Teal (сбалансированный)
        Color(0xFF8BC34A), // Light Green (яркий, но не резкий)
        Color(0xFFCDDC39), // Lime (выделяется, но приятен)
        Color(0xFF3F51B5), // Indigo (хорошо контрастирует)
        Color(0xFF03A9F4), // Light Blue (лучше, чем пастельный)
        Color(0xFFE64A19), // Deep Orange (другой оттенок для разнообразия)
        Color(0xFFAA00FF), // Violet (насыщенный, заметный)
        Color(0xFF6D4C41), // Brown Dark (для тёмной темы)
        Color(0xFF9E9E9E), // Grey (нейтральный, но не скучный)
    )
    return defaultColors[index % defaultColors.size]
}