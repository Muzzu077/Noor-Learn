package com.noorlearn.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecitationLogDto(
    val id: String = "",
    @SerialName("user_id") val userId: String = "",
    @SerialName("surah_id") val surahId: Int = 0,
    @SerialName("ayah_id") val ayahId: Int = 0,
    @SerialName("transcribed_text") val transcribedText: String = "",
    @SerialName("accuracy_score") val accuracyScore: Float = 0f,
    @SerialName("tip_ai") val tipAi: String? = null,
    val timestamp: Long = 0
)
