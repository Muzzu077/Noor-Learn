package com.noorlearn.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noorlearn.data.local.preferences.UserPreferencesDataStore
import com.noorlearn.domain.model.RecitationLog
import com.noorlearn.domain.usecase.GetRecitationLogsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecitationHistoryViewModel @Inject constructor(
    private val getRecitationLogsUseCase: GetRecitationLogsUseCase,
    private val userPreferences: UserPreferencesDataStore
) : ViewModel() {

    private val _recitationLogs = MutableStateFlow<List<RecitationLog>>(emptyList())
    val recitationLogs: StateFlow<List<RecitationLog>> = _recitationLogs.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadRecitationLogs()
    }

    fun loadRecitationLogs() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            userPreferences.userId.collect { userId ->
                if (userId != null) {
                    try {
                        val result = getRecitationLogsUseCase(userId)
                        if (result.isSuccess) {
                            _recitationLogs.value = result.getOrDefault(emptyList()).sortedByDescending { it.createdAt }
                        } else {
                            _error.value = result.exceptionOrNull()?.message ?: "Failed to load history"
                        }
                    } catch (e: Exception) {
                        _error.value = e.message ?: "Failed to load history"
                    } finally {
                        _isLoading.value = false
                    }
                } else {
                    _isLoading.value = false
                }
            }
        }
    }
}
