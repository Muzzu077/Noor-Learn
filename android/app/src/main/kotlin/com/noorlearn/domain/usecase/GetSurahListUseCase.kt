package com.noorlearn.domain.usecase

import com.noorlearn.domain.model.Surah
import com.noorlearn.domain.repository.QuranRepository
import javax.inject.Inject

class GetSurahListUseCase @Inject constructor(
    private val quranRepository: QuranRepository
) {
    suspend operator fun invoke(): List<Surah> {
        return quranRepository.getSurahs()
    }
}
