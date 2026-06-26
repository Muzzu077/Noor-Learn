package com.noorlearn.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noorlearn.data.local.preferences.UserPreferencesDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToolsViewModel @Inject constructor(
    private val userPreferences: UserPreferencesDataStore
) : ViewModel() {

    fun completeAdhkarTask() {
        viewModelScope.launch {
            userPreferences.completeDailyJourneyTask("morning_adhkar")
        }
    }
}
