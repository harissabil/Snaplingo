package com.igd.snaplingo.core.download_model_manager.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.harissabil.fisch.core.datastore.local_user_manager.domain.DownloadModelManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DownloadModelManagerImpl @Inject constructor(
    private val context: Context,
) : DownloadModelManager {

    override suspend fun saveModelEntry() {
        context.dataStore.edit { settings ->
            settings[PreferenceKeys.MODEL_ENTRY] = true
        }
    }

    override fun readModelEntry(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[PreferenceKeys.MODEL_ENTRY] ?: false
        }
    }

    override suspend fun deleteModelEntry() {
        context.dataStore.edit { settings ->
            settings[PreferenceKeys.MODEL_ENTRY] = false
        }
    }
}

private val readOnlyProperty = preferencesDataStore(name = "translation_model_settings")

val Context.dataStore: DataStore<Preferences> by readOnlyProperty

private object PreferenceKeys {
    val MODEL_ENTRY = booleanPreferencesKey("model_entry")
}