package com.noorlearn.domain.repository

import com.noorlearn.domain.model.Surah
import com.noorlearn.domain.model.Ayah
import com.noorlearn.domain.model.Bookmark
import com.noorlearn.domain.model.RecitationLog

interface QuranRepository {
    suspend fun getSurahs(): List<Surah>
    suspend fun getAyahs(surahId: Int): List<Ayah>
    suspend fun bookmarkAyah(userId: String, ayahId: Int): Result<Bookmark>
    suspend fun submitRecitation(
        userId: String,
        surahId: Int,
        ayahId: Int,
        transcribedText: String,
        accuracyScore: Float
    ): Result<RecitationLog>
}
