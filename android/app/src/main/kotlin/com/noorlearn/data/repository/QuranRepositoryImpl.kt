package com.noorlearn.data.repository

import com.noorlearn.domain.model.Surah
import com.noorlearn.domain.model.Ayah
import com.noorlearn.domain.model.Bookmark
import com.noorlearn.domain.model.RecitationLog
import com.noorlearn.domain.repository.QuranRepository
import com.noorlearn.data.local.dao.SurahDao
import com.noorlearn.data.local.dao.AyahDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class QuranRepositoryImpl @Inject constructor(
    private val surahDao: SurahDao,
    private val ayahDao: AyahDao
) : QuranRepository {
    
    override suspend fun getSurahs(): List<Surah> = withContext(Dispatchers.IO) {
        // Here we would typically check local DB first, if empty fetch from remote, save to local, then return local
        surahDao.getAllSurahs().map { entity ->
            Surah(
                id = entity.id,
                name = entity.nameEnglishTranslation,
                arabicName = entity.nameArabic,
                revelationType = entity.revelationType,
                totalAyah = entity.numberOfAyahs,
                meaning = ""
            )
        }
    }

    override suspend fun getAyahs(surahId: Int): List<Ayah> = withContext(Dispatchers.IO) {
        ayahDao.getAyahsBySurah(surahId).map { entity ->
            Ayah(
                id = entity.id,
                surahId = entity.surahId,
                ayahNumber = entity.ayahNumber,
                arabicText = entity.textArabic,
                translationEn = entity.textTranslation,
                translationUr = "",
                tafsirShort = "",
                audioUrl = entity.audioUrl
            )
        }
    }

    override suspend fun bookmarkAyah(userId: String, ayahId: Int): Result<Bookmark> {
        // Implementation for bookmarking, saving to Supabase and Room
        return Result.success(Bookmark(1L, userId, ayahId, System.currentTimeMillis().toString()))
    }

    override suspend fun submitRecitation(
        userId: String,
        surahId: Int,
        ayahId: Int,
        transcribedText: String,
        accuracyScore: Float
    ): Result<RecitationLog> {
        // 1. Calculate accuracy locally
        // 2. Call Edge Function (via AI proxy) for Tip/Explanation
        // 3. Save to Room
        // 4. Sync to Supabase
        return Result.success(RecitationLog(
            id = 1L,
            userId = userId,
            ayahId = ayahId,
            transcribedText = transcribedText,
            accuracyScore = accuracyScore,
            feedbackText = "Mashallah, try to focus on the Makharij of letter Raa.",
            createdAt = System.currentTimeMillis().toString()
        ))
    }
}
