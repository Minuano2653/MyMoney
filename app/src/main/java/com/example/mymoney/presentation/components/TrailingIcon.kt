package com.example.mymoney.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.mymoney.R

/**
 * Компонент для отображения иконки в конце строки.
 *
 * @param iconId Ресурс иконки (по умолчанию `R.drawable.ic_more_vert`).
 */
@Composable
fun TrailingIcon(iconId: Int = R.drawable.ic_more_vert) {
    Image(
        painter = painterResource(id = iconId),
        contentDescription = null
    )
}
