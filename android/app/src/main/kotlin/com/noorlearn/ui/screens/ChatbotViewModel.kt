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

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

@HiltViewModel
class ChatbotViewModel @Inject constructor(
    private val askChatbotUseCase: AskChatbotUseCase
) : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun sendMessage(question: String) {
        if (question.isBlank()) return

        val currentMessages = _messages.value.toMutableList()
        currentMessages.add(ChatMessage(text = question, isUser = true))
        _messages.value = currentMessages

        viewModelScope.launch {
            _isLoading.value = true
            val result = askChatbotUseCase(question)
            
            val newMessages = _messages.value.toMutableList()
            if (result.isSuccess) {
                newMessages.add(ChatMessage(text = result.getOrNull() ?: "Error parsing response.", isUser = false))
            } else {
                val errorMsg = result.exceptionOrNull()?.message ?: "Sorry, I could not process your request. Check your connection."
                newMessages.add(ChatMessage(text = errorMsg, isUser = false))
            }
            _messages.value = newMessages
            _isLoading.value = false
        }
    }

    fun clearChat() {
        _messages.value = emptyList()
    }
}
