package com.noorlearn.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noorlearn.BuildConfig
import com.noorlearn.domain.model.User
import com.noorlearn.domain.repository.AuthRepository
import com.noorlearn.data.local.preferences.UserPreferencesDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userPreferences: UserPreferencesDataStore
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    init {
        checkSession()
    }
    
    private fun checkSession() {
        viewModelScope.launch {
            _isLoading.value = true
            val savedUserId = userPreferences.userId.first()
            if (BuildConfig.DEBUG && savedUserId?.startsWith("demo") == true) {
                val savedName = userPreferences.userName.first()
                _user.value = com.noorlearn.domain.model.User(
                    id = savedUserId,
                    name = savedName,
                    email = "demo@noorlearn.app",
                    roleMode = "student",
                    streakDays = 0,
                    isPremium = true,
                    createdAt = "2026-03-25"
                )
            } else {
                _user.value = authRepository.getCurrentUser()
            }
            _isLoading.value = false
        }
    }

    fun signIn(email: String, pass: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            var result = authRepository.signIn(email, pass)
            
            // Demo bypass: ONLY in debug builds. Never ships to production.
            if (BuildConfig.DEBUG && result.isFailure && email == "demo@noorlearn.app") {
                android.util.Log.d("AuthViewModel", "[DEBUG] Demo bypass active — injecting mock user.")
                val demoUser = com.noorlearn.domain.model.User(
                    id = "demo-user-id-555",
                    name = "Demo User",
                    email = "demo@noorlearn.app",
                    roleMode = "student",
                    streakDays = 0,
                    isPremium = true,
                    createdAt = "2026-03-25"
                )
                _user.value = demoUser
                userPreferences.saveUserId(demoUser.id)
                userPreferences.saveUserName(demoUser.name)
                onSuccess()
                _isLoading.value = false
                return@launch
            }
            
            if (result.isSuccess) {
                val user = result.getOrNull()
                _user.value = user
                user?.let {
                    userPreferences.saveUserId(it.id)
                    userPreferences.saveUserName(it.name)
                }
                onSuccess()
            } else {
                _error.value = result.exceptionOrNull()?.message ?: "Login failed. Please check your credentials."
            }
            _isLoading.value = false
        }
    }

    fun signUp(email: String, pass: String, name: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            val result = authRepository.signUp(email, pass, name)

            if (result.isSuccess) {
                val user = result.getOrNull()
                _user.value = user
                user?.let {
                    userPreferences.saveUserId(it.id)
                    userPreferences.saveUserName(it.name)
                }
                onSuccess()
            } else {
                _error.value = result.exceptionOrNull()?.message ?: "Signup failed"
            }
            _isLoading.value = false
        }
    }

    fun sendPasswordResetEmail(email: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            val result = authRepository.sendPasswordResetEmail(email)
            if (result.isSuccess) {
                onSuccess()
            } else {
                _error.value = result.exceptionOrNull()?.message ?: "Failed to send password reset email."
            }
            _isLoading.value = false
        }
    }

    fun signInWithGoogle(idToken: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            val result = authRepository.signInWithGoogle(idToken)
            if (result.isSuccess) {
                val user = result.getOrNull()
                _user.value = user
                user?.let {
                    userPreferences.saveUserId(it.id)
                    userPreferences.saveUserName(it.name)
                }
                onSuccess()
            } else {
                _error.value = result.exceptionOrNull()?.message ?: "Google login failed."
            }
            _isLoading.value = false
        }
    }
}
