package com.noorlearn.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noorlearn.domain.usecase.AskChatbotUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatbotViewModel @Inject constructor(
    private val askChatbotUseCase: AskChatbotUseCase
) : ViewModel() {

    private val _messages = MutableStateFlow<List<Pair<String, Boolean>>>(emptyList())
    val messages: StateFlow<List<Pair<String, Boolean>>> = _messages.asStateFlow() // Pair(message, isUser)

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun sendMessage(question: String) {
        if (question.isBlank()) return

        val currentMessages = _messages.value.toMutableList()
        currentMessages.add(Pair(question, true))
        _messages.value = currentMessages

        viewModelScope.launch {
            _isLoading.value = true
            val result = askChatbotUseCase(question)
            
            val newMessages = _messages.value.toMutableList()
            if (result.isSuccess) {
                newMessages.add(Pair(result.getOrNull() ?: "Error parsing response.", false))
            } else {
                val errorMsg = result.exceptionOrNull()?.message ?: "Sorry, I could not process your request. Check your connection."
                newMessages.add(Pair(errorMsg, false))
            }
            _messages.value = newMessages
            _isLoading.value = false
        }
    }
}
