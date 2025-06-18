package com.example.mymoney.presentation.components

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