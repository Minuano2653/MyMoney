package com.example.mymoney.presentation.components.model

import com.example.mymoney.R

/**
 * Состояние для Floating Action Button (FAB).
 *
 * @property isVisible Флаг видимости FAB.
 * @property iconRes Ресурс иконки для FAB (по умолчанию `R.drawable.ic_add`).
 * @property onClick Лямбда-обработчик нажатия на FAB (может быть null).
 */
data class FabState(
    val isVisible: Boolean = false,
    val iconRes: Int = R.drawable.ic_add,
    val onClick: (() -> Unit)? = null
)
