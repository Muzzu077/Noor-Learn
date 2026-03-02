package com.noorlearn.domain.model

data class Surah(
    val id: Int, 
    val name: String,
    val arabicName: String,
    val revelationType: String, 
    val totalAyah: Int,
    val meaning: String
)
