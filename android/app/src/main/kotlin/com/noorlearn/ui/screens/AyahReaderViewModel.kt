package com.noorlearn.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noorlearn.domain.model.Ayah
import com.noorlearn.domain.repository.QuranRepository
import com.noorlearn.domain.usecase.AskChatbotUseCase
import com.noorlearn.data.local.preferences.UserPreferencesDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class Reciter(val name: String, val id: Int)

val RECITERS = listOf(
    Reciter("Mishary Rashid Alafasy", 7),
    Reciter("Abdul Basit Abdul Samad", 1),
    Reciter("Abu Bakr Al-Shatri", 4),
    Reciter("Abdur-Rahman as-Sudais", 3),
    Reciter("Hani Ar-Rifai", 8),
    Reciter("Saad Al-Ghamdi", 6)
)

@HiltViewModel
class AyahReaderViewModel @Inject constructor(
    private val quranRepository: QuranRepository,
    private val askChatbotUseCase: AskChatbotUseCase,
    private val userPreferences: UserPreferencesDataStore
) : ViewModel() {

    private val _ayahs = MutableStateFlow<List<Ayah>>(emptyList())
    val ayahs: StateFlow<List<Ayah>> = _ayahs.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Audio state
    private val _audioUrl = MutableStateFlow<String?>(null)
    val audioUrl: StateFlow<String?> = _audioUrl.asStateFlow()

    private val _isAudioLoading = MutableStateFlow(false)
    val isAudioLoading: StateFlow<Boolean> = _isAudioLoading.asStateFlow()

    private val _selectedReciter = MutableStateFlow(RECITERS[0])
    val selectedReciter: StateFlow<Reciter> = _selectedReciter.asStateFlow()

    // Bookmark state
    private val _bookmarkedAyahIds = MutableStateFlow<Set<Int>>(emptySet())
    val bookmarkedAyahIds: StateFlow<Set<Int>> = _bookmarkedAyahIds.asStateFlow()

    // Explain This state
    private val _explainAyahId = MutableStateFlow<Int?>(null)
    val explainAyahId: StateFlow<Int?> = _explainAyahId.asStateFlow()

    private val _explanation = MutableStateFlow<String?>(null)
    val explanation: StateFlow<String?> = _explanation.asStateFlow()

    private val _isExplaining = MutableStateFlow(false)
    val isExplaining: StateFlow<Boolean> = _isExplaining.asStateFlow()

    private var currentSurahId: Int = 0
    private var currentUserId: String? = null
    private var isDemoUser = false

    fun loadAyahs(surahId: Int) {
        currentSurahId = surahId
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                userPreferences.updateStreak()
                _ayahs.value = quranRepository.getAyahs(surahId)
                if (_ayahs.value.isEmpty()) {
                    _error.value = "No ayahs found for this surah. Data may not be loaded yet."
                }
            } catch (e: Exception) {
                _error.value = "Failed to load ayahs: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
        loadAudio(surahId, _selectedReciter.value.id)
        loadBookmarks()
    }

    private fun loadBookmarks() {
        viewModelScope.launch {
            val userId = userPreferences.userId.first()
            currentUserId = userId
            isDemoUser = userId?.startsWith("demo") == true

            // Always load from DataStore first (works for both demo and real users)
            _bookmarkedAyahIds.value = userPreferences.bookmarkedAyahIds.first()

            // For real users, also merge from Supabase
            if (userId != null && !isDemoUser) {
                try {
                    val remote = quranRepository.getBookmarkedAyahIds(userId)
                    if (remote.isNotEmpty()) {
                        _bookmarkedAyahIds.value = _bookmarkedAyahIds.value + remote
                    }
                } catch (_: Exception) { }
            }
        }
    }

    fun toggleBookmark(ayahId: Int) {
        viewModelScope.launch {
            // Always persist locally via DataStore
            userPreferences.toggleLocalBookmark(ayahId)
            _bookmarkedAyahIds.value = userPreferences.bookmarkedAyahIds.first()

            // For real users, also sync to Supabase
            val userId = currentUserId
            if (userId != null && !isDemoUser) {
                quranRepository.toggleBookmark(userId, ayahId)
            }
        }
    }

    fun explainAyah(ayah: Ayah) {
        _explainAyahId.value = ayah.id
        _explanation.value = null
        _isExplaining.value = true
        viewModelScope.launch {
            val prompt = "Explain this Qur'an verse briefly (3-4 sentences). Give the historical context and main lesson:\n\nArabic: ${ayah.arabicText}\nTranslation: ${ayah.translationEn}\n\nKeep it simple and clear."
            val result = askChatbotUseCase(prompt)
            _explanation.value = result.getOrElse { "Could not load explanation. Please try again." }
            _isExplaining.value = false
        }
    }

    fun dismissExplanation() {
        _explainAyahId.value = null
        _explanation.value = null
    }

    fun loadAudio(surahId: Int, reciterId: Int) {
        viewModelScope.launch {
            _isAudioLoading.value = true
            _audioUrl.value = null
            val result = quranRepository.getSurahAudioUrl(reciterId, surahId)
            if (result.isSuccess) {
                _audioUrl.value = result.getOrNull()
            }
            _isAudioLoading.value = false
        }
    }

    fun selectReciter(reciter: Reciter) {
        _selectedReciter.value = reciter
        if (currentSurahId > 0) {
            loadAudio(currentSurahId, reciter.id)
        }
    }
}
