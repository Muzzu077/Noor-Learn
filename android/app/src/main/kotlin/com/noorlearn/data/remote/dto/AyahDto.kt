package com.noorlearn.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AyahDto(
    val id: Int = 0,
    @SerialName("surah_id") val surahId: Int = 0,
    @SerialName("ayah_number") val ayahNumber: Int = 0,
    @SerialName("arabic_text") val arabicText: String = "",
    @SerialName("translation_en") val translationEn: String = "",
    val transliteration: String = "",
    @SerialName("audio_url") val audioUrl: String? = null
)
