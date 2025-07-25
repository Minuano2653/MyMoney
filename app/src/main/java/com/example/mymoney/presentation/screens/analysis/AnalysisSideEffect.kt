package com.example.mymoney.presentation.screens.analysis

import com.example.core.ui.contract.BaseSideEffect

sealed class AnalysisSideEffect: BaseSideEffect {
    data class ShowError(val message: Int) : AnalysisSideEffect()
    object NavigateBack : AnalysisSideEffect()
}