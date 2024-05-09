package com.igd.snaplingo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harissabil.fisch.core.datastore.local_user_manager.domain.usecase.ReadAppEntry
import com.igd.snaplingo.ui.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val readAppEntry: ReadAppEntry,
) : ViewModel() {

    var splashCondition by mutableStateOf(true)
        private set

    var startDestionation by mutableStateOf(Route.AppStartNavigation.route)
        private set

    init {
        getAppEntry()
    }

    private fun getAppEntry() {
        readAppEntry.invoke().onEach { onBoardingScreenPassed ->
            startDestionation = when {
                onBoardingScreenPassed -> {
                    Route.MainNavigation.route
                }

                else -> {
                    Route.AppStartNavigation.route
                }
            }
            delay(300)
            splashCondition = false
        }.launchIn(viewModelScope)
    }
}