package com.noorlearn.data.repository

import com.noorlearn.domain.repository.ChatRepository
import com.noorlearn.data.remote.EdgeFunctionService
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val edgeFunctionService: EdgeFunctionService
) : ChatRepository {

    override suspend fun askQuestion(question: String): Result<String> {
        return edgeFunctionService.askChatbot(question)
    }
}
