package com.noorlearn.data.remote

import com.noorlearn.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.first
import org.json.JSONArray
import org.json.JSONObject
import com.noorlearn.data.local.preferences.UserPreferencesDataStore
import javax.inject.Inject

class EdgeFunctionService @Inject constructor(
    private val userPreferences: UserPreferencesDataStore
) {
    companion object {
        // Singleton HttpClient — one per app process
        private val httpClient = HttpClient(Android) {
            engine {
                connectTimeout = 30_000
                socketTimeout = 60_000
            }
        }

        private const val OPENROUTER_URL = "https://openrouter.ai/api/v1/chat/completions"
        private const val MAX_QUESTION_LENGTH = 2000
        
        // List of reliable free models to try if one is rate-limited
        private val FREE_MODELS = listOf(
            "google/gemini-2.0-flash-lite-preview-02-05:free",
            "meta-llama/llama-3.3-70b-instruct:free",
            "deepseek/deepseek-r1:free",
            "qwen/qwen-2.5-72b-instruct:free",
            "google/gemini-2.0-flash-exp:free",
            "openrouter/auto"
        )

        private const val SYSTEM_PROMPT = """You are NoorLearn AI — a friendly, warm Islamic learning companion inside a mobile app.

CRITICAL RULES:
• Be CONVERSATIONAL. Talk like a kind teacher, not a textbook.
• Keep responses SHORT — 2-5 sentences for simple questions. Only go longer if the user asks for detail.
• For greetings like "Assalamualaikum" or "hello": reply with "Wa Alaikum Assalam!" and ask what they'd like to learn. Do NOT lecture.
• For simple questions: give a direct answer first, then a brief reference.
• Only cite Qur'an/Hadith when it adds value, not on every response.
• Match the user's energy — short question = short answer, detailed question = detailed answer.
• If unsure or if it's a fatwa-level question, say "Please consult a qualified scholar for this."
• Never repeat the user's greeting back (don't say Assalamualaikum if they already said it).
• Do not start every response with Bismillah — only when naturally appropriate.
• Be encouraging and supportive of the learner's journey."""
    }

    suspend fun askChatbot(question: String): Result<String> = withContext(Dispatchers.IO) {
        // Input validation — prevent abuse
        if (question.isBlank()) return@withContext Result.failure(Exception("Question cannot be empty."))
        val sanitizedQuestion = question.take(MAX_QUESTION_LENGTH)
        val apiKey = BuildConfig.OPENROUTER_API_KEY
        val level = userPreferences.learningLevel.first() ?: "Beginner"
        
        val dynamicSystemPrompt = buildString {
            append(SYSTEM_PROMPT)
            append("\n\nContext: The user's current Arabic learning level is '$level'. ")
            if (level == "Beginner") {
                append("Explain concepts in very simple terms. Always use English transliterations for Arabic words. Do not use complex Fiqh terminology without basic explanations. strongly encourage the user.")
            } else if (level == "Advanced") {
                append("The user is advanced. You may use deep academic Fiqh/Tafsir terminology. Provide exact linguistic breakdowns of Arabic words if relevant.")
            }
        }
        
        var lastError: String? = null
        for (model in FREE_MODELS) {
            try {
                val messagesArray = JSONArray().apply {
                    put(JSONObject().apply {
                        put("role", "system")
                        put("content", dynamicSystemPrompt)
                    })
                    put(JSONObject().apply {
                        put("role", "user")
                        put("content", sanitizedQuestion)
                    })
                }

                val jsonBody = JSONObject().apply {
                    put("model", model)
                    put("messages", messagesArray)
                    put("max_tokens", 800)
                    put("temperature", 0.7)
                }.toString()

                val response = httpClient.post(OPENROUTER_URL) {
                    header("Authorization", "Bearer $apiKey")
                    header("HTTP-Referer", "https://noorlearn.app")
                    header("X-Title", "NoorLearn Islamic App")
                    contentType(ContentType.Application.Json)
                    setBody(jsonBody)
                }

                val responseText = response.bodyAsText()
                if (BuildConfig.DEBUG) {
                    android.util.Log.d("EdgeFunctionService", "[$model] Status: ${response.status}")
                }

                when (response.status.value) {
                    in 200..299 -> {
                        val responseJson = JSONObject(responseText)
                        if (responseJson.has("choices")) {
                            val answer = responseJson
                                .getJSONArray("choices")
                                .getJSONObject(0)
                                .getJSONObject("message")
                                .getString("content")
                            return@withContext Result.success(answer.trim())
                        }
                    }
                    429 -> {
                        lastError = "Rate limited on $model, trying next..."
                        if (BuildConfig.DEBUG) android.util.Log.w("EdgeFunctionService", "Rate limited: $model")
                        continue
                    }
                    else -> {
                        lastError = "Service error (${response.status.value})"
                        if (BuildConfig.DEBUG) android.util.Log.w("EdgeFunctionService", "HTTP ${response.status.value} for $model")
                        continue
                    }
                }
            } catch (e: Exception) {
                lastError = e.message ?: "Unknown error"
                if (BuildConfig.DEBUG) android.util.Log.e("EdgeFunctionService", "Error with $model", e)
            }
        }
        
        Result.failure(Exception(lastError ?: "The Islamic AI assistant is currently unavailable."))
    }

    private fun getMockResponse(question: String): String {
        val lowerQ = question.lowercase()
        return when {
            lowerQ.contains("salam") || lowerQ.contains("hello") ->
                "Wa Alaikum Assalam! I am your NoorLearn AI assistant. I'm currently in offline mode. Please check your internet connection and try again."
            lowerQ.contains("prayer") || lowerQ.contains("salah") ->
                "Prayer (Salah) is the second pillar of Islam. It is obligatory for every adult Muslim to perform the five daily prayers: Fajr, Dhuhr, Asr, Maghrib, and Isha. The Prophet (SAW) said: 'The key to Paradise is prayer.' (Tirmidhi)"
            lowerQ.contains("quran") || lowerQ.contains("surah") ->
                "The Qur'an is the literal word of Allah revealed to Prophet Muhammad (SAW) through Angel Jibreel over 23 years. It contains 114 Surahs and 6,236 Ayahs. Reading even a single letter brings ten rewards! (Tirmidhi)"
            lowerQ.contains("fasting") || lowerQ.contains("ramadan") ->
                "Fasting during Ramadan is the fourth pillar of Islam. Allah says in the Qur'an: 'O you who believe, fasting is prescribed for you as it was prescribed for those before you, that you may attain Taqwa.' (Al-Baqarah 2:183)"
            lowerQ.contains("hajj") || lowerQ.contains("pilgrimage") ->
                "Hajj is the fifth pillar of Islam, obligatory once in a lifetime for those who are physically and financially able. The Prophet (SAW) said: 'An accepted Hajj has no reward except Paradise.' (Bukhari & Muslim)"
            lowerQ.contains("zakat") || lowerQ.contains("charity") ->
                "Zakat is the third pillar of Islam — 2.5% of one's savings given annually to those in need. Allah says: 'Take from their wealth a charity by which you purify them and cause them increase.' (At-Tawbah 9:103)"
            else ->
                "I'm currently in offline mode. Please check your internet connection and try again. In the meantime, explore the Qur'an, Hadith, and Prophet Stories sections of the app for Islamic knowledge."
        }
    }
}
