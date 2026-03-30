package com.noorlearn.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noorlearn.domain.model.Ayah
import com.noorlearn.domain.repository.QuranRepository
import com.noorlearn.data.local.preferences.UserPreferencesDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(
    private val quranRepository: QuranRepository,
    private val userPreferences: UserPreferencesDataStore
) : ViewModel() {

    private val _bookmarkedAyahs = MutableStateFlow<List<Ayah>>(emptyList())
    val bookmarkedAyahs: StateFlow<List<Ayah>> = _bookmarkedAyahs.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadBookmarkedAyahs()
    }

    private fun loadBookmarkedAyahs() {
        viewModelScope.launch {
            _isLoading.value = true
            val ids = userPreferences.bookmarkedAyahIds.first()
            if (ids.isEmpty()) {
                _bookmarkedAyahs.value = emptyList()
                _isLoading.value = false
                return@launch
            }

            // Load all surahs to get ayahs for each bookmarked ID
            val allAyahs = mutableListOf<Ayah>()
            val surahIds = ids.map { ayahId ->
                // Rough surah lookup: load surahs, find which surah contains this ayah
                // For efficiency, load ayahs by surah and filter
                ayahId
            }

            // Get unique surah IDs by checking all surahs
            val surahs = quranRepository.getSurahs()
            var runningTotal = 0
            for (surah in surahs) {
                val surahStart = runningTotal + 1
                val surahEnd = runningTotal + surah.totalAyah
                val matchingIds = ids.filter { it in surahStart..surahEnd }
                if (matchingIds.isNotEmpty()) {
                    val ayahs = quranRepository.getAyahs(surah.id)
                    allAyahs.addAll(ayahs.filter { it.id in ids })
                }
                runningTotal = surahEnd
            }

            _bookmarkedAyahs.value = allAyahs
            _isLoading.value = false
        }
    }

    fun removeBookmark(ayahId: Int) {
        viewModelScope.launch {
            userPreferences.toggleLocalBookmark(ayahId)
            _bookmarkedAyahs.value = _bookmarkedAyahs.value.filter { it.id != ayahId }
        }
    }
}
