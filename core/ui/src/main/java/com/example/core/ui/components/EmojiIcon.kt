package com.example.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Компонент отображения эмодзи с круглым фоном и заданным размером.
 *
 * @param emoji Строка с эмодзи для отображения.
 * @param backgroundColor Цвет фона (по умолчанию [androidx.compose.material3.MaterialTheme.colorScheme.secondary]).
 * @param size Размер компонента (ширина и высота) (по умолчанию 24.dp).
 */
@Composable
fun EmojiIcon(
    emoji: String,
    backgroundColor: Color = MaterialTheme.colorScheme.secondary,
    size: Dp = 24.dp
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(backgroundColor, CircleShape)
            .size(size)
    ) {
        Text(text = emoji)
    }
}
