package com.example.mymoney.presentation.screens.analysis

import com.example.mymoney.presentation.base.contract.BaseSideEffect

sealed class AnalysisSideEffect: BaseSideEffect {
    data class ShowError(val message: String) : AnalysisSideEffect()
    object NavigateBack : AnalysisSideEffect()
}