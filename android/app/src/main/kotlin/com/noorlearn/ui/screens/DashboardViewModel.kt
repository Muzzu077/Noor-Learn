package com.noorlearn.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noorlearn.data.local.preferences.UserPreferencesDataStore
import com.noorlearn.domain.model.Hadith
import com.noorlearn.domain.model.RecitationLog
import com.noorlearn.domain.repository.HadithRepository
import com.noorlearn.domain.usecase.GetRecitationLogsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val hadithRepository: HadithRepository,
    private val userPreferences: UserPreferencesDataStore,
    private val getRecitationLogsUseCase: GetRecitationLogsUseCase
) : ViewModel() {

    val streakCount: StateFlow<Int> = userPreferences.streakCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val userName: StateFlow<String> = userPreferences.userName
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "Learner")
        
    val primaryGoal: StateFlow<String?> = userPreferences.primaryGoal
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val dailyJourneyDay: StateFlow<Int> = userPreferences.dailyJourneyDay
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1)

    val lastReadSurahId: StateFlow<Int?> = userPreferences.lastReadSurahId
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val lastReadSurahName: StateFlow<String?> = userPreferences.lastReadSurahName
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Real journey progress — driven by same DataStore as DailyJourneyViewModel
    val completedJourneyTasks: StateFlow<Set<String>> = userPreferences.dailyJourneyTasksCompleted
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    // 4 tasks total in DailyJourneyViewModel
    val journeyProgressPercent: StateFlow<Float> = userPreferences.dailyJourneyTasksCompleted
        .map { completed -> completed.size.coerceAtMost(4) / 4f }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0f)

    private val _dailyHadith = MutableStateFlow<Hadith?>(null)
    val dailyHadith: StateFlow<Hadith?> = _dailyHadith.asStateFlow()

    private val _recitationLogs = MutableStateFlow<List<RecitationLog>>(emptyList())
    val recitationLogs: StateFlow<List<RecitationLog>> = _recitationLogs.asStateFlow()

    init {
        loadDailyHadith()
        loadRecitationLogs()
    }

    private fun loadDailyHadith() {
        viewModelScope.launch {
            try {
                _dailyHadith.value = hadithRepository.getDailyHadith()
            } catch (_: Exception) { }
        }
    }

    private fun loadRecitationLogs() {
        viewModelScope.launch {
            userPreferences.userId.collect { userId ->
                if (userId != null) {
                    try {
                        val result = getRecitationLogsUseCase(userId)
                        if (result.isSuccess) {
                            _recitationLogs.value = result.getOrDefault(emptyList())
                        }
                    } catch (_: Exception) {}
                }
            }
        }
    }
}
