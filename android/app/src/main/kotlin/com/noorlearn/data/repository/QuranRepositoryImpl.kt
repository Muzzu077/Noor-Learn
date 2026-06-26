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
import com.noorlearn.data.local.dao.ReflectionDao
import com.noorlearn.data.local.entity.ReflectionEntity
import com.noorlearn.domain.model.Reflection

class QuranRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val surahDao: SurahDao,
    private val ayahDao: AyahDao,
    private val reflectionDao: ReflectionDao,
    private val supabaseClient: SupabaseClient,
    private val quranDataIngestor: QuranDataIngestor
) : QuranRepository {

    private val httpClient = HttpClient(Android) {
        engine {
            connectTimeout = 15_000
            socketTimeout = 30_000
        }
    }

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
                val response = httpClient.get(
                    "https://api.quran.com/api/v4/chapter_recitations/$reciterId/$surahId"
                )
                val json = JSONObject(response.bodyAsText())
                val audioUrl = json.getJSONObject("audio_file").getString("audio_url")
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
        getBookmarkedIds(userId, "ayah").mapNotNull { it.toIntOrNull() }.toSet()
    }

    override suspend fun toggleBookmark(userId: String, ayahId: Int): Result<Boolean> = withContext(Dispatchers.IO) {
        toggleGenericBookmark(userId, "ayah", ayahId.toString())
    }

    override suspend fun bookmarkAyah(userId: String, ayahId: Int): Result<Bookmark> {
        val logId = java.util.UUID.randomUUID().toString()
        return Result.success(Bookmark(logId, userId, "ayah", ayahId.toString(), System.currentTimeMillis().toString()))
    }

    override suspend fun getBookmarkedIds(userId: String, type: String): Set<String> = withContext(Dispatchers.IO) {
        try {
            if (userId.startsWith("demo") || userId.isBlank()) return@withContext emptySet()
            val bookmarks = supabaseClient.postgrest["bookmarks"]
                .select { filter { eq("user_id", userId); eq("type", type) } }
                .decodeList<com.noorlearn.data.remote.dto.BookmarkDto>()
            bookmarks.map { it.referenceId }.toSet()
        } catch (e: Exception) {
            android.util.Log.e("QuranRepo", "Error fetching bookmarks for $type", e)
            emptySet()
        }
    }

    override suspend fun toggleGenericBookmark(userId: String, type: String, referenceId: String): Result<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                if (userId.startsWith("demo") || userId.isBlank()) return@withContext Result.success(true)
                
                val existing = supabaseClient.postgrest["bookmarks"]
                    .select { filter { eq("user_id", userId); eq("type", type); eq("reference_id", referenceId) } }
                    .decodeList<com.noorlearn.data.remote.dto.BookmarkDto>()

                if (existing.isNotEmpty()) {
                    supabaseClient.postgrest["bookmarks"]
                        .delete { filter { eq("user_id", userId); eq("type", type); eq("reference_id", referenceId) } }
                    Result.success(false) // now unbookmarked
                } else {
                    val uuid = java.util.UUID.randomUUID().toString()
                    val dto = com.noorlearn.data.remote.dto.BookmarkDto(
                        id = uuid,
                        userId = userId,
                        type = type,
                        referenceId = referenceId,
                        timestamp = System.currentTimeMillis()
                    )
                    supabaseClient.postgrest["bookmarks"].insert(dto)
                    Result.success(true) // now bookmarked
                }
            } catch (e: Exception) {
                android.util.Log.e("QuranRepo", "Error toggling generic bookmark $type:$referenceId", e)
                Result.failure(e)
            }
        }

    override suspend fun submitRecitation(
        userId: String,
        surahId: Int,
        ayahId: Int,
        transcribedText: String,
        accuracyScore: Float,
        tipAi: String?
    ): Result<RecitationLog> = withContext(Dispatchers.IO) {
        try {
            val logId = java.util.UUID.randomUUID().toString()
            val timestamp = System.currentTimeMillis()
            
            if (!userId.startsWith("demo") && userId.isNotBlank()) {
                val dto = com.noorlearn.data.remote.dto.RecitationLogDto(
                    id = logId,
                    userId = userId,
                    surahId = surahId,
                    ayahId = ayahId,
                    transcribedText = transcribedText,
                    accuracyScore = accuracyScore,
                    tipAi = tipAi,
                    timestamp = timestamp
                )
                supabaseClient.postgrest["recitation_logs"].insert(dto)
            }

            Result.success(RecitationLog(
                id = logId,
                userId = userId,
                ayahId = ayahId,
                transcribedText = transcribedText,
                accuracyScore = accuracyScore,
                feedbackText = tipAi,
                createdAt = timestamp.toString()
            ))
        } catch (e: Exception) {
            android.util.Log.e("QuranRepo", "Error submitting recitation log", e)
            Result.failure(e)
        }
    }

    override suspend fun getRecitationLogs(userId: String): Result<List<RecitationLog>> =
        withContext(Dispatchers.IO) {
            try {
                if (userId.startsWith("demo") || userId.isBlank()) {
                    val mockLogs = listOf(
                        RecitationLog("1", userId, 1, "الحمد لله رب العالمين", 95f, "Great pronunciation of Al-hamdu lillah.", (System.currentTimeMillis() - 86400000).toString()),
                        RecitationLog("2", userId, 2, "الرحمن الرحيم", 88f, "Watch out for the Madd length on Ar-Rahman.", (System.currentTimeMillis() - 43200000).toString())
                    )
                    return@withContext Result.success(mockLogs)
                }

                val remoteLogs = supabaseClient.postgrest["recitation_logs"]
                    .select { filter { eq("user_id", userId) } }
                    .decodeList<com.noorlearn.data.remote.dto.RecitationLogDto>()

                val domainLogs = remoteLogs.map { dto ->
                    RecitationLog(
                        id = dto.id,
                        userId = dto.userId,
                        ayahId = dto.ayahId,
                        transcribedText = dto.transcribedText,
                        accuracyScore = dto.accuracyScore,
                        feedbackText = dto.tipAi,
                        createdAt = dto.timestamp.toString()
                    )
                }.sortedByDescending { it.createdAt }

                Result.success(domainLogs)
            } catch (e: Exception) {
                android.util.Log.e("QuranRepo", "Error fetching recitation logs", e)
                Result.failure(e)
            }
        }

    override suspend fun getReflections(userId: String): Result<List<Reflection>> =
        withContext(Dispatchers.IO) {
            try {
                val localEntities = reflectionDao.getReflectionsForUser(userId)
                if (localEntities.isNotEmpty()) {
                    return@withContext Result.success(localEntities.map { it.toDomain() })
                }

                if (!userId.startsWith("demo") && userId.isNotBlank()) {
                    try {
                        val remote = supabaseClient.postgrest["reflections"]
                            .select { filter { eq("user_id", userId) } }
                            .decodeList<com.noorlearn.data.remote.dto.ReflectionDto>()

                        val domainList = remote.map { dto ->
                            Reflection(
                                id = dto.id,
                                userId = dto.userId,
                                content = dto.content,
                                date = dto.date,
                                linkedAyahId = dto.linkedAyahId
                            )
                        }
                        
                        domainList.forEach { reflection ->
                            reflectionDao.insertReflection(ReflectionEntity.fromDomain(reflection))
                        }

                        return@withContext Result.success(domainList.sortedByDescending { it.date })
                    } catch (remoteEx: Exception) {
                        android.util.Log.e("QuranRepo", "Error fetching remote reflections (falling back)", remoteEx)
                    }
                }

                val mockReflections = listOf(
                    Reflection("1", userId, "This Ayah reminds me to start everything with Bismillah and keep a positive intention.", "2026-06-25", 1)
                )
                Result.success(mockReflections)
            } catch (e: Exception) {
                android.util.Log.e("QuranRepo", "Error fetching reflections", e)
                Result.failure(e)
            }
        }

    override suspend fun saveReflection(userId: String, reflection: Reflection): Result<Reflection> =
        withContext(Dispatchers.IO) {
            try {
                reflectionDao.insertReflection(ReflectionEntity.fromDomain(reflection))

                if (!userId.startsWith("demo") && userId.isNotBlank()) {
                    try {
                        val dto = com.noorlearn.data.remote.dto.ReflectionDto(
                            id = reflection.id,
                            userId = userId,
                            content = reflection.content,
                            date = reflection.date,
                            linkedAyahId = reflection.linkedAyahId
                        )
                        supabaseClient.postgrest["reflections"].insert(dto)
                    } catch (remoteEx: Exception) {
                        android.util.Log.e("QuranRepo", "Error saving reflection to remote Supabase (ignoring)", remoteEx)
                    }
                }
                Result.success(reflection)
            } catch (e: Exception) {
                android.util.Log.e("QuranRepo", "Error saving reflection", e)
                Result.failure(e)
            }
        }

    override suspend fun deleteReflection(userId: String, reflectionId: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                reflectionDao.deleteReflection(reflectionId, userId)

                if (!userId.startsWith("demo") && userId.isNotBlank()) {
                    try {
                        supabaseClient.postgrest["reflections"]
                            .delete { filter { eq("id", reflectionId); eq("user_id", userId) } }
                    } catch (remoteEx: Exception) {
                        android.util.Log.e("QuranRepo", "Error deleting reflection from remote Supabase (ignoring)", remoteEx)
                    }
                }
                Result.success(Unit)
            } catch (e: Exception) {
                android.util.Log.e("QuranRepo", "Error deleting reflection", e)
                Result.failure(e)
            }
        }
}
