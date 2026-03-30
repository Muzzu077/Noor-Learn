package com.noorlearn.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noorlearn.domain.model.Hadith
import com.noorlearn.domain.repository.HadithRepository
import com.noorlearn.data.local.preferences.UserPreferencesDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HadithHubViewModel @Inject constructor(
    private val hadithRepository: HadithRepository,
    private val userPreferences: UserPreferencesDataStore
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
}
