package com.noorlearn.domain.model

data class ParaStory(
    val juzNumber: Int,
    val title: String,
    val arabicTitle: String,
    val story: String,
    val themes: List<String>,
    val lessons: List<String>,
    val audioUrl: String = ""
)
