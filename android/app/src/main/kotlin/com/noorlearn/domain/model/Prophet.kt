package com.noorlearn.domain.model

data class Prophet(
    val id: Int,
    val name: String,
    val arabicName: String,
    val period: String,
    val summary: String,
    val moralLesson: String,
    val imageUrl: String,
    val audioUrl: String
)
