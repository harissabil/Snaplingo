package com.igd.snaplingo.ui.screen.home

import android.app.Activity
import android.content.Intent
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.LocationSearching
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.igd.snaplingo.R
import com.igd.snaplingo.ui.screen.home.component.SnapDetail
import com.igd.snaplingo.ui.screen.home.component.TopIconButton
import com.igd.snaplingo.ui.screen.home.util.centerOnLocation
import com.igd.snaplingo.ui.screen.home.util.getExpandedBounds
import com.igd.snaplingo.ui.screen.home.util.loadBitmapDescriptorFromUrl
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun HomeScreen(
    innerPadding: PaddingValues,
    snackbarHostState: SnackbarHostState,
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToHistory: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val state by viewModel.state.collectAsState()
    val isLocationEnabled by viewModel.isLocationEnabled.collectAsState()
    val locationRequestLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartIntentSenderForResult()) { activityResult ->
            if (activityResult.resultCode == Activity.RESULT_OK) {
                // User has enabled location
                Timber.d("Location enabled")
                viewModel.requestLocationUpdate()
            } else {
                if (!isLocationEnabled) {
                    // If the user cancels, still make a check and then give a snackbar
                    scope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        val result = snackbarHostState
                            .showSnackbar(
                                message = "Location is required to get your current location",
                                actionLabel = "Enable",
                                duration = SnackbarDuration.Long
                            )
                        when (result) {
                            SnackbarResult.ActionPerformed -> {
                                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                                context.startActivity(intent)
                            }

                            SnackbarResult.Dismissed -> {
                                Timber.d("Snackbar dismissed")
                            }
                        }
                    }
                }
            }
        }

    LaunchedEffect(key1 = isLocationEnabled) {
        if (!isLocationEnabled) {
            viewModel.enableLocationRequest(context = context) {
                locationRequestLauncher.launch(it)
            }
        }
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is HomeViewModel.UIEvent.ShowSnackbar -> {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
            }
        }
    }

    val cameraPositionState = rememberCameraPositionState()
    val properties = MapProperties(
        mapStyleOptions = MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style),
        isBuildingEnabled = true,
        isMyLocationEnabled = true,
    )

    LaunchedEffect(key1 = Unit) {
        if (cameraPositionState.position.tilt != 90f) {
            cameraPositionState.position = CameraPosition.builder()
                .target(cameraPositionState.position.target)
                .tilt(90f)
                .build()
        }
        if (state.location != null) {
            cameraPositionState.centerOnLocation(state.location!!)
        }
    }

    LaunchedEffect(key1 = state.location) {
        if (state.location != null && cameraPositionState.position.zoom < 17.5f) {
            if (cameraPositionState.position.target !in getExpandedBounds(state.location!!, 0.01)) {
                cameraPositionState.centerOnLocation(state.location!!)
            }
        }
    }

    val uiSettings = MapUiSettings(
        zoomControlsEnabled = false,
        compassEnabled = false,
        myLocationButtonEnabled = false,
    )

    val contentPadding = PaddingValues(
        start = innerPadding.calculateStartPadding(layoutDirection = LocalLayoutDirection.current),
        top = innerPadding.calculateTopPadding(),
        end = innerPadding.calculateEndPadding(layoutDirection = LocalLayoutDirection.current),
        bottom = 0.dp,
    )

    val selectedSnap by viewModel.selectedSnap.collectAsState()
    var showDetailDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            contentPadding = contentPadding,
            properties = properties,
            uiSettings = uiSettings,
        ) {
            state.snapList.onEach { snap ->
                val iconState = remember { mutableStateOf<BitmapDescriptor?>(null) }
                LaunchedEffect(key1 = snap) {
                    iconState.value = loadBitmapDescriptorFromUrl(
                        context = context,
                        imageUrl = snap.image!!
                    )
                }
                Marker(
                    state = rememberMarkerState(position = LatLng(snap.lat!!, snap.long!!)),
                    icon = iconState.value,
                    title = snap.label,
                    snippet = snap.date,
                    onClick = {
                        viewModel.getSnapByDate(it.snippet!!)
                        showDetailDialog = true
                        true
                    },
                )
            }
        }

        if (showDetailDialog) {
            selectedSnap?.let { snap ->
                SnapDetail(
                    onDismissRequest = {
                        showDetailDialog = false
                        viewModel.setSelectedSnap(null)
                    },
                    onDeleteSnapResult = {
                        selectedSnap?.let { viewModel.deleteSnap(it) }
                        showDetailDialog = false
                    },
                    snapEntity = snap,
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(contentPadding)
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                TopIconButton(
                    icon = Icons.Outlined.Person,
                    onClick = {
                        scope.launch {
                            cameraPositionState.centerOnLocation(state.location!!)
                        }
                    }
                )
                Spacer(modifier = Modifier.weight(1f))
                TopIconButton(
                    icon = Icons.Outlined.History,
                    onClick = onNavigateToHistory
                )
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.weight(1f))
                if (state.location != null) {
                    AnimatedVisibility(
                        visible = cameraPositionState.position.target !in getExpandedBounds(state.location!!),
                        enter = fadeIn(), exit = fadeOut()
                    ) {
                        TopIconButton(
                            icon = Icons.Outlined.LocationSearching,
                            onClick = {
                                scope.launch {
                                    cameraPositionState.centerOnLocation(state.location!!)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}