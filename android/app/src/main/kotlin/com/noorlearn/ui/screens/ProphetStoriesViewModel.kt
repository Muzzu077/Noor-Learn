package com.noorlearn.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noorlearn.domain.model.Prophet
import com.noorlearn.domain.repository.ProphetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProphetStoriesViewModel @Inject constructor(
    private val prophetRepository: ProphetRepository
) : ViewModel() {

    private val _prophets = MutableStateFlow<List<Prophet>>(emptyList())
    val prophets: StateFlow<List<Prophet>> = _prophets.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadProphets()
    }

    private fun loadProphets() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _prophets.value = prophetRepository.getProphetStories()
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }
}
