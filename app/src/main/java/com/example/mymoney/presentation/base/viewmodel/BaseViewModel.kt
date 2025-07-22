package com.example.mymoney.presentation.base.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymoney.presentation.base.contract.BaseEvent
import com.example.mymoney.presentation.base.contract.BaseSideEffect
import com.example.mymoney.presentation.base.contract.BaseUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Базовая ViewModel с поддержкой состояния UI, событий и побочных эффектов.
 *
 * @param T Тип состояния UI, реализующий [BaseUiState].
 * @param E Тип событий, реализующий [BaseEvent].
 * @param S Тип побочных эффектов, реализующий [BaseSideEffect].
 * @param initialState Начальное состояние UI.
 */
abstract class BaseViewModel<T: BaseUiState, E: BaseEvent, S: BaseSideEffect>(
    initialState: T
): ViewModel() {

    protected val _uiState = MutableStateFlow<T>(initialState)
    open val uiState: StateFlow<T> = _uiState.asStateFlow()

    protected val _sideEffect = MutableSharedFlow<S>()
    val sideEffect = _sideEffect.asSharedFlow()

    abstract fun handleEvent(event: E)

    fun emitEffect(effect: S) {
        viewModelScope.launch {
            _sideEffect.emit(effect)
        }
    }
}
