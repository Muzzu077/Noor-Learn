package com.noorlearn.ui.screens

import com.noorlearn.data.local.preferences.UserPreferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class DailyJourneyViewModelTest {

    @Mock
    private lateinit var userPreferences: UserPreferencesDataStore
    
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: DailyJourneyViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        
        `when`(userPreferences.streakCount).thenReturn(flowOf(3))
        `when`(userPreferences.dailyJourneyTasksCompleted).thenReturn(flowOf(setOf("morning_adhkar")))
        
        viewModel = DailyJourneyViewModel(userPreferences)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `viewModel should load streak count and completed tasks from preferences`() = runTest {
        // Collect state flows in background to trigger WhileSubscribed upstream collection
        val collectJob1 = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.streakCount.collect {}
        }
        val collectJob2 = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.completedTasks.collect {}
        }
        
        testDispatcher.scheduler.advanceUntilIdle()
        
        assertEquals(3, viewModel.streakCount.value)
        assertTrue(viewModel.completedTasks.value.contains("morning_adhkar"))
    }

    @Test
    fun `noorPoints should calculate dynamically based on streak and completed tasks`() = runTest {
        val collectJob1 = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.streakCount.collect {}
        }
        val collectJob2 = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.completedTasks.collect {}
        }
        val collectJob3 = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.noorPoints.collect {}
        }
        
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Base points (200) + streak (3 * 20) + completed tasks (1 * 10) = 270
        assertEquals(270, viewModel.noorPoints.value)
    }

    @Test
    fun `toggleTask should delegate to userPreferences and update streak`() = runTest {
        viewModel.toggleTask("surah_reading")
        testDispatcher.scheduler.advanceUntilIdle()
        
        verify(userPreferences).toggleDailyJourneyTask("surah_reading")
        verify(userPreferences).updateStreak()
    }
}
