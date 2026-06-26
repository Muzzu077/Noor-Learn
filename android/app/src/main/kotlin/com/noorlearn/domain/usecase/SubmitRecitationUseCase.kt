package com.noorlearn.domain.usecase

import com.noorlearn.domain.repository.QuranRepository
import com.noorlearn.domain.model.RecitationLog
import javax.inject.Inject

class SubmitRecitationUseCase @Inject constructor(
    private val quranRepository: QuranRepository
) {
    suspend operator fun invoke(
        userId: String,
        surahId: Int,
        ayahId: Int,
        transcribedText: String,
        accuracyScore: Float,
        tipAi: String?
    ): Result<RecitationLog> {
        return quranRepository.submitRecitation(
            userId, surahId, ayahId, transcribedText, accuracyScore, tipAi
        )
    }
}
