package com.example.core.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.ui.contract.BaseEvent
import com.example.core.ui.contract.BaseSideEffect
import com.example.core.ui.contract.BaseUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Базовая ViewModel с поддержкой состояния UI, событий и побочных эффектов.
 *
 * @param T Тип состояния UI, реализующий [com.example.core.ui.contract.BaseUiState].
 * @param E Тип событий, реализующий [com.example.core.ui.contract.BaseEvent].
 * @param S Тип побочных эффектов, реализующий [com.example.core.ui.contract.BaseSideEffect].
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
