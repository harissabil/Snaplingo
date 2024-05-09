package com.igd.snaplingo.ui.screen.home

import android.content.Context
import android.content.IntentSender
import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task
import com.igd.snaplingo.core.location.LocationHelper
import com.igd.snaplingo.data.local.SnapRepository
import com.igd.snaplingo.data.local.entity.SnapEntity
import com.igd.snaplingo.domain.GetLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val locationHelper: LocationHelper,
    private val getLocationUseCase: GetLocationUseCase,
    private val snapRepository: SnapRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _isLocationEnabled = MutableStateFlow(false)
    val isLocationEnabled: StateFlow<Boolean> = _isLocationEnabled.asStateFlow()

    private val _selectedSnap = MutableStateFlow<SnapEntity?>(null)
    val selectedSnap: StateFlow<SnapEntity?> = _selectedSnap.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow: SharedFlow<UIEvent> = _eventFlow.asSharedFlow()

    init {
        updateLocationServiceStatus()
        getSnapList()
        if (isLocationEnabled.value) {
            requestLocationUpdate()
        }
    }

    private fun getSnapList() = viewModelScope.launch {
        snapRepository.getSavedSnap().collect { snapList ->
            _state.update { it.copy(snapList = snapList) }
        }
    }

    fun setSelectedSnap(snap: SnapEntity?) {
        _selectedSnap.value = snap
    }

    fun getSnapByDate(date: String) = viewModelScope.launch {
        val snap = snapRepository.getSnapByDate(date)
        Timber.d("getSnapByDate: $snap")
        setSelectedSnap(snap)
    }

    fun deleteSnap(snap: SnapEntity) = viewModelScope.launch {
        snapRepository.deleteSnap(snap)
        _eventFlow.emit(UIEvent.ShowSnackbar("Snap deleted"))
    }

    fun requestLocationUpdate() = viewModelScope.launch {
        getLocationUseCase().collect { location ->
            _state.update { it.copy(location = location) }
        }
    }

    private fun updateLocationServiceStatus() {
        _isLocationEnabled.value = locationHelper.isConnected()
    }

    fun enableLocationRequest(
        context: Context,
        makeRequest: (intentSenderRequest: IntentSenderRequest) -> Unit,//Lambda to call when locations are off.
    ) {
        val locationRequest = LocationRequest.Builder( //Create a location request object
            Priority.PRIORITY_HIGH_ACCURACY, //Self explanatory
            10000 //Interval -> shorter the interval more frequent location updates
        ).build()

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val client: SettingsClient = LocationServices.getSettingsClient(context)
        val task: Task<LocationSettingsResponse> =
            client.checkLocationSettings(builder.build()) //Checksettings with building a request
        task.addOnSuccessListener { locationSettingsResponse ->
            Timber.tag("Location")
                .d("enableLocationRequest: LocationService Already Enabled $locationSettingsResponse")
        }
        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    val intentSenderRequest =
                        IntentSenderRequest.Builder(exception.resolution)
                            .build() //Create the request prompt
                    makeRequest(intentSenderRequest) //Make the request from UI
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    sealed class UIEvent {
        data class ShowSnackbar(val message: String) : UIEvent()
    }
}