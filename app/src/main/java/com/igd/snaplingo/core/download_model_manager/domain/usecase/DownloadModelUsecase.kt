package com.harissabil.fisch.core.datastore.local_user_manager.domain.usecase

import com.harissabil.fisch.core.datastore.local_user_manager.domain.DownloadModelManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DownloadModelUsecase @Inject constructor(
    private val downloadModelManager: DownloadModelManager,
) {

    fun readModelEntry(): Flow<Boolean> {
        return downloadModelManager.readModelEntry()
    }

    suspend fun saveModelEntry() {
        downloadModelManager.saveModelEntry()
    }
}