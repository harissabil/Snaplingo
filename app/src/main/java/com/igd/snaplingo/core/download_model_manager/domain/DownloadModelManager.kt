package com.harissabil.fisch.core.datastore.local_user_manager.domain

import kotlinx.coroutines.flow.Flow

interface DownloadModelManager {

    suspend fun saveModelEntry()

    fun readModelEntry(): Flow<Boolean>

    suspend fun deleteModelEntry()
}