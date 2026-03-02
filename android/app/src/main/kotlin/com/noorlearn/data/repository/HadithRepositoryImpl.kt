package com.noorlearn.data.repository

import com.noorlearn.domain.model.Hadith
import com.noorlearn.domain.repository.HadithRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HadithRepositoryImpl @Inject constructor() : HadithRepository {
    override suspend fun getDailyHadith(): Hadith = withContext(Dispatchers.IO) {
        Hadith(
            id = 1L,
            source = "Bukhari",
            topic = "Knowledge",
            arabicText = "طَلَبُ الْعِلْمِ فَرِيضَةٌ عَلَى كُلِّ مُسْلِمٍ",
            translationEn = "Seeking knowledge is an obligation upon every Muslim.",
            narratorChain = "",
            grade = "Sahih"
        )
    }

    override suspend fun getHadithsBySource(source: String): List<Hadith> = withContext(Dispatchers.IO) {
        listOf(
            getDailyHadith()
        )
    }

    override suspend fun explainHadith(hadithId: String): Result<String> {
        return Result.success("This hadith emphasizes the importance of gaining knowledge...")
    }
}
