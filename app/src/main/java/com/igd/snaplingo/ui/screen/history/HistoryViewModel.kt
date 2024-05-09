package com.igd.snaplingo.ui.screen.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.igd.snaplingo.data.local.SnapRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val snapRepository: SnapRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(HistoryState())
    val state: StateFlow<HistoryState> = _state.asStateFlow()

    init {
        getSnapHistory()

    }

    private fun getSnapHistory() {
        snapRepository.getSnapHistory().onEach { snaps ->
            _state.value = _state.value.copy(snaps = snaps)
        }.launchIn(viewModelScope)
    }
}