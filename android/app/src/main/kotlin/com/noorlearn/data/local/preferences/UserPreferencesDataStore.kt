package com.noorlearn.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

@Singleton
class UserPreferencesDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        val ROLE_MODE = stringPreferencesKey("role_mode")
        val LANGUAGE = stringPreferencesKey("language")
        val STREAK_COUNT = intPreferencesKey("streak_count")
        val LAST_ACTIVE_DATE = longPreferencesKey("last_active_date")
        val USER_NAME = stringPreferencesKey("user_name")
        val SURAHS_READ = intPreferencesKey("surahs_read")
        val BOOKMARKS_COUNT = intPreferencesKey("bookmarks_count")
        val STUDY_MINUTES = intPreferencesKey("study_minutes")

        // Onboarding Preferences
        val LEARNING_LEVEL = stringPreferencesKey("learning_level")
        val PRIMARY_GOAL = stringPreferencesKey("primary_goal")
        val DAILY_COMMITMENT = stringPreferencesKey("daily_commitment")

        // Streak Tracking
        val LONGEST_STREAK = intPreferencesKey("longest_streak")

        // Auth
        val USER_ID = stringPreferencesKey("user_id")

        // Local bookmarks (comma-separated ayah IDs)
        val BOOKMARKED_AYAH_IDS = stringPreferencesKey("bookmarked_ayah_ids")

        // Daily Journey Progress
        val DAILY_JOURNEY_DAY = intPreferencesKey("daily_journey_day")
        val DAILY_JOURNEY_TASKS_COMPLETED = stringPreferencesKey("daily_journey_tasks_completed")

        // Vocabulary Builder
        val MASTERED_VOCAB_IDS = stringPreferencesKey("mastered_vocab_ids")

        // Daily reset tracking — stores the date string of last journey reset (e.g. "2026-06-26")
        val LAST_JOURNEY_DATE = stringPreferencesKey("last_journey_date")

        // Last Read Quran tracking
        val LAST_READ_SURAH_ID = intPreferencesKey("last_read_surah_id")
        val LAST_READ_SURAH_NAME = stringPreferencesKey("last_read_surah_name")
        val LAST_READ_AYAH_NUMBER = intPreferencesKey("last_read_ayah_number")
    }

    val roleMode: Flow<String> = context.dataStore.data.map { it[ROLE_MODE] ?: "adult" }
    val language: Flow<String> = context.dataStore.data.map { it[LANGUAGE] ?: "en" }
    val streakCount: Flow<Int> = context.dataStore.data.map { it[STREAK_COUNT] ?: 0 }
    val lastActiveDate: Flow<Long> = context.dataStore.data.map { it[LAST_ACTIVE_DATE] ?: 0L }
    val userName: Flow<String> = context.dataStore.data.map { it[USER_NAME] ?: "Learner" }
    val surahsRead: Flow<Int> = context.dataStore.data.map { it[SURAHS_READ] ?: 0 }
    val bookmarksCount: Flow<Int> = context.dataStore.data.map { it[BOOKMARKS_COUNT] ?: 0 }
    val studyMinutes: Flow<Int> = context.dataStore.data.map { it[STUDY_MINUTES] ?: 0 }

    val userId: Flow<String?> = context.dataStore.data.map { it[USER_ID] }
    val learningLevel: Flow<String?> = context.dataStore.data.map { it[LEARNING_LEVEL] }
    val primaryGoal: Flow<String?> = context.dataStore.data.map { it[PRIMARY_GOAL] }
    val dailyCommitment: Flow<String?> = context.dataStore.data.map { it[DAILY_COMMITMENT] }
    val longestStreak: Flow<Int> = context.dataStore.data.map { it[LONGEST_STREAK] ?: 0 }

    val dailyJourneyDay: Flow<Int> = context.dataStore.data.map { it[DAILY_JOURNEY_DAY] ?: 1 }
    val dailyJourneyTasksCompleted: Flow<Set<String>> = context.dataStore.data.map { prefs ->
        val raw = prefs[DAILY_JOURNEY_TASKS_COMPLETED] ?: ""
        if (raw.isBlank()) emptySet()
        else raw.split(",").toSet()
    }

    val masteredVocabIds: Flow<Set<Int>> = context.dataStore.data.map { prefs ->
        val raw = prefs[MASTERED_VOCAB_IDS] ?: ""
        if (raw.isBlank()) emptySet()
        else raw.split(",").mapNotNull { it.toIntOrNull() }.toSet()
    }

    val lastReadSurahId: Flow<Int?> = context.dataStore.data.map { it[LAST_READ_SURAH_ID] }
    val lastReadSurahName: Flow<String?> = context.dataStore.data.map { it[LAST_READ_SURAH_NAME] }
    val lastReadAyahNumber: Flow<Int?> = context.dataStore.data.map { it[LAST_READ_AYAH_NUMBER] }

    suspend fun saveLastRead(surahId: Int, surahName: String, ayahNumber: Int) {
        context.dataStore.edit { prefs ->
            prefs[LAST_READ_SURAH_ID] = surahId
            prefs[LAST_READ_SURAH_NAME] = surahName
            prefs[LAST_READ_AYAH_NUMBER] = ayahNumber
        }
    }

    suspend fun toggleVocabMastery(vocabId: Int): Boolean {
        var isMastered = false
        context.dataStore.edit { prefs ->
            val raw = prefs[MASTERED_VOCAB_IDS] ?: ""
            val current = if (raw.isBlank()) mutableSetOf()
            else raw.split(",").mapNotNull { it.toIntOrNull() }.toMutableSet()

            if (current.contains(vocabId)) {
                current.remove(vocabId)
                isMastered = false
            } else {
                current.add(vocabId)
                isMastered = true
            }
            prefs[MASTERED_VOCAB_IDS] = current.joinToString(",")
        }
        return isMastered
    }

    suspend fun saveDailyJourneyDay(day: Int) {
        context.dataStore.edit {
            it[DAILY_JOURNEY_DAY] = day
            it[DAILY_JOURNEY_TASKS_COMPLETED] = "" // Reset task checklist on new day
        }
    }

    suspend fun toggleDailyJourneyTask(taskId: String): Boolean {
        var isCompleted = false
        context.dataStore.edit { prefs ->
            val raw = prefs[DAILY_JOURNEY_TASKS_COMPLETED] ?: ""
            val current = if (raw.isBlank()) mutableSetOf()
            else raw.split(",").toMutableSet()

            if (current.contains(taskId)) {
                current.remove(taskId)
                isCompleted = false
            } else {
                current.add(taskId)
                isCompleted = true
            }
            prefs[DAILY_JOURNEY_TASKS_COMPLETED] = current.joinToString(",")
        }
        return isCompleted
    }

    suspend fun completeDailyJourneyTask(taskId: String) {
        context.dataStore.edit { prefs ->
            val raw = prefs[DAILY_JOURNEY_TASKS_COMPLETED] ?: ""
            val current = if (raw.isBlank()) mutableSetOf()
            else raw.split(",").toMutableSet()

            if (!current.contains(taskId)) {
                current.add(taskId)
                prefs[DAILY_JOURNEY_TASKS_COMPLETED] = current.joinToString(",")
            }
        }
    }

    /**
     * Checks if today is a new day compared to when tasks were last reset.
     * If so, clears the completed tasks list for a fresh daily journey.
     * Should be called on every app launch / ViewModel init.
     */
    suspend fun checkAndResetDailyTasks(todayDate: String) {
        context.dataStore.edit { prefs ->
            val lastDate = prefs[LAST_JOURNEY_DATE] ?: ""
            if (lastDate != todayDate) {
                // New day — reset journey tasks
                prefs[DAILY_JOURNEY_TASKS_COMPLETED] = ""
                prefs[LAST_JOURNEY_DATE] = todayDate
            }
        }
    }

    suspend fun saveOnboardingPreferences(level: String, goal: String, commitment: String) {
        context.dataStore.edit {
            it[LEARNING_LEVEL] = level
            it[PRIMARY_GOAL] = goal
            it[DAILY_COMMITMENT] = commitment
        }
    }

    suspend fun saveRoleMode(mode: String) {
        context.dataStore.edit { it[ROLE_MODE] = mode }
    }

    suspend fun saveUserName(name: String) {
        context.dataStore.edit { it[USER_NAME] = name }
    }

    suspend fun saveUserId(id: String) {
        context.dataStore.edit { it[USER_ID] = id }
    }

    suspend fun updateStreak() {
        context.dataStore.edit { preferences ->
            val now = System.currentTimeMillis()
            val oneDayInMillis = 24 * 60 * 60 * 1000L
            val lastActive = preferences[LAST_ACTIVE_DATE] ?: 0L
            val current = preferences[STREAK_COUNT] ?: 0
            val longest = preferences[LONGEST_STREAK] ?: 0

            val diffDays = (now - lastActive) / oneDayInMillis

            if (lastActive == 0L || diffDays >= 2) {
                // Streak broken or first time
                preferences[STREAK_COUNT] = 1
                preferences[LAST_ACTIVE_DATE] = now
            } else if (diffDays == 1L) {
                // Streak continued
                val newStreak = current + 1
                preferences[STREAK_COUNT] = newStreak
                preferences[LAST_ACTIVE_DATE] = now
                if (newStreak > longest) {
                    preferences[LONGEST_STREAK] = newStreak
                }
            }
            // If diffDays == 0, already tracked today, do nothing.
        }
    }

    suspend fun incrementSurahsRead() {
        context.dataStore.edit {
            val current = it[SURAHS_READ] ?: 0
            it[SURAHS_READ] = current + 1
        }
    }

    suspend fun addStudyMinutes(minutes: Int) {
        context.dataStore.edit {
            val current = it[STUDY_MINUTES] ?: 0
            it[STUDY_MINUTES] = current + minutes
        }
    }

    // Local bookmark helpers
    val bookmarkedAyahIds: Flow<Set<Int>> = context.dataStore.data.map { prefs ->
        val raw = prefs[BOOKMARKED_AYAH_IDS] ?: ""
        if (raw.isBlank()) emptySet()
        else raw.split(",").mapNotNull { it.toIntOrNull() }.toSet()
    }

    suspend fun toggleLocalBookmark(ayahId: Int): Boolean {
        var isNowBookmarked = false
        context.dataStore.edit { prefs ->
            val raw = prefs[BOOKMARKED_AYAH_IDS] ?: ""
            val current = if (raw.isBlank()) mutableSetOf()
            else raw.split(",").mapNotNull { it.toIntOrNull() }.toMutableSet()

            if (current.contains(ayahId)) {
                current.remove(ayahId)
                isNowBookmarked = false
            } else {
                current.add(ayahId)
                isNowBookmarked = true
            }
            prefs[BOOKMARKED_AYAH_IDS] = current.joinToString(",")
            prefs[BOOKMARKS_COUNT] = current.size
        }
        return isNowBookmarked
    }

    /** Clears all preferences — called on sign-out to prevent stale user data. */
    suspend fun clearAll() {
        context.dataStore.edit { it.clear() }
    }
}
