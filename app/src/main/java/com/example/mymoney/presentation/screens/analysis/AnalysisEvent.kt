package com.example.mymoney.presentation.screens.analysis

import com.example.mymoney.presentation.base.contract.BaseEvent

sealed class AnalysisEvent: BaseEvent {
    object LoadAnalysis : AnalysisEvent()
    object OnStartDateClicked : AnalysisEvent()
    object OnEndDateClicked : AnalysisEvent()
    object OnBackPressed : AnalysisEvent()
    data class OnStartDateSelected(val date: String) : AnalysisEvent()
    data class OnEndDateSelected(val date: String) : AnalysisEvent()
}