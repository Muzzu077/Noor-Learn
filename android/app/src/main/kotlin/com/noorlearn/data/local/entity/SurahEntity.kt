package com.noorlearn.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "surahs")
data class SurahEntity(
    @PrimaryKey val id: Int,
    val nameArabic: String,
    val nameEnglishTranslation: String,
    val revelationType: String,
    val numberOfAyahs: Int
)
