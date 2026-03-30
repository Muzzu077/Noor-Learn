package com.noorlearn.data.repository

import android.content.Context
import com.noorlearn.data.local.dao.AyahDao
import com.noorlearn.data.local.dao.SurahDao
import com.noorlearn.data.local.entity.AyahEntity
import com.noorlearn.data.local.entity.SurahEntity
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class SurahDtoLocal(
    val id: Int = 0,
    @SerialName("name_arabic") val arabicName: String = "",
    @SerialName("name_english_translation") val name: String = "",
    @SerialName("name_roman") val nameRoman: String = "",
    @SerialName("revelation_type") val revelationType: String = "",
    @SerialName("number_of_ayahs") val totalAyah: Int = 0,
    val meaning: String = ""
)

@Serializable
data class AyahDtoLocal(
    val id: Int = 0,
    @SerialName("surah_id") val surahId: Int = 0,
    @SerialName("ayah_number") val ayahNumber: Int = 0,
    @SerialName("text_arabic") val arabicText: String = "",
    @SerialName("text_translation") val translationEn: String = "",
    @SerialName("transliteration") val transliteration: String = "",
    @SerialName("audio_url") val audioUrl: String? = null
)

@Serializable
data class QuranPayloadLocal(
    val surahList: List<SurahDtoLocal>,
    val ayahList: List<AyahDtoLocal>
)

@Singleton
class QuranDataIngestor @Inject constructor(
    private val surahDao: SurahDao,
    private val ayahDao: AyahDao
) {
    suspend fun ingestFromAssetsIfNeeded(context: Context) {
        val count = surahDao.getAllSurahs().size
        if (count < 114) {
            try {
                android.util.Log.d("QuranIngestor", "Ingesting Quran data...")
                val jsonString = context.assets.open("quran_data.json").bufferedReader().use { it.readText() }
                
                // Explicitly typing EVERYTHING to avoid inference issues
                val myJson = Json { ignoreUnknownKeys = true; isLenient = true }
                val mySerializer = QuranPayloadLocal.serializer()
                val payload: QuranPayloadLocal = myJson.decodeFromString(mySerializer, jsonString)
                
                val surahEntities = payload.surahList.map { d ->
                    SurahEntity(
                        id = d.id,
                        nameArabic = d.arabicName,
                        nameEnglishTranslation = d.name,
                        nameRoman = d.nameRoman,
                        revelationType = d.revelationType,
                        numberOfAyahs = d.totalAyah
                    )
                }
                surahDao.insertSurahs(surahEntities)
                
                val ayahEntities = payload.ayahList.map { d ->
                    AyahEntity(
                        id = d.id,
                        surahId = d.surahId,
                        ayahNumber = d.ayahNumber,
                        textArabic = d.arabicText,
                        textTranslation = d.translationEn,
                        textTransliteration = d.transliteration,
                        audioUrl = d.audioUrl
                    )
                }
                ayahEntities.chunked(1000).forEach { ayahDao.insertAyahs(it) }
                android.util.Log.d("QuranIngestor", "Success! ${surahEntities.size} surahs.")
            } catch (e: Exception) {
                android.util.Log.e("QuranIngestor", "Error", e)
            }
        }
    }
}
