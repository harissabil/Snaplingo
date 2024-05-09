package com.igd.snaplingo.data.remote

import com.igd.snaplingo.BuildConfig
import com.igd.snaplingo.data.remote.dto.ObjectDetectionResponseItem
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface HuggingFaceApiService {

    @POST("models/hustvl/yolos-tiny")
    suspend fun detectObjects(
        @Header("Authorization") token: String = "Bearer ${BuildConfig.HUGGING_FACE_API_KEY}",
        @Header("Content-Type") contentType: String = "image/jpeg",
        @Body image: RequestBody,
    ): List<ObjectDetectionResponseItem>?
}