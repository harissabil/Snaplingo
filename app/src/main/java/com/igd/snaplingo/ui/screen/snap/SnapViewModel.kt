package com.igd.snaplingo.ui.screen.snap

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.harissabil.fisch.core.datastore.local_user_manager.domain.usecase.DownloadModelUsecase
import com.igd.snaplingo.core.current_location.domain.LocationTracker
import com.igd.snaplingo.core.util.Resource
import com.igd.snaplingo.data.local.SnapRepository
import com.igd.snaplingo.data.local.entity.SnapEntity
import com.igd.snaplingo.data.remote.HuggingFaceRepository
import com.igd.snaplingo.ui.screen.snap.util.reduceFileImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SnapViewModel @Inject constructor(
    private val huggingFaceRepository: HuggingFaceRepository,
    private val snapRepository: SnapRepository,
    private val locationTracker: LocationTracker,
    private val downloadModelUsecase: DownloadModelUsecase,
) : ViewModel() {

    private val _state = MutableStateFlow(SnapState())
    val state: StateFlow<SnapState> = _state.asStateFlow()

    var cameraPermissionGranted by mutableStateOf(false)
        private set

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow: SharedFlow<UIEvent> = _eventFlow.asSharedFlow()

    private val options = TranslatorOptions
        .Builder()
        .setSourceLanguage(TranslateLanguage.ENGLISH)
        .setTargetLanguage(TranslateLanguage.INDONESIAN)
        .build()

    private val languageTranslator = Translation
        .getClient(options)

    init {
        readDownloadModelEntry()
    }

    private fun readDownloadModelEntry() {
        downloadModelUsecase.readModelEntry().onEach { isModelDownloaded ->
            when (isModelDownloaded) {
                true -> {
                    // Do nothing because model is already downloaded
                }

                false -> {
                    // Download model
                    downloadModelIfNotAvailable(context = null)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun onCameraPermissionGranted(isGranted: Boolean) {
        cameraPermissionGranted = isGranted
    }

    private fun onTranslate(
        text: String,
        context: Context,
    ) {
        _state.value = state.value.copy(isTranslating = true)
        languageTranslator.translate(text)
            .addOnSuccessListener { translatedText ->
                _state.value = state.value.copy(
                    translatedText = translatedText
                )
                _state.value = state.value.copy(isTranslating = false)
            }
            .addOnFailureListener {
                downloadModelIfNotAvailable(context)
                _state.value = state.value.copy(isTranslating = false)
            }
    }

    private fun downloadModelIfNotAvailable(
        context: Context? = null,
    ) {
        _state.value = state.value.copy(isDownloadingTranslation = true)

        showMessage("Downloading model for translation, please wait...", context)

        val conditions = DownloadConditions
            .Builder()
            .requireWifi()
            .build()

        languageTranslator
            .downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                showMessage("Model downloaded successfully!", context)
                _state.value = state.value.copy(isDownloadingTranslation = false)
                viewModelScope.launch { downloadModelUsecase.saveModelEntry() }
            }
            .addOnFailureListener {
                showMessage("Some error occurred, couldn't download language model...", context)
            }
    }

    fun detectObjects(file: File, context: Context) = viewModelScope.launch {
        if (_state.value.isLoading) return@launch

        _state.value = SnapState(isLoading = true)

        val currentLocation = async { locationTracker.getCurrentLocation() }
        val (lat, long) = currentLocation.await()?.latitude to currentLocation.await()?.longitude

        val imageBinary =
            reduceFileImage(file).asRequestBody(contentType = "application/octet".toMediaType())
        when (val response = huggingFaceRepository.detectObjects(imageBinary)) {
            is Resource.Error -> {
                Timber.e("Error: ${response.message}")
                _state.value = SnapState(isLoading = false)
                _eventFlow.emit(UIEvent.ShowSnackbar(response.message ?: "Something went wrong!"))
            }

            is Resource.Loading -> {}
            is Resource.Success -> {
                Timber.d("Detected objects: ${response.data}")
                _state.value = SnapState(
                    isLoading = false,
                    detectedObject = response.data,
                    imagePath = file.absolutePath,
                    lat = lat,
                    long = long
                )

                if (response.data.isNullOrEmpty()) {
                    _eventFlow.emit(UIEvent.ShowSnackbar("No object detected!"))
                } else {
                    val detectedObject = response.data.first()
                    onTranslate(detectedObject.label!!, context)
                }
            }
        }

//        delay(1000)
//        _state.value = SnapState(
//            isLoading = false,
//            detectedObject = listOf(
//                ObjectDetectionResponseItem(
//                    score = 0.9,
//                    label = "Person",
//                    box = Box(ymin = 1, ymax = 9, xmin = 1, xmax = 9)
//                )
//            ),
//            imagePath = file.absolutePath,
//            lat = lat,
//            long = long
//        )
//        onTranslate("Person", context)
    }

    fun upsertSnap(isSaved: Boolean) = viewModelScope.launch {
        snapRepository.upsertSnap(
            SnapEntity(
                image = state.value.imagePath,
                label = state.value.detectedObject?.first()?.label!!,
                translation = state.value.translatedText,
                isSaved = isSaved,
                lat = state.value.lat,
                long = state.value.long
            )
        )
        resetState()
    }

    private fun resetState() {
        _state.value = SnapState(
            isCanTranslate = state.value.isCanTranslate,
        )
    }

    sealed class UIEvent {
        data class ShowSnackbar(val message: String) : UIEvent()
    }

    private fun showMessage(message: String, context: Context? = null) {
        if (context != null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        } else {
            viewModelScope.launch {
                _eventFlow.emit(UIEvent.ShowSnackbar(message))
            }
        }
    }
}