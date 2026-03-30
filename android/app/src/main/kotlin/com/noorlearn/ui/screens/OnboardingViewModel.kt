package com.noorlearn.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noorlearn.data.local.preferences.UserPreferencesDataStore
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val dataStore: UserPreferencesDataStore,
    private val supabaseClient: SupabaseClient
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    @Serializable
    data class UserProfileUpdate(
        val learning_level: String,
        val primary_goal: String,
        val daily_commitment: String
    )

    fun completeOnboarding(level: String, goal: String, commitment: String, onComplete: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            
            // 1. Save locally for fast access
            dataStore.saveOnboardingPreferences(level, goal, commitment)
            
            // 2. Save remotely to Supabase if logged in
            val userId = supabaseClient.auth.currentUserOrNull()?.id
            if (userId != null) {
                try {
                    val updateData = UserProfileUpdate(level, goal, commitment)
                    supabaseClient.postgrest["user_profiles"]
                        .update(updateData) {
                            filter { eq("id", userId) }
                        }
                } catch (e: Exception) {
                    android.util.Log.e("OnboardingVM", "Failed to update remote profile", e)
                    // If it's a new user, they might not have a profile row yet. Let's try upsert.
                    try {
                        @Serializable
                        data class UserProfileInsert(
                            val id: String,
                            val learning_level: String,
                            val primary_goal: String,
                            val daily_commitment: String
                        )
                        supabaseClient.postgrest["user_profiles"].upsert(
                            UserProfileInsert(userId, level, goal, commitment)
                        )
                    } catch (e2: Exception) {
                        android.util.Log.e("OnboardingVM", "Failed to upsert remote profile", e2)
                    }
                }
            }
            
            _isLoading.value = false
            onComplete()
        }
    }
}
