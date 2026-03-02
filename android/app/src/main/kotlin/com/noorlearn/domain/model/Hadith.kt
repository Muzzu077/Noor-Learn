package com.noorlearn.domain.model

data class Hadith(
    val id: Long,
    val source: String,
    val topic: String,
    val arabicText: String,
    val translationEn: String,
    val narratorChain: String,
    val grade: String
)
