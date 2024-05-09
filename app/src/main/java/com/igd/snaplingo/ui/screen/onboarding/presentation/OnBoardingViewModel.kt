package com.igd.snaplingo.ui.screen.onboarding.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.igd.snaplingo.ui.screen.onboarding.domain.usecase.SaveAppEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val saveAppEntry: SaveAppEntry
) : ViewModel() {

    fun saveAppEntry() {
        viewModelScope.launch {
            saveAppEntry.invoke()
        }
    }
}