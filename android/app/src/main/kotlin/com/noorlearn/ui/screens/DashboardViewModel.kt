package com.noorlearn.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noorlearn.data.local.preferences.UserPreferencesDataStore
import com.noorlearn.domain.model.Hadith
import com.noorlearn.domain.repository.HadithRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val hadithRepository: HadithRepository,
    private val userPreferences: UserPreferencesDataStore
) : ViewModel() {

    val streakCount: StateFlow<Int> = userPreferences.streakCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val userName: StateFlow<String> = userPreferences.userName
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "Learner")
        
    val primaryGoal: StateFlow<String?> = userPreferences.primaryGoal
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _dailyHadith = MutableStateFlow<Hadith?>(null)
    val dailyHadith: StateFlow<Hadith?> = _dailyHadith.asStateFlow()

    init {
        loadDailyHadith()
    }

    private fun loadDailyHadith() {
        viewModelScope.launch {
            try {
                _dailyHadith.value = hadithRepository.getDailyHadith()
            } catch (_: Exception) { }
        }
    }
}
