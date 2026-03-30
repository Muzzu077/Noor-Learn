package com.noorlearn.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookmarkDto(
    val id: Long = 0,
    @SerialName("user_id") val userId: String = "",
    @SerialName("ayah_id") val ayahId: Int = 0,
    @SerialName("created_at") val createdAt: String = ""
)
