package com.example.mymoney.presentation.components

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Вертикальный разделитель с настраиваемой толщиной и цветом.
 *
 * @param thickness Толщина разделителя (по умолчанию [1.dp]).
 * @param color Цвет разделителя (по умолчанию [MaterialTheme.colorScheme.outlineVariant]).
 */
@Composable
fun Divider(
    thickness: Dp = 1.dp,
    color: Color = MaterialTheme.colorScheme.outlineVariant
) {
    HorizontalDivider(
        thickness = thickness,
        color = color
    )
}
