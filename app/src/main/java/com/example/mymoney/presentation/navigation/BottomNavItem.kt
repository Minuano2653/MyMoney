package com.example.mymoney.presentation.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class BottomNavItem<T: Any>(
    val route: T,
    @StringRes val labelRes: Int,
    @DrawableRes val iconRes: Int
)
