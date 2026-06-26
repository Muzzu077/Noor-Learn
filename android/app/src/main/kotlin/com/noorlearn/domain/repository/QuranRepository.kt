package com.noorlearn.domain.repository

import com.noorlearn.domain.model.Surah
import com.noorlearn.domain.model.Ayah
import com.noorlearn.domain.model.Bookmark
import com.noorlearn.domain.model.RecitationLog
import com.noorlearn.domain.model.Reflection

interface QuranRepository {
    suspend fun getSurahs(): List<Surah>
    suspend fun getAyahs(surahId: Int): List<Ayah>
    suspend fun getSurahAudioUrl(reciterId: Int, surahId: Int): Result<String>
    suspend fun getBookmarkedAyahIds(userId: String): Set<Int>
    suspend fun toggleBookmark(userId: String, ayahId: Int): Result<Boolean>
    suspend fun bookmarkAyah(userId: String, ayahId: Int): Result<Bookmark>
    suspend fun getBookmarkedIds(userId: String, type: String): Set<String>
    suspend fun toggleGenericBookmark(userId: String, type: String, referenceId: String): Result<Boolean>
    suspend fun submitRecitation(
        userId: String,
        surahId: Int,
        ayahId: Int,
        transcribedText: String,
        accuracyScore: Float,
        tipAi: String?
    ): Result<RecitationLog>
    suspend fun getRecitationLogs(userId: String): Result<List<RecitationLog>>
    suspend fun getReflections(userId: String): Result<List<Reflection>>
    suspend fun saveReflection(userId: String, reflection: Reflection): Result<Reflection>
    suspend fun deleteReflection(userId: String, reflectionId: String): Result<Unit>
}
