package com.noorlearn.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noorlearn.data.local.preferences.UserPreferencesDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

data class JourneyTask(
    val id: String,
    val title: String,
    val description: String,
    val route: String,
    val iconType: String // "adhkar", "quran", "hadith", "reflection"
)

@HiltViewModel
class DailyJourneyViewModel @Inject constructor(
    private val userPreferences: UserPreferencesDataStore
) : ViewModel() {

    // The tasks for each day (same every day — reset at midnight)
    val tasks = listOf(
        JourneyTask(
            id = "morning_adhkar",
            title = "Morning Adhkar",
            description = "\"Remember Me; I will remember you.\"",
            route = "tools?tab=1",
            iconType = "adhkar"
        ),
        JourneyTask(
            id = "surah_reading",
            title = "Surah Reading",
            description = "Read Surah Al-Mulk for protection and blessings.",
            route = "ayah_reader/67/Al-Mulk",
            iconType = "quran"
        ),
        JourneyTask(
            id = "hadith_day",
            title = "Hadith of the Day",
            description = "Read the Prophet's ﷺ wisdom for today.",
            route = "hadith_hub",
            iconType = "hadith"
        ),
        JourneyTask(
            id = "reflection_journal",
            title = "Reflection Journal",
            description = "Reflect on your growth and record your thoughts.",
            route = "reflection_journal",
            iconType = "reflection"
        )
    )

    val streakCount: StateFlow<Int> = userPreferences.streakCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val completedTasks: StateFlow<Set<String>> = userPreferences.dailyJourneyTasksCompleted
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    // Noor Points: 200 base + 20/streak day + 10/completed task
    val noorPoints: StateFlow<Int> = combine(streakCount, completedTasks) { streak, completed ->
        200 + (streak * 20) + (completed.size * 10)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 200)

    init {
        // On launch, check if tasks need to be reset for a new day
        checkDailyReset()
    }

    /**
     * Resets daily tasks if the last reset was on a different calendar date.
     * This ensures fresh tasks every morning at midnight.
     */
    private fun checkDailyReset() {
        viewModelScope.launch {
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            userPreferences.checkAndResetDailyTasks(today)
        }
    }

    fun toggleTask(taskId: String) {
        viewModelScope.launch {
            userPreferences.toggleDailyJourneyTask(taskId)
            userPreferences.updateStreak()
        }
    }
}
