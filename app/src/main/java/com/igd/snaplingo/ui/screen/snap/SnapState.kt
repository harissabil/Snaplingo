package com.igd.snaplingo.ui.screen.snap

import com.igd.snaplingo.data.remote.dto.ObjectDetectionResponseItem

data class SnapState(
    val isLoading: Boolean = false,
    val detectedObject: List<ObjectDetectionResponseItem>? = null,
    val imagePath: String? = null,
    val lat: Double? = null,
    val long: Double? = null,
    val isCanTranslate: Boolean = false,
    val isDownloadingTranslation: Boolean = false,
    val isTranslating: Boolean = false,
    val translatedText: String? = null,
)
