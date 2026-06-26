package com.noorlearn.data.repository

import com.noorlearn.domain.model.Prophet
import io.github.jan.supabase.SupabaseClient
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class ProphetRepositoryImplTest {

    @Mock
    private lateinit var supabaseClient: SupabaseClient
    
    private lateinit var repository: ProphetRepositoryImpl

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        repository = ProphetRepositoryImpl(supabaseClient)
    }

    @Test
    fun `getProphetStories should return fallback list when supabase throws exception`() = runBlocking {
        // Act
        val stories = repository.getProphetStories()

        // Assert
        assertNotNull(stories)
        assertEquals(6, stories.size)
        assertEquals("Adam (AS)", stories[0].name)
        assertEquals("Muhammad (SAW) - Muhammad", stories[5].name)
    }

    @Test
    fun `getStory should return correct prophet from fallback list`() = runBlocking {
        // Act
        val prophet = repository.getStory("4")

        // Assert
        assertNotNull(prophet)
        assertEquals("Musa (AS) - Moses", prophet.name)
        assertEquals("آدَم", repository.getStory("1").arabicName)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `getStory should throw IllegalArgumentException when id is invalid`(): Unit = runBlocking {
        // Act
        repository.getStory("999")
    }
}
