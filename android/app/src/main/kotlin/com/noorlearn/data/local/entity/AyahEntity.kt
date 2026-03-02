package com.noorlearn.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ayahs")
data class AyahEntity(
    @PrimaryKey val id: Int,
    val surahId: Int,
    val ayahNumber: Int,
    val textArabic: String,
    val textTranslation: String,
    val audioUrl: String?
)
