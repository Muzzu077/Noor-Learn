package com.noorlearn.domain.repository

interface ChatRepository {
    suspend fun askQuestion(question: String): Result<String>
}
