package com.noorlearn.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noorlearn.data.local.preferences.UserPreferencesDataStore
import com.noorlearn.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userPreferences: UserPreferencesDataStore,
    private val authRepository: AuthRepository
) : ViewModel() {

    val userName: StateFlow<String> = userPreferences.userName
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "Learner")

    val surahsRead: StateFlow<Int> = userPreferences.surahsRead
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val bookmarksCount: StateFlow<Int> = userPreferences.bookmarksCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val studyMinutes: StateFlow<Int> = userPreferences.studyMinutes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val streakCount: StateFlow<Int> = userPreferences.streakCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    private val _isSignedOut = MutableStateFlow(false)
    val isSignedOut: StateFlow<Boolean> = _isSignedOut.asStateFlow()

    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
            userPreferences.clearAll()
            _isSignedOut.value = true
        }
    }
}
