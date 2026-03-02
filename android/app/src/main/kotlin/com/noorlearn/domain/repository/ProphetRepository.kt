package com.noorlearn.domain.repository

import com.noorlearn.domain.model.Prophet

interface ProphetRepository {
    suspend fun getProphetStories(): List<Prophet>
    suspend fun getStory(prophetId: String): Prophet
}
