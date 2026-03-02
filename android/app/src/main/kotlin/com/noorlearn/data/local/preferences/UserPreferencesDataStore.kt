package com.noorlearn.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreferencesDataStore(private val context: Context) {
    companion object {
        val ROLE_MODE = stringPreferencesKey("role_mode")
        val LANGUAGE = stringPreferencesKey("language")
        val STREAK_COUNT = intPreferencesKey("streak_count")
    }

    val roleMode: Flow<String?> = context.dataStore.data.map { it[ROLE_MODE] }
    val language: Flow<String?> = context.dataStore.data.map { it[LANGUAGE] }
    val streakCount: Flow<Int> = context.dataStore.data.map { it[STREAK_COUNT] ?: 0 }

    suspend fun saveRoleMode(mode: String) {
        context.dataStore.edit { it[ROLE_MODE] = mode }
    }

    suspend fun incrementStreak() {
        context.dataStore.edit {
            val current = it[STREAK_COUNT] ?: 0
            it[STREAK_COUNT] = current + 1
        }
    }
}
