package com.codewithrish.pdfreader.core.common

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

abstract class BaseViewModel<STATE, EVENT>() : ViewModel() {
    abstract fun initState(): STATE
    abstract fun onEvent(event: EVENT)

    private val _state = MutableStateFlow(initState())
    val state: StateFlow<STATE>
        get() = _state

    fun updateState(newState: (currentState: STATE) -> STATE) {
        _state.update { currentState ->
            newState(currentState)
        }
    }

    val onEvent: ((EVENT) -> Unit) = { onEvent(it) }
}