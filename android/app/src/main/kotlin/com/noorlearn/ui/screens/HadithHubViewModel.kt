package com.noorlearn.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noorlearn.domain.model.Hadith
import com.noorlearn.domain.repository.HadithRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HadithHubViewModel @Inject constructor(
    private val hadithRepository: HadithRepository
) : ViewModel() {

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
                // Fetching from a default source or all
                _hadiths.value = hadithRepository.getHadithsBySource("Bukhari") 
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }
}
