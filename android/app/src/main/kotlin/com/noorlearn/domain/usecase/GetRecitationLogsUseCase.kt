package com.noorlearn.domain.usecase

import com.noorlearn.domain.repository.QuranRepository
import com.noorlearn.domain.model.RecitationLog
import javax.inject.Inject

class GetRecitationLogsUseCase @Inject constructor(
    private val quranRepository: QuranRepository
) {
    suspend operator fun invoke(userId: String): Result<List<RecitationLog>> {
        return quranRepository.getRecitationLogs(userId)
    }
}
