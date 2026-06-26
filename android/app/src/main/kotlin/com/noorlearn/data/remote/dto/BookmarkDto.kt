package com.noorlearn.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookmarkDto(
    val id: String = "",
    @SerialName("user_id") val userId: String = "",
    val type: String = "", // "ayah", "hadith", or "story"
    @SerialName("reference_id") val referenceId: String = "",
    val timestamp: Long = 0
)
