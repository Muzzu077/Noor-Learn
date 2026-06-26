package com.noorlearn.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReflectionDto(
    val id: String = "",
    @SerialName("user_id") val userId: String = "",
    val content: String = "",
    val date: String = "",
    @SerialName("linked_ayah_id") val linkedAyahId: Int? = null
)
