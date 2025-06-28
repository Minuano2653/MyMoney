package com.example.mymoney.presentation.components.model

import com.example.mymoney.R

/**
 * Состояние для верхнего AppBar.
 *
 * @property titleRes Ресурс строки для заголовка AppBar (по умолчанию `R.string.top_bar_title_expenses`).
 * @property leadingIconRes Ресурс иконки слева (может быть null).
 * @property trailingIconRes Ресурс иконки справа (может быть null).
 * @property onLeadingClick Обработчик клика по иконке слева (может быть null).
 * @property onTrailingClick Обработчик клика по иконке справа (может быть null).
 */
data class TopAppBarState(
    val titleRes: Int = R.string.top_bar_title_expenses,
    val leadingIconRes: Int? = null,
    val trailingIconRes: Int? = null,
    val onLeadingClick: (() -> Unit)? = null,
    val onTrailingClick: (() -> Unit)? = null,
)
