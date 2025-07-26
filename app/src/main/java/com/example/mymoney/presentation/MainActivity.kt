package com.example.mymoney.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.mymoney.MyMoneyApp
import com.example.mymoney.presentation.navigation.RootGraph
import com.example.mymoney.presentation.screens.settings.theme.ThemeViewModel
import com.example.mymoney.presentation.theme.MyMoneyTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var themeViewModel: ThemeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appComponent = (application as MyMoneyApp).appComponent
        val viewModelComponent = appComponent.viewModelComponentFactory().create()
        val viewModelFactory = viewModelComponent.getViewModelFactory()

        themeViewModel = ViewModelProvider(this, viewModelFactory)[ThemeViewModel::class.java]

        lifecycleScope.launch {
            themeViewModel.initDefaultLanguage()
        }

        enableEdgeToEdge()
        setContent {
            val themeUiState by themeViewModel.uiState.collectAsStateWithLifecycle()

            MyMoneyTheme(appTheme = themeUiState.currentTheme) {
                RootGraph()
            }
        }
    }
}
