package com.example.core.ui.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource

/**
 * Компонент для отображения иконки в конце строки.
 *
 * @param iconId Ресурс иконки (по умолчанию `R.drawable.ic_more_vert`).
 */
@Composable
fun TrailingIcon(iconId: Int) {
    Image(
        painter = painterResource(id = iconId),
        contentDescription = null
    )
}
