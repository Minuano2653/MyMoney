package com.example.mymoney.presentation.base.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import com.example.mymoney.MyMoneyApp

@Composable
fun provideViewModelFactory(): ViewModelProvider.Factory {
    val context = LocalContext.current
    val appComponent = (context.applicationContext as MyMoneyApp).appComponent

    val viewModelComponent = remember {
        appComponent.viewModelComponentFactory().create()
    }

    return viewModelComponent.getViewModelFactory()
}