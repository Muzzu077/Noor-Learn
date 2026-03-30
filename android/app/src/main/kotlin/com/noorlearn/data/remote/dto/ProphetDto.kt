package com.noorlearn.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProphetDto(
    val id: String,
    @SerialName("name_arabic") val nameArabic: String,
    @SerialName("name_english") val nameEnglish: String,
    @SerialName("short_summary") val shortSummary: String,
    @SerialName("full_story") val fullStory: String,
    @SerialName("timeline_era") val timelineEra: String,
    @SerialName("image_url") val imageUrl: String = "",
    @SerialName("audio_url") val audioUrl: String = ""
)
