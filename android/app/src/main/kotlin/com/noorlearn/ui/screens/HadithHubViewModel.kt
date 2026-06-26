package com.noorlearn.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noorlearn.domain.model.Hadith
import com.noorlearn.domain.repository.HadithRepository
import com.noorlearn.data.local.preferences.UserPreferencesDataStore
import com.noorlearn.domain.usecase.AskChatbotUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HadithHubViewModel @Inject constructor(
    private val hadithRepository: HadithRepository,
    private val userPreferences: UserPreferencesDataStore,
    private val askChatbotUseCase: AskChatbotUseCase
) : ViewModel() {

    private val _allHadiths = MutableStateFlow<List<Hadith>>(emptyList())
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _hadiths = MutableStateFlow<List<Hadith>>(emptyList())
    val hadiths: StateFlow<List<Hadith>> = _hadiths.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadHadiths()
    }

    private fun loadHadiths() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                userPreferences.updateStreak()
                _allHadiths.value = hadithRepository.getHadithsBySource("All")
                _hadiths.value = _allHadiths.value
                userPreferences.completeDailyJourneyTask("hadith_day")
            } catch (_: Exception) { }
            finally {
                _isLoading.value = false
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        filterHadiths(query)
    }

    private fun filterHadiths(query: String) {
        if (query.isBlank()) {
            _hadiths.value = _allHadiths.value
        } else {
            val lowerCaseQuery = query.lowercase()
            _hadiths.value = _allHadiths.value.filter {
                it.translationEn.lowercase().contains(lowerCaseQuery) ||
                it.arabicText.contains(query) ||
                it.source.lowercase().contains(lowerCaseQuery) ||
                it.topic.lowercase().contains(lowerCaseQuery)
            }
        }
    }

    // Explain Hadith state
    private val _explainHadithId = MutableStateFlow<Long?>(null)
    val explainHadithId: StateFlow<Long?> = _explainHadithId.asStateFlow()

    private val _explanation = MutableStateFlow<String?>(null)
    val explanation: StateFlow<String?> = _explanation.asStateFlow()

    private val _isExplaining = MutableStateFlow(false)
    val isExplaining: StateFlow<Boolean> = _isExplaining.asStateFlow()

    fun explainHadith(hadith: Hadith) {
        _explainHadithId.value = hadith.id
        _explanation.value = null
        _isExplaining.value = true
        viewModelScope.launch {
            val prompt = "Explain this Hadith briefly (3-4 sentences). Give the context, lessons, and how to apply it in daily life:\n\nArabic: ${hadith.arabicText}\nTranslation: ${hadith.translationEn}\nSource: ${hadith.source}\nGrade: ${hadith.grade}\n\nKeep it simple and practical."
            val result = askChatbotUseCase(prompt)
            _explanation.value = result.getOrElse { "Could not load explanation. Please try again." }
            _isExplaining.value = false
        }
    }

    fun dismissExplanation() {
        _explainHadithId.value = null
        _explanation.value = null
    }
}
