package com.noorlearn.domain.model

data class RecitationLog(
    val id: String,
    val userId: String,
    val ayahId: Int,
    val transcribedText: String,
    val accuracyScore: Float, 
    val feedbackText: String?, 
    val createdAt: String
)
