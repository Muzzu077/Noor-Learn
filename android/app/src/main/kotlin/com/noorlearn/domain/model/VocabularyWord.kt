package com.noorlearn.domain.model

data class VocabularyWord(
    val id: Int,
    val arabic: String,
    val transliteration: String,
    val english: String,
    val occurrences: Int,
    val exampleArabic: String,
    val exampleTranslation: String,
    val isMastered: Boolean = false
)
