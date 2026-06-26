package com.noorlearn.ui.screens

import com.noorlearn.data.local.preferences.UserPreferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class VocabularyBuilderViewModelTest {

    @Mock
    private lateinit var userPreferences: UserPreferencesDataStore

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: VocabularyBuilderViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)

        `when`(userPreferences.masteredVocabIds).thenReturn(flowOf(setOf(1, 3)))

        viewModel = VocabularyBuilderViewModel(userPreferences)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `viewModel should initialize words with correct mastery status`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        val words = viewModel.words.value
        assertEquals(10, words.size)

        // Word with ID 1 and 3 should be mastered
        val word1 = words.first { it.id == 1 }
        val word2 = words.first { it.id == 2 }
        val word3 = words.first { it.id == 3 }

        assertTrue(word1.isMastered)
        assertFalse(word2.isMastered)
        assertTrue(word3.isMastered)
    }

    @Test
    fun `toggleMastery should invoke userPreferences toggle and update streak`() = runTest {
        viewModel.toggleMastery(2)
        testDispatcher.scheduler.advanceUntilIdle()

        verify(userPreferences).toggleVocabMastery(2)
        verify(userPreferences).updateStreak()
    }
}
