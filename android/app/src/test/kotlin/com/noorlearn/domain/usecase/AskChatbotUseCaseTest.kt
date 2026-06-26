package com.noorlearn.domain.usecase

import com.noorlearn.domain.repository.ChatRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class AskChatbotUseCaseTest {

    @Mock
    private lateinit var chatRepository: ChatRepository
    private lateinit var askChatbotUseCase: AskChatbotUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        askChatbotUseCase = AskChatbotUseCase(chatRepository)
    }

    @Test
    fun `invoke should return success result from repository`() = runBlocking {
        // Arrange
        val question = "What is the meaning of Noor?"
        val expectedAnswer = "Noor means Light in Arabic."
        `when`(chatRepository.askQuestion(question)).thenReturn(Result.success(expectedAnswer))

        // Act
        val result = askChatbotUseCase(question)

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(expectedAnswer, result.getOrNull())
    }

    @Test
    fun `invoke should return failure result from repository when error occurs`() = runBlocking {
        // Arrange
        val question = "What is the meaning of Noor?"
        val exception = RuntimeException("Network error")
        `when`(chatRepository.askQuestion(question)).thenReturn(Result.failure(exception))

        // Act
        val result = askChatbotUseCase(question)

        // Assert
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}
