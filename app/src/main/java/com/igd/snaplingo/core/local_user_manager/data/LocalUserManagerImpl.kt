package com.igd.snaplingo.core.local_user_manager.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.harissabil.fisch.core.datastore.local_user_manager.domain.LocalUserManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalUserManagerImpl @Inject constructor(
    private val context: Context,
) : LocalUserManager {

    override suspend fun saveAppEntry() {
        context.dataStore.edit { settings ->
            settings[PreferenceKeys.APP_ENTRY] = true
        }
    }

    override fun readAppEntry(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[PreferenceKeys.APP_ENTRY] ?: false
        }
    }

    override suspend fun deleteAppEntry() {
        context.dataStore.edit { settings ->
            settings[PreferenceKeys.APP_ENTRY] = false
        }
    }
}

private val readOnlyProperty = preferencesDataStore(name = "user_settings")

val Context.dataStore: DataStore<Preferences> by readOnlyProperty

private object PreferenceKeys {
    val APP_ENTRY = booleanPreferencesKey("app_entry")
}