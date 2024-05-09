package com.igd.snaplingo.ui.screen.snap

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.igd.snaplingo.ui.component.SnapResult
import com.igd.snaplingo.ui.component.SnaplingoFullscreenLoading
import com.igd.snaplingo.ui.screen.snap.component.CameraPreviewScreen
import com.igd.snaplingo.ui.screen.snap.component.SnapTopAppBar
import com.igd.snaplingo.ui.screen.snap.util.getReadableLocation
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@Composable
fun SnapScreen(
    snackbarHostState: SnackbarHostState,
    onCameraPermissionDenied: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: SnapViewModel = hiltViewModel(),
) {
    val isCameraPermissionGranted = viewModel.cameraPermissionGranted

    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    val permissionRequest =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Timber.d("Camera permission granted")
                viewModel.onCameraPermissionGranted(true)
            } else {
                Timber.d("Camera permission denied")
                if (!isCameraPermissionGranted) {
                    onCameraPermissionDenied()
                }
            }
        }

    LaunchedEffect(key1 = Unit) {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) -> {
                // Camera permission already granted
                // Implement camera related code
                viewModel.onCameraPermissionGranted(true)

                when (PackageManager.PERMISSION_GRANTED) {
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ),
                    -> {
                        // Read external storage permission already granted
                        when (PackageManager.PERMISSION_GRANTED) {
                            ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ),
                            -> {
                                // Read external storage permission already granted
                            }

                            else -> {
                                permissionRequest.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            }
                        }
                    }

                    else -> {
                        permissionRequest.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }
                }
            }

            else -> {
                permissionRequest.launch(Manifest.permission.CAMERA)
            }
        }
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is SnapViewModel.UIEvent.ShowSnackbar -> {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
            }
        }
    }

    Scaffold(
        topBar = {
            SnapTopAppBar(onBackClick = onBackClick)
        }
    ) {
        Timber.d("padding: $it")
        if (isCameraPermissionGranted) {
            CameraPreviewScreen(
                onImageCapturedSuccess = { file ->
                    viewModel.detectObjects(file, context)
                }
            )
        }
    }

    if (state.isLoading || state.isDownloadingTranslation) {
        SnaplingoFullscreenLoading(
            backgroundColor = Color.DarkGray.copy(alpha = 0.5f)
        )
    }

    var showResultDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(key1 = state.detectedObject) {
        showResultDialog = state.detectedObject?.isNotEmpty() == true
    }

    if (showResultDialog) {
        SnapResult(
            onDismissRequest = {
                if (!state.detectedObject.isNullOrEmpty() && state.translatedText != null && state.lat != null && state.long != null) {
                    viewModel.upsertSnap(isSaved = false)
                }
                showResultDialog = false
            },
            onSaveSnapResult = {
                if (!state.detectedObject.isNullOrEmpty() && state.translatedText != null && state.lat != null && state.long != null) {
                    viewModel.upsertSnap(isSaved = true)
                }
                showResultDialog = false
            },
            imagePath = state.imagePath,
            label = state.detectedObject?.firstOrNull()?.label,
            labelTranslation = state.translatedText,
            location = getReadableLocation(state.lat, state.long, context),
            isTranslating = state.isTranslating
        )
    }
}