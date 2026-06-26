package com.noorlearn.data.speech

import com.noorlearn.domain.model.RecitationLog
import java.util.UUID

enum class WordStatus {
    CORRECT,
    INCORRECT,
    SKIPPED
}

data class AlignedWord(
    val word: String,
    val status: WordStatus
)

object RecitationFeedbackEngine {
    private val HARAKAT_PATTERN = Regex("[\u064B-\u065F\u0670]") // Fatha, Damma, Kasra, Shadda, Sukoon, etc.
    private val PUNCTUATION_PATTERN = Regex("[\u06D6-\u06ED\u0621\u062C\u063E\u06D6-\u06DF\u06E0-\u06E4\u06E7-\u06EC\u06EA-\u06ED,.?!;:\u060C\u061F\u06DD]")

    fun normalize(text: String): String {
        var normalized = text
        // Remove diacritics
        normalized = normalized.replace(HARAKAT_PATTERN, "")
        // Remove Quranic punctuation
        normalized = normalized.replace(PUNCTUATION_PATTERN, "")
        // Normalize Alifs and Hamzas
        normalized = normalized.replace(Regex("[\u0622\u0623\u0625\u0671]"), "\u0627") // آ, أ, إ, ٱ -> ا
        normalized = normalized.replace("\u0624", "\u0648") // ؤ -> و
        normalized = normalized.replace("\u0626", "\u064A") // ئ -> ي
        // Normalize Taa Marbuta
        normalized = normalized.replace("\u0629", "\u0647") // ة -> ه
        // Normalize Yaa
        normalized = normalized.replace("\u0649", "\u064A") // ى -> ي
        // Trim spaces
        normalized = normalized.replace(Regex("\\s+"), " ").trim()
        return normalized
    }

    /**
     * Compares the user's transcription against the reference text.
     * Returns a list of aligned words corresponding to the reference text,
     * along with an accuracy score between 0.0 and 100.0.
     */
    fun analyzeRecitation(
        referenceText: String,
        transcribedText: String
    ): Pair<List<AlignedWord>, Float> {
        val originalWords = referenceText.split(" ").filter { it.isNotBlank() }
        if (originalWords.isEmpty()) return Pair(emptyList(), 0f)
        if (transcribedText.isBlank()) {
            return Pair(originalWords.map { AlignedWord(it, WordStatus.SKIPPED) }, 0f)
        }

        val refNorm = originalWords.map { normalize(it) }
        val transNorm = transcribedText.split(" ").filter { it.isNotBlank() }.map { normalize(it) }

        val n = refNorm.size
        val m = transNorm.size

        // DP table for Levenshtein Distance
        val dp = Array(n + 1) { IntArray(m + 1) }
        for (i in 0..n) dp[i][0] = i
        for (j in 0..m) dp[0][j] = j

        for (i in 1..n) {
            for (j in 1..m) {
                val cost = if (refNorm[i - 1] == transNorm[j - 1]) 0 else 1
                dp[i][j] = minOf(
                    dp[i - 1][j] + 1, // deletion
                    dp[i][j - 1] + 1, // insertion
                    dp[i - 1][j - 1] + cost // substitution / match
                )
            }
        }

        // Backtrack to find aligned statuses
        val alignment = MutableList<AlignedWord?>(n) { null }
        var i = n
        var j = m

        while (i > 0 || j > 0) {
            if (i > 0 && j > 0) {
                val cost = if (refNorm[i - 1] == transNorm[j - 1]) 0 else 1
                if (dp[i][j] == dp[i - 1][j - 1] + cost) {
                    val status = if (cost == 0) WordStatus.CORRECT else WordStatus.INCORRECT
                    alignment[i - 1] = AlignedWord(originalWords[i - 1], status)
                    i--
                    j--
                    continue
                }
            }
            if (i > 0 && (j == 0 || dp[i][j] == dp[i - 1][j] + 1)) {
                alignment[i - 1] = AlignedWord(originalWords[i - 1], WordStatus.SKIPPED)
                i--
            } else if (j > 0 && (i == 0 || dp[i][j] == dp[i][j - 1] + 1)) {
                j-- // skip extra words in transcription
            }
        }

        // Clean any nulls just in case
        val alignedList = alignment.mapIndexed { idx, alignedWord ->
            alignedWord ?: AlignedWord(originalWords[idx], WordStatus.SKIPPED)
        }

        // Calculate score
        val correctCount = alignedList.count { it.status == WordStatus.CORRECT }
        val score = (correctCount.toFloat() / n) * 100f

        return Pair(alignedList, score)
    }

    /**
     * Builds an AI prompt for generating specific pronunciation tips based on the mistakes.
     */
    fun buildFeedbackPrompt(
        surahName: String,
        ayahNumber: Int,
        referenceText: String,
        transcribedText: String,
        alignedWords: List<AlignedWord>
    ): String {
        val mistakes = alignedWords.filter { it.status != WordStatus.CORRECT }.map { it.word }
        
        return """You are a warm, supportive Arabic/Qur'an pronunciation coach.
The student is practicing reciting Surah $surahName, Ayah $ayahNumber.
Correct verse (Arabic): "$referenceText"
Student's recitation transcription: "$transcribedText"
Mistakes (words incorrect or skipped): ${mistakes.joinToString(", ")}

Provide a brief feedback (1-3 sentences maximum) in English.
Identify if there are letters that were mispronounced or skipped (e.g. letters with similar sounds like Haa/Khaa, Seen/Saad, or general pronunciation flow).
Speak directly to the student ("You recited...", "Focus on..."). Keep it encouraging and constructive. Do not use complex technical terms without simple explanations."""
    }
}
