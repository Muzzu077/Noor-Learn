package com.noorlearn.domain.repository

import com.noorlearn.domain.model.Hadith

interface HadithRepository {
    suspend fun getDailyHadith(): Hadith
    suspend fun getHadithsBySource(source: String): List<Hadith>
    suspend fun explainHadith(hadithId: String): Result<String>
}
