package com.igd.snaplingo.data.remote

import com.igd.snaplingo.core.util.Resource
import com.igd.snaplingo.data.remote.dto.ObjectDetectionResponseItem
import okhttp3.RequestBody
import javax.inject.Inject

class HuggingFaceRepository @Inject constructor(
    private val apiService: HuggingFaceApiService,
) {

    suspend fun detectObjects(image: RequestBody): Resource<List<ObjectDetectionResponseItem>?> = try {
        val response = apiService.detectObjects(image = image)
        Resource.Success(response)
    } catch (e: Exception) {
        Resource.Error(e.localizedMessage ?: "Something went wrong!")
    }
}