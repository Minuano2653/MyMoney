package com.example.mymoney.presentation.base.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
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

@Composable
inline fun <reified VM : ViewModel> daggerViewModel(): VM {
    val factory = provideViewModelFactory()
    return viewModel(factory = factory)
}