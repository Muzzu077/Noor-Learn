package com.noorlearn.domain.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val roleMode: String,
    val streakDays: Int,
    val isPremium: Boolean,
    val createdAt: String
)
