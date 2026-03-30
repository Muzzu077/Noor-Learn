package com.noorlearn.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SurahDto(
    val id: Int = 0,
    @SerialName("arabic_name") val arabicName: String = "",
    val name: String = "",
    val nameRoman: String = "",
    @SerialName("revelation_type") val revelationType: String = "",
    @SerialName("total_ayah") val totalAyah: Int = 0,
    val meaning: String = ""
)
