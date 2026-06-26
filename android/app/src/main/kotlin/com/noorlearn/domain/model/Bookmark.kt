package com.noorlearn.domain.model

data class Bookmark(
    val id: String,
    val userId: String,
    val type: String, // "ayah", "hadith", or "story"
    val referenceId: String,
    val createdAt: String
)
