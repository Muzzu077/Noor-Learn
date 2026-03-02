package com.noorlearn.data.repository

import com.noorlearn.domain.model.Prophet
import com.noorlearn.domain.repository.ProphetRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProphetRepositoryImpl @Inject constructor() : ProphetRepository {
    override suspend fun getProphetStories(): List<Prophet> = withContext(Dispatchers.IO) {
        listOf(
            Prophet(
                id = 1,
                name = "Prophet Adam (AS)",
                arabicName = "آدم عليه السلام",
                period = "Beginning of humanity",
                summary = "Prophet Adam was the first human created...",
                moralLesson = "We learn humility, repentance...",
                imageUrl = "",
                audioUrl = ""
            ),
            Prophet(
                id = 25,
                name = "Prophet Muhammad (SAW)",
                arabicName = "محمد صلى الله عليه وسلم",
                period = "570-632 CE",
                summary = "Born in Mecca, received the Quran...",
                moralLesson = "Follow the Sunnah...",
                imageUrl = "",
                audioUrl = ""
            )
        )
    }

    override suspend fun getStory(prophetId: String): Prophet = withContext(Dispatchers.IO) {
        getProphetStories().first { it.id.toString() == prophetId }
    }
}
