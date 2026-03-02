package com.noorlearn.domain.usecase

import com.noorlearn.domain.repository.ChatRepository
import javax.inject.Inject

class AskChatbotUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(question: String): Result<String> {
        return chatRepository.askQuestion(question)
    }
}
