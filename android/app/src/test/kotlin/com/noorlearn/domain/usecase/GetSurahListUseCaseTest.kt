package com.noorlearn.domain.usecase

import com.noorlearn.domain.model.Surah
import com.noorlearn.domain.repository.QuranRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class GetSurahListUseCaseTest {

    @Mock
    private lateinit var quranRepository: QuranRepository
    private lateinit var getSurahListUseCase: GetSurahListUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        getSurahListUseCase = GetSurahListUseCase(quranRepository)
    }

    @Test
    fun `invoke should return list of surahs from repository`() = runBlocking {
        // Arrange
        val expectedSurahs = listOf(
            Surah(1, "الفاتحة", "The Opening", "Meccan", 7),
            Surah(2, "البقرة", "The Cow", "Medinan", 286)
        )
        `when`(quranRepository.getSurahs()).thenReturn(expectedSurahs)

        // Act
        val actualSurahs = getSurahListUseCase()

        // Assert
        assertEquals(expectedSurahs, actualSurahs)
    }
}
