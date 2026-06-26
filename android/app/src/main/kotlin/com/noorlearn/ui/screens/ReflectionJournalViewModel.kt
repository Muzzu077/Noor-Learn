package com.noorlearn.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noorlearn.domain.model.Reflection
import com.noorlearn.domain.repository.QuranRepository
import com.noorlearn.data.local.preferences.UserPreferencesDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ReflectionJournalViewModel @Inject constructor(
    private val quranRepository: QuranRepository,
    private val userPreferences: UserPreferencesDataStore
) : ViewModel() {

    private val _reflections = MutableStateFlow<List<Reflection>>(emptyList())
    val reflections: StateFlow<List<Reflection>> = _reflections.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadReflections()
    }

    fun loadReflections() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val uId = userPreferences.userId.first() ?: "demo-user"
                val result = quranRepository.getReflections(uId)
                _reflections.value = result.getOrDefault(emptyList())
            } catch (_: Exception) {
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addReflection(content: String, linkedAyahId: Int?) {
        if (content.isBlank()) return
        viewModelScope.launch {
            try {
                val uId = userPreferences.userId.first() ?: "demo-user"
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                val currentDate = sdf.format(Date())
                
                val newReflection = Reflection(
                    id = UUID.randomUUID().toString(),
                    userId = uId,
                    content = content.trim(),
                    date = currentDate,
                    linkedAyahId = linkedAyahId
                )
                
                val result = quranRepository.saveReflection(uId, newReflection)
                if (result.isSuccess) {
                    loadReflections()
                    userPreferences.updateStreak()
                    userPreferences.completeDailyJourneyTask("reflection_journal")
                }
            } catch (_: Exception) {}
        }
    }

    fun deleteReflection(reflectionId: String) {
        viewModelScope.launch {
            try {
                val uId = userPreferences.userId.first() ?: "demo-user"
                val result = quranRepository.deleteReflection(uId, reflectionId)
                if (result.isSuccess) {
                    loadReflections()
                }
            } catch (_: Exception) {}
        }
    }
}
