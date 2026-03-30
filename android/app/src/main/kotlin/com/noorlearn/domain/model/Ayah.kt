package com.noorlearn.domain.model

data class Ayah(
    val id: Int,
    val surahId: Int,
    val ayahNumber: Int,
    val arabicText: String,
    val translationEn: String,
    val transliteration: String,
    val translationUr: String,
    val tafsirShort: String,
    val audioUrl: String? = null
)
