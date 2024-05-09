package com.igd.snaplingo.ui.screen.flashcard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.igd.snaplingo.data.local.SnapRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FlashcardViewModel @Inject constructor(
    private val snapRepository: SnapRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(FlashcardState())
    val state: StateFlow<FlashcardState> = _state.asStateFlow()

    init {
        getSnapList()
    }

    private fun getSnapList() = viewModelScope.launch {
        snapRepository.getSavedSnap().collect { snapList ->
            _state.value = state.value.copy(snapList = snapList)
        }
    }
}