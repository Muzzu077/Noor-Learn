package com.noorlearn.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import com.noorlearn.domain.model.Surah
import com.noorlearn.domain.model.Ayah
import com.noorlearn.domain.model.Bookmark
import com.noorlearn.domain.model.RecitationLog
import com.noorlearn.domain.repository.QuranRepository
import com.noorlearn.data.local.dao.SurahDao
import com.noorlearn.data.local.dao.AyahDao
import com.noorlearn.data.local.entity.SurahEntity
import com.noorlearn.data.local.entity.AyahEntity
import com.noorlearn.data.remote.dto.AyahDto
import com.noorlearn.data.remote.dto.BookmarkDto
import com.noorlearn.data.remote.dto.SurahDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import javax.inject.Inject

class QuranRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val surahDao: SurahDao,
    private val ayahDao: AyahDao,
    private val supabaseClient: SupabaseClient,
    private val quranDataIngestor: QuranDataIngestor
) : QuranRepository {

    private suspend fun populateFromAssetsIfNeeded() {
        quranDataIngestor.ingestFromAssetsIfNeeded(context)
    }

    override suspend fun getSurahs(): List<Surah> = withContext(Dispatchers.IO) {
        populateFromAssetsIfNeeded()
        
        val localSurahs = surahDao.getAllSurahs()
        if (localSurahs.isNotEmpty()) {
            return@withContext localSurahs.map { it.toDomain() }
        }

        try {
            val remoteSurahs = supabaseClient.postgrest["surahs"].select().decodeList<SurahDto>()
            android.util.Log.d("QuranRepo", "getSurahs() - Found ${remoteSurahs.size} remote surahs")
            val entities = remoteSurahs.map { dto ->
                SurahEntity(
                    id = dto.id,
                    nameArabic = dto.arabicName,
                    nameEnglishTranslation = dto.name,
                    nameRoman = dto.nameRoman,
                    revelationType = dto.revelationType,
                    numberOfAyahs = dto.totalAyah
                )
            }
            surahDao.insertSurahs(entities)
            remoteSurahs.map { dto ->
                Surah(
                    id = dto.id,
                    name = dto.name,
                    nameRoman = dto.nameRoman,
                    arabicName = dto.arabicName,
                    revelationType = dto.revelationType,
                    totalAyah = dto.totalAyah,
                    meaning = dto.meaning
                )
            }
        } catch (e: Exception) {
            android.util.Log.e("QuranRepo", "Error in getSurahs()", e)
            emptyList()
        }
    }

    override suspend fun getAyahs(surahId: Int): List<Ayah> = withContext(Dispatchers.IO) {
        populateFromAssetsIfNeeded()
        
        val localAyahs = ayahDao.getAyahsBySurah(surahId)
        if (localAyahs.isNotEmpty()) {
            return@withContext localAyahs.map { it.toDomain() }
        }

        try {
            val remoteAyahs = supabaseClient.postgrest["ayahs"]
                .select { filter { eq("surah_id", surahId) } }
                .decodeList<AyahDto>()

            android.util.Log.d("QuranRepo", "getAyahs($surahId) - Found ${remoteAyahs.size} remote ayahs")

            val entities = remoteAyahs.map { dto ->
                AyahEntity(
                    id = dto.id,
                    surahId = dto.surahId,
                    ayahNumber = dto.ayahNumber,
                    textArabic = dto.arabicText,
                    textTranslation = dto.translationEn,
                    textTransliteration = dto.transliteration,
                    audioUrl = dto.audioUrl
                )
            }
            ayahDao.insertAyahs(entities)
            entities.map { it.toDomain() }
        } catch (e: Exception) {
            android.util.Log.e("QuranRepo", "Error in getAyahs($surahId)", e)
            emptyList()
        }
    }

    override suspend fun getSurahAudioUrl(reciterId: Int, surahId: Int): Result<String> =
        withContext(Dispatchers.IO) {
            try {
                val client = HttpClient(Android)
                val response = client.get(
                    "https://api.quran.com/api/v4/chapter_recitations/$reciterId/$surahId"
                )
                val json = JSONObject(response.bodyAsText())
                val audioUrl = json.getJSONObject("audio_file").getString("audio_url")
                client.close()
                Result.success(audioUrl)
            } catch (e: Exception) {
                android.util.Log.e("QuranRepo", "Error fetching audio for surah $surahId", e)
                Result.failure(e)
            }
        }

    private fun SurahEntity.toDomain() = Surah(
        id = id,
        name = nameEnglishTranslation,
        nameRoman = nameRoman,
        arabicName = nameArabic,
        revelationType = revelationType,
        totalAyah = numberOfAyahs,
        meaning = ""
    )

    private fun AyahEntity.toDomain() = Ayah(
        id = id,
        surahId = surahId,
        ayahNumber = ayahNumber,
        arabicText = textArabic,
        translationEn = textTranslation,
        transliteration = textTransliteration,
        translationUr = "",
        tafsirShort = "",
        audioUrl = audioUrl
    )

    override suspend fun getBookmarkedAyahIds(userId: String): Set<Int> = withContext(Dispatchers.IO) {
        try {
            val bookmarks = supabaseClient.postgrest["bookmarks"]
                .select { filter { eq("user_id", userId) } }
                .decodeList<BookmarkDto>()
            bookmarks.map { it.ayahId }.toSet()
        } catch (e: Exception) {
            android.util.Log.e("QuranRepo", "Error fetching bookmarks", e)
            emptySet()
        }
    }

    override suspend fun toggleBookmark(userId: String, ayahId: Int): Result<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                // Check if bookmark exists
                val existing = supabaseClient.postgrest["bookmarks"]
                    .select { filter { eq("user_id", userId); eq("ayah_id", ayahId) } }
                    .decodeList<BookmarkDto>()

                if (existing.isNotEmpty()) {
                    // Remove bookmark
                    supabaseClient.postgrest["bookmarks"]
                        .delete { filter { eq("user_id", userId); eq("ayah_id", ayahId) } }
                    Result.success(false) // now unbookmarked
                } else {
                    // Add bookmark
                    supabaseClient.postgrest["bookmarks"]
                        .insert(BookmarkDto(userId = userId, ayahId = ayahId))
                    Result.success(true) // now bookmarked
                }
            } catch (e: Exception) {
                android.util.Log.e("QuranRepo", "Error toggling bookmark", e)
                Result.failure(e)
            }
        }

    override suspend fun bookmarkAyah(userId: String, ayahId: Int): Result<Bookmark> {
        return Result.success(Bookmark(1L, userId, ayahId, System.currentTimeMillis().toString()))
    }

    override suspend fun submitRecitation(
        userId: String,
        surahId: Int,
        ayahId: Int,
        transcribedText: String,
        accuracyScore: Float
    ): Result<RecitationLog> {
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
